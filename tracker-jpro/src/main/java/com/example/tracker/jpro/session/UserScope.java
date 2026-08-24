/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro.session;

import com.jpro.webapi.WebAPI;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.tentackle.log.Logger;
import org.tentackle.session.Session;
import org.tentackle.session.SessionInfo;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Everything that belongs to one browser session.
 * <p>
 * A JPro <em>instance</em> is one browser session and thus takes the role of the HTTP session in a
 * servlet container: its {@link WebAPI#getInstanceID() instance id} is the web session key of the
 * {@link com.example.tracker.jpro.TrackerWebApplication}.
 * <p>
 * Unlike a servlet, which borrows a session per request and returns it immediately, the FX UI keeps
 * {@link org.tentackle.session.SessionDependable} PDOs alive between events. The scope therefore holds
 * <em>one</em> upstream session for the lifetime of the instance and returns it to the pool when the
 * instance is closed. Background tasks are the exception: they borrow a second session for the same
 * user, see {@link com.example.tracker.jpro.TrackerJProRdcUtilities}.
 *
 * @see FxSessionBinder
 */
public final class UserScope {

  private static final Logger LOGGER = Logger.get();

  /** the live scopes, keyed by the JPro instance id. */
  private static final Map<String, UserScope> SCOPES = new ConcurrentHashMap<>();

  /**
   * Windows already resolved to a scope.
   * <p>
   * Weak, because JavaFX windows are dropped without notice. Never holds negative results:
   * the primary stage of an instance is shown <em>before</em> its user logs in.
   */
  private static final Map<Window, UserScope> WINDOWS = Collections.synchronizedMap(new WeakHashMap<>());


  /**
   * Creates and registers the scope for a logged-in instance.
   *
   * @param webAPI the web API of the JPro instance
   * @param sessionInfo the session info of the logged-in user
   * @param session the upstream session
   * @param locale the effective locale of the browser
   * @return the scope
   */
  public static UserScope create(WebAPI webAPI, SessionInfo sessionInfo, Session session, Locale locale) {
    String instanceID = webAPI.getInstanceID();
    UserScope scope = new UserScope(webAPI, sessionInfo, session, locale);
    SCOPES.put(instanceID, scope);
    LOGGER.info("web session {0} opened for {1} with {2}", instanceID, sessionInfo, locale);
    return scope;
  }

  /**
   * Gets the scope bound to the current thread.
   * <p>
   * For the few framework callbacks that get no owner at all, such as
   * {@link org.tentackle.fx.FxUtilities#terminate()}. Application code must never resolve the user
   * this way.
   *
   * @return the scope, null if this thread is not bound to a user
   */
  public static UserScope current() {
    Session session = Session.getCurrentSession();
    if (session != null) {
      for (UserScope scope : SCOPES.values()) {
        if (scope.getSession() == session) {
          return scope;
        }
      }
    }
    return null;
  }

  /**
   * Gets the scope of a JPro instance.
   *
   * @param instanceID the JPro instance id
   * @return the scope, null if there is no logged-in user for that instance
   */
  public static UserScope get(String instanceID) {
    return instanceID == null ? null : SCOPES.get(instanceID);
  }

  /**
   * Unregisters the scope of a JPro instance.
   *
   * @param instanceID the JPro instance id
   * @return the removed scope, null if none
   */
  public static UserScope remove(String instanceID) {
    UserScope scope = instanceID == null ? null : SCOPES.remove(instanceID);
    if (scope != null) {
      WINDOWS.values().removeIf(s -> s == scope);
      LOGGER.info("web session {0} closed", instanceID);
    }
    return scope;
  }

  /**
   * Binds a window to a scope explicitly.
   * <p>
   * Necessary for the primary stage only, which is already showing when its user logs in.
   * All other windows are resolved on demand, see {@link #of(Window)}.
   *
   * @param window the window
   * @param scope the scope
   */
  public static void bindWindow(Window window, UserScope scope) {
    WINDOWS.put(Objects.requireNonNull(window, "window"), Objects.requireNonNull(scope, "scope"));
  }

  /**
   * Determines the scope a window belongs to.
   * <p>
   * Tries, in that order: an explicit binding, the JPro instance the window is registered with,
   * and the window's owner. The result is cached, but never a negative one.
   *
   * @param window the window, null if unknown
   * @return the scope, null if the window does not belong to a logged-in user
   */
  public static UserScope of(Window window) {
    if (window == null) {
      return null;
    }
    UserScope scope = WINDOWS.get(window);
    if (scope == null) {
      scope = get(instanceIdOf(window));
      if (scope == null) {
        scope = of(ownerOf(window));
      }
      if (scope != null) {
        WINDOWS.put(window, scope);
      }
    }
    return scope;
  }

  /**
   * Determines the scope a node belongs to.
   *
   * @param node the node, null if unknown
   * @return the scope, null if the node does not belong to a logged-in user
   */
  public static UserScope of(Node node) {
    Scene scene = node == null ? null : node.getScene();
    return scene == null ? null : of(scene.getWindow());
  }

  /**
   * Gets the JPro instance id a window belongs to.
   *
   * @param window the window
   * @return the instance id, null if the window is unknown to JPro
   */
  public static String instanceIdOf(Window window) {
    try {
      WebAPI webAPI = WebAPI.getWebAPI(window);
      return webAPI == null ? null : webAPI.getInstanceID();
    }
    catch (RuntimeException rx) {
      // not a JPro-managed window (popups, context menus, tooltips): resolve via the owner
      return null;
    }
  }

  /**
   * Gets the owner of a window.
   * <p>
   * {@link Window} itself has no owner. Only {@link Stage} and {@link PopupWindow} have,
   * and both set it before the window is shown.
   *
   * @param window the window
   * @return the owner, null if none
   */
  public static Window ownerOf(Window window) {
    return switch (window) {
      case Stage stage -> stage.getOwner();
      case PopupWindow popup -> popup.getOwnerWindow();
      case null, default -> null;
    };
  }


  private final WebAPI webAPI;
  private final String instanceID;
  private final SessionInfo sessionInfo;
  private final Session session;
  private final Locale locale;

  private UserScope(WebAPI webAPI, SessionInfo sessionInfo, Session session, Locale locale) {
    this.webAPI = Objects.requireNonNull(webAPI, "webAPI");
    this.instanceID = Objects.requireNonNull(webAPI.getInstanceID(), "instanceID");
    this.sessionInfo = Objects.requireNonNull(sessionInfo, "sessionInfo");
    this.session = Objects.requireNonNull(session, "session");
    this.locale = Objects.requireNonNull(locale, "locale");
  }

  /**
   * Gets the web API of this browser session.
   *
   * @return the web API
   */
  public WebAPI getWebAPI() {
    return webAPI;
  }

  /**
   * Gets the JPro instance id, i.e. the web session key.
   *
   * @return the instance id
   */
  public String getInstanceID() {
    return instanceID;
  }

  /**
   * Gets the session info of the logged-in user.
   *
   * @return the session info
   */
  public SessionInfo getSessionInfo() {
    return sessionInfo;
  }

  /**
   * Gets the upstream session held for the lifetime of the JPro instance.
   *
   * @return the session
   */
  public Session getSession() {
    return session;
  }

  /**
   * Gets the effective locale of the browser.
   *
   * @return the locale
   */
  public Locale getLocale() {
    return locale;
  }

  @Override
  public String toString() {
    return instanceID + "/" + sessionInfo.getUserName() + "/" + locale;
  }

}
