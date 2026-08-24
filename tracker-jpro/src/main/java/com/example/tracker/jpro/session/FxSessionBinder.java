/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro.session;

import javafx.collections.ListChangeListener;
import javafx.event.EventDispatcher;
import javafx.stage.Window;

import org.tentackle.common.LocaleProvider;
import org.tentackle.log.Logger;
import org.tentackle.session.Session;

import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * Binds a user's session and locale to the JavaFX application thread.
 * <p>
 * This is the multi-user equivalent of what a dispatcher servlet does for a request, and of what
 * {@code DesktopApplication} does once per JVM for the single desktop user. Because all browser
 * sessions share <em>one</em> FX thread, the binding cannot be permanent: it must bracket every
 * unit of work.
 * <p>
 * It is needed because two things resolve the session implicitly and would otherwise fail or,
 * worse, serve the wrong user:
 * <ol>
 *   <li>Cached PDOs deliberately carry no session — they are immutable and shared concurrently by
 *       many threads. The moment such a PDO needs one (lazy loading of components, navigating an
 *       outgoing relation) it falls back to the thread-local session.</li>
 *   <li>{@link LocaleProvider} is thread-local. Without a per-user locale every user would fall
 *       back to the JVM's locale.</li>
 * </ol>
 * The bracket mirrors {@code RemoteDbDelegateInvocationHandler.updateThreadInfo/clearThreadInfo},
 * which does the same three calls on the middle tier.
 * <p>
 * Coverage is achieved by wrapping the {@link EventDispatcher} of every window: a window's
 * dispatcher is the outermost link of the dispatch chain, so the wrapper brackets the complete
 * capture &rarr; target &rarr; bubble pass of every event routed into that window. {@link #install()}
 * hooks {@link Window#getWindows()}, which JavaFX updates exactly when a window is shown or hidden,
 * so stages, popups, context menus and tooltips are all covered without registering anything.
 * <p>
 * The few places where Tentackle enters the FX thread through a bare {@code Platform.runLater} are
 * covered by {@link #wrap(UserScope, Runnable)}, applied in
 * {@link com.example.tracker.jpro.TrackerJProFxUtilities} and
 * {@link com.example.tracker.jpro.TrackerJProRdcUtilities}.
 *
 * @see UserScope
 */
public final class FxSessionBinder {

  private static final Logger LOGGER = Logger.get();

  /** windows whose event dispatcher is already wrapped. */
  private static final Set<Window> WRAPPED = Collections.newSetFromMap(new WeakHashMap<>());

  private static boolean installed;


  /**
   * Installs the global window hook.
   * <p>
   * Must be invoked once per JVM on the FX thread, see
   * {@link com.example.tracker.jpro.TrackerJProBootstrap}.
   */
  public static synchronized void install() {
    if (!installed) {
      installed = true;
      Window.getWindows().addListener((ListChangeListener<Window>) change -> {
        while (change.next()) {
          if (change.wasAdded()) {
            for (Window window : change.getAddedSubList()) {
              attach(window);
            }
          }
        }
      });
      // windows already showing (should be none, but install() must not depend on that)
      for (Window window : Window.getWindows()) {
        attach(window);
      }
      LOGGER.info("FX session binder installed");
    }
  }

  /**
   * Runs a unit of work with the scope's session and locale bound to the current thread.
   *
   * @param <T> the result type
   * @param scope the user scope
   * @param body the unit of work
   * @return the result of the unit of work
   */
  public static <T> T call(UserScope scope, Supplier<T> body) {
    Objects.requireNonNull(scope, "scope");
    Session session = scope.getSession();
    LocaleProvider localeProvider = LocaleProvider.getInstance();

    // save and restore instead of clear: event dispatch nests, and the FX thread is shared
    Session previousSession = Session.getCurrentSession();
    Locale previousLocale = localeProvider.getCurrentLocale();
    Thread previousOwner = session.getOwnerThread();

    session.makeCurrent();
    session.setOwnerThread(Thread.currentThread());
    localeProvider.setCurrentLocale(scope.getLocale());
    try {
      return body.get();
    }
    finally {
      session.setOwnerThread(previousOwner);
      localeProvider.setCurrentLocale(previousLocale);
      Session.setCurrentSession(previousSession);
    }
  }

  /**
   * Runs a unit of work with the scope's session and locale bound to the current thread.
   *
   * @param scope the user scope
   * @param body the unit of work
   */
  public static void run(UserScope scope, Runnable body) {
    call(scope, () -> {
      body.run();
      return null;
    });
  }

  /**
   * Wraps a runnable so that it will be bound to the scope when it is finally run.
   * <p>
   * For the handful of places that enter the FX thread through {@code Platform.runLater}
   * rather than through the event system.
   *
   * @param scope the user scope, null to run unbound
   * @param body the runnable, may be null
   * @return the wrapped runnable, null if {@code body} is null
   */
  public static Runnable wrap(UserScope scope, Runnable body) {
    // null in, null out: callers such as FxUtilities#showNotification treat a null runnable
    // as "nothing to do" and must not be handed a wrapper that then fails on it
    return scope == null || body == null ? body : () -> run(scope, body);
  }

  /**
   * Gets the scope currently bound to this thread.
   * <p>
   * For diagnostics only — application code must never resolve the user this way.
   *
   * @return the session bound to this thread, null if none
   */
  public static Session getBoundSession() {
    return Session.getCurrentSession();
  }


  /**
   * Replaces a window's event dispatcher by one that binds the window's user.
   * <p>
   * Idempotent. Normally invoked by the global hook when a window is shown; may be invoked earlier
   * for windows that are handed to JPro rather than shown by JavaFX.
   * <p>
   * The scope is resolved lazily on every dispatch, not captured: the primary stage of a JPro
   * instance is shown before its user logs in, and stays the same stage afterwards.
   *
   * @param window the window
   */
  public static synchronized void attach(Window window) {
    if (WRAPPED.add(window)) {
      EventDispatcher delegate = window.getEventDispatcher();
      window.setEventDispatcher((event, tail) -> {
        UserScope scope = UserScope.of(window);
        return scope == null ? delegate.dispatchEvent(event, tail)
                             : call(scope, () -> delegate.dispatchEvent(event, tail));
      });
      LOGGER.fine(() -> "session binding installed for " + window);
    }
  }


  private FxSessionBinder() {}

}
