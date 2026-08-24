/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.jpro.session.FxSessionBinder;
import com.example.tracker.jpro.session.UserScope;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;

import org.tentackle.common.LocaleProvider;
import org.tentackle.common.Service;
import org.tentackle.fx.FxUtilities;
import org.tentackle.fx.rdc.RdcUtilities;
import org.tentackle.log.Logger;
import org.tentackle.pdo.PersistentDomainObject;
import org.tentackle.session.Session;
import org.tentackle.session.SessionDependable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * RDC utilities for the web client.
 * <p>
 * The inherited {@code runInBackground} submits the closure to the JVM-wide
 * {@link org.tentackle.session.ModificationTracker}, whose task dispatcher makes the
 * <em>technical</em> session current. In a multi-user JVM that is the wrong user, so background
 * work here borrows a second session for the same user out of the multi-user pool instead — which
 * is exactly the borrow/return pattern a dispatcher servlet uses per request.
 *
 * @see FxSessionBinder
 */
@Service(RdcUtilities.class)
public class TrackerJProRdcUtilities extends RdcUtilities {

  private static final Logger LOGGER = Logger.get();

  /**
   * Creates the utilities.
   */
  public TrackerJProRdcUtilities() {
    // see -Xlint:missing-explicit-ctor since Java 16
  }

  @Override
  public <V> void runInBackground(Node node, Supplier<V> runner, Consumer<V> updateUI, Consumer<RuntimeException> failedUI) {
    UserScope scope = UserScope.of(node);
    if (scope == null) {
      scope = UserScope.current();
    }
    if (scope == null) {
      // no logged-in user involved: technical background work such as cache preloading
      super.runInBackground(node, runner, updateUI, failedUI);
      return;
    }

    UserScope userScope = scope;
    Cursor oldCursor = setWaitCursor(node);

    Thread thread = new Thread(() -> {
      TrackerWebApplication application = TrackerJProApplication.getWebApplication();
      Session session = application.getSession(userScope.getInstanceID());
      if (session == null) {
        Platform.runLater(FxSessionBinder.wrap(userScope,
                () -> fail(node, oldCursor, failedUI, new IllegalStateException("no session for " + userScope))));
        return;
      }
      try {
        session.setOwnerThread(Thread.currentThread());
        session.makeCurrent();
        LocaleProvider.getInstance().setCurrentLocale(userScope.getLocale());
        if (runner instanceof SessionDependable sessionDependable) {
          sessionDependable.setSession(session);
        }
        V value = runner.get();
        Platform.runLater(FxSessionBinder.wrap(userScope, () -> {
          restoreCursor(node, oldCursor);
          if (updateUI != null) {
            updateUI.accept(value);
          }
        }));
      }
      catch (RuntimeException rx) {
        Platform.runLater(FxSessionBinder.wrap(userScope, () -> fail(node, oldCursor, failedUI, rx)));
      }
      finally {
        LocaleProvider.getInstance().setCurrentLocale(null);
        Session.setCurrentSession(null);
        session.setOwnerThread(null);
        application.putSession(session);
      }
    }, "bg-" + userScope.getInstanceID());

    thread.setDaemon(true);
    thread.start();
  }


  /**
   * {@inheritDoc}
   * <p>
   * Overridden to switch the cache off. The cache is keyed by the PDO class in this JVM-wide
   * singleton, and a controller is reused as soon as its stage is no longer showing — which in a
   * multi-user JVM means user B would be handed the search view user A just closed, along with
   * A's PDOs and A's session. Cheaper to build a new controller than to get this wrong.
   */
  @Override
  protected <T extends PersistentDomainObject<T>> boolean isSearchCachingEnabled(T pdo) {
    return false;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to switch the cache off, see {@link #isSearchCachingEnabled(PersistentDomainObject)}.
   */
  @Override
  protected <T extends PersistentDomainObject<T>> boolean isCrudCachingEnabled(T pdo) {
    return false;
  }

  /**
   * Sets the wait cursor.
   *
   * @param node the node, null if none
   * @return the cursor to restore, null if none
   */
  protected Cursor setWaitCursor(Node node) {
    if (node == null) {
      return null;
    }
    Cursor waitCursor = FxUtilities.getInstance().getWaitCursor(node);
    Cursor cursor = node.getCursor();
    node.setCursor(waitCursor);
    // don't restore the wait cursor if it was already set by another background task
    return waitCursor.equals(cursor) ? null : cursor;
  }

  /**
   * Restores the cursor.
   *
   * @param node the node, null if none
   * @param oldCursor the cursor to restore, which may be null, i.e. inherited from the parent
   */
  protected void restoreCursor(Node node, Cursor oldCursor) {
    if (node != null) {
      node.setCursor(oldCursor);
    }
  }

  private void fail(Node node, Cursor oldCursor, Consumer<RuntimeException> failedUI, RuntimeException rx) {
    restoreCursor(node, oldCursor);
    if (failedUI == null) {
      LOGGER.severe("background task failed", rx);
    }
    else {
      failedUI.accept(rx);
    }
  }

}
