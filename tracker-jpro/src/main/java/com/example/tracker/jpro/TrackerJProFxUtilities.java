/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.jpro.session.FxSessionBinder;
import com.example.tracker.jpro.session.UserScope;
import com.jpro.webapi.JSVariable;
import com.jpro.webapi.WebAPI;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.tentackle.common.Service;
import org.tentackle.fx.FxRuntimeException;
import org.tentackle.fx.FxUtilities;
import org.tentackle.fx.rdc.RdcFxUtilities;
import org.tentackle.log.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

/**
 * FX utilities for the web client.
 * <p>
 * Replaces everything that talks to the desktop of the machine the JVM runs on — which in a web
 * application is the <em>server's</em> desktop, not the user's — by the corresponding
 * {@link WebAPI} operation, and closes the last gap the event-dispatcher hook leaves: the dialog
 * callbacks that Tentackle fires through a bare {@code Platform.runLater}.
 *
 * @see FxSessionBinder
 */
@Service(FxUtilities.class)
public class TrackerJProFxUtilities extends RdcFxUtilities {

  private static final Logger LOGGER = Logger.get();

  /** prefix of the browser window name a stage is opened under. */
  private static final String TAB_TARGET_PREFIX = "tracker-stage-";

  /** counter for unique window names. Must be unique per browser, so per JVM is more than enough. */
  private static final AtomicLong TAB_COUNTER = new AtomicLong();

  /** marker in the stage's property map that the tab is already tied to its window name. */
  private static final String CLOSE_TAB_KEY = TrackerJProFxUtilities.class.getName() + ".closeTab";

  /**
   * Creates the utilities.
   */
  public TrackerJProFxUtilities() {
    // see -Xlint:missing-explicit-ctor since Java 16
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to show the stage as a browser tab of the user it belongs to.
   * <p>
   * JPro turns such a stage into a JPro instance of its own ({@code "dynamic-stage"}), so
   * {@link WebAPI#getWebAPI(javafx.stage.Window)} on it answers with a <em>different</em> instance
   * id than the one the user logged in with. The explicit binding below is therefore not an
   * optimization but the only thing that keeps the stage attached to its user.
   * <p>
   * The tab is opened under a window name of our own so that it can be closed again later, see
   * {@link #closeTabWhenHidden(Stage, WebAPI, String)}.
   */
  @Override
  public void show(Stage stage) {
    UserScope scope = scopeOf(stage);
    if (scope == null) {
      super.show(stage);
    }
    else {
      if (stage.getOwner() != null) {
        // TrackerJProFxFactory creates stages without an owner because openStageAsTab rejects one.
        // Stages created elsewhere are fixed up here as long as they have not been shown yet.
        LOGGER.fine("dropping the owner of {0} to open it as a tab", stage);
        stage.initOwner(null);
      }
      // JPro takes the stage over instead of JavaFX showing it, so bind it up front
      UserScope.bindWindow(stage, scope);
      FxSessionBinder.attach(stage);

      WebAPI webAPI = scope.getWebAPI();
      String target = TAB_TARGET_PREFIX + TAB_COUNTER.incrementAndGet();
      webAPI.openStageAsTab(stage, target);   // runs window.open(<stage url>, target) in the user's page
      closeTabWhenHidden(stage, webAPI, target);
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to bind {@code onClose} to the owning user: Tentackle fires it through a bare
   * {@code Platform.runLater}, so it does not pass through any window's event dispatcher, yet the
   * answer of a {@code Fx.question} typically goes on to modify PDOs.
   */
  @Override
  public void showNotification(Object owner, Popup popup, Parent notification, Runnable onClose) {
    super.showNotification(owner, popup, notification, FxSessionBinder.wrap(scopeOf(owner), onClose));
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to open the help in a new browser tab instead of on the server's desktop.
   */
  @Override
  public void showHelp(Object owner) {
    WebAPI webAPI = webAPIOf(owner);
    if (webAPI == null) {
      super.showHelp(owner);
    }
    else {
      String url = getHelpURL();
      if (url != null && !url.isEmpty()) {
        webAPI.openURLAsTab(url);
      }
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to download the file to the user's browser instead of opening it on the server.
   */
  @Override
  public void edit(Object owner, File file) {
    WebAPI webAPI = webAPIOf(owner);
    if (webAPI == null) {
      super.edit(owner, file);
    }
    else {
      try {
        webAPI.downloadURL(file.toURI().toURL());
      }
      catch (MalformedURLException mux) {
        throw new FxRuntimeException("cannot download " + file, mux);
      }
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden to close <em>this</em> browser session only. The inherited implementation invokes
   * {@link javafx.application.Platform#exit()}, which would tear down every other user's session as
   * well, and latches a flag that makes it work exactly once per JVM.
   */
  @Override
  public void terminate() {
    UserScope scope = UserScope.current();
    if (scope == null) {
      LOGGER.warning("terminate() outside a bound unit of work -> ignored");
    }
    else {
      LOGGER.info("closing web session {0}", scope);
      scope.getWebAPI().closeInstance();
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * Overridden because the server has no browser. Applications reaching this are misconfigured.
   */
  @Override
  public void browse(URI uri) {
    throw new FxRuntimeException("cannot browse " + uri + " on the server, use showHelp(owner)");
  }


  /**
   * Closes the browser tab of a stage when the stage is hidden.
   * <p>
   * {@code stage.close()} — what every cancel button ends up calling — only hides the JavaFX stage.
   * JPro's client answers the vanishing instance with its reconnect overlay and leaves the tab
   * open, which from the user's seat is indistinguishable from a hung UI.
   * <p>
   * The tab cannot close itself on request: {@code Window.hide()} runs
   * {@code peer.setVisible(false)} — where JPro tears the tab's view down — before it fires
   * {@code WINDOW_HIDING} or {@code WINDOW_HIDDEN}, so a script submitted to the tab's own
   * {@link WebAPI} from any hook is queued on a connection that is already dead, silently.
   * <p>
   * So the tab is closed by the page that opened it, whose connection is unaffected: reopening the
   * named window with an empty URL hands the user's page the existing tab without navigating it,
   * and JPro keeps that handle alive in a JS variable for us.
   *
   * @param stage the stage, just opened as a tab
   * @param webAPI the web API of the user, <em>not</em> of the tab
   * @param target the window name the tab was opened under
   */
  protected void closeTabWhenHidden(Stage stage, WebAPI webAPI, String target) {
    if (stage.getProperties().putIfAbsent(CLOSE_TAB_KEY, Boolean.TRUE) == null) {
      JSVariable tab = webAPI.executeScriptWithVariable("window.open('', '" + target + "')");
      // addEventHandler, not setOnHidden: RdcUtilities#displaySearchStage owns the onHidden slot
      stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, _ -> {
        LOGGER.fine("closing browser tab {0}", target);
        webAPI.executeScript("if (" + tab.getName() + ") { " + tab.getName() + ".close(); }");
      });
    }
  }


  /**
   * Determines the scope an owner belongs to.
   *
   * @param owner a window, scene or node, null if unknown
   * @return the scope, null if it cannot be determined
   */
  protected UserScope scopeOf(Object owner) {
    if (owner == null) {
      return UserScope.current();
    }
    try {
      UserScope scope = UserScope.of(getWindow(owner));
      return scope == null ? UserScope.current() : scope;
    }
    catch (FxRuntimeException fx) {
      return UserScope.current();
    }
  }

  /**
   * Determines the web API an owner belongs to.
   *
   * @param owner a window, scene or node, null if unknown
   * @return the web API, null if it cannot be determined
   */
  protected WebAPI webAPIOf(Object owner) {
    UserScope scope = scopeOf(owner);
    return scope == null ? null : scope.getWebAPI();
  }

}
