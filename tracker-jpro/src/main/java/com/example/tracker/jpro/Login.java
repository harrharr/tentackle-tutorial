/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.example.tracker.gui.main.MainController;
import com.example.tracker.jpro.session.FxSessionBinder;
import com.example.tracker.jpro.session.UserScope;
import com.jpro.webapi.JProApplication;
import com.jpro.webapi.WebAPI;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.tentackle.common.LocaleProvider;
import org.tentackle.fx.AbstractFxController;
import org.tentackle.fx.Fx;
import org.tentackle.fx.FxControllerService;
import org.tentackle.fx.component.FxButton;
import org.tentackle.fx.component.FxPasswordField;
import org.tentackle.fx.component.FxTextField;
import org.tentackle.log.Logger;
import org.tentackle.session.Session;
import org.tentackle.session.SessionInfo;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The login controller of the web client.
 * <p>
 * This is where a browser session becomes a Tentackle web session: the credentials are turned into
 * a {@link SessionInfo}, the JPro instance id becomes the web session key, and the resulting
 * {@link UserScope} is what {@link FxSessionBinder} binds to the FX thread from then on.
 * <p>
 * {@code org.tentackle.fx.rdc.login.Login} cannot be reused because it casts the application to a
 * {@code DesktopApplication}.
 * <p>
 * Authentication runs in a background thread on purpose: the FX thread is shared by all browser
 * sessions of this JVM, so a slow login must not freeze everybody else's UI.
 */
@FxControllerService
public class Login extends AbstractFxController {

  private static final Logger LOGGER = Logger.get();

  private JProApplication jproApplication;

  @FXML
  private ResourceBundle resources;

  @FXML
  private GridPane loginPane;

  @FXML
  private FxTextField usernameField;

  @FXML
  private FxPasswordField passwordField;

  @FXML
  private Label messageLabel;

  @FXML
  private ProgressBar progressBar;

  @FXML
  private FxButton loginButton;


  /**
   * Creates the login controller.
   */
  public Login() {
    // see -Xlint:missing-explicit-ctor since Java 16
  }

  @FXML
  private void initialize() {
    progressBar.setVisible(false);
    loginButton.setGraphic(Fx.createGraphic("login"));
    loginButton.setOnAction(_ -> login());
    usernameField.setAutoSelect(true);
    passwordField.setAutoSelect(true);
    passwordField.setOnAction(_ -> login());
  }

  /**
   * Sets the JPro application this login belongs to, i.e. the browser session.
   *
   * @param jproApplication the JPro application
   */
  public void setJProApplication(JProApplication jproApplication) {
    this.jproApplication = jproApplication;
  }

  /**
   * Sets the message shown below the login pane.
   *
   * @param message the message, null to clear
   */
  public void setMessage(String message) {
    messageLabel.setText(message);
  }

  /**
   * Sets the progress.
   *
   * @param progress &le; 0 if login has not begun yet, &ge; 1 if completed
   */
  public void setProgress(double progress) {
    progressBar.setProgress(Math.max(progress, 0.0));
    boolean busy = progress > 0.0;
    loginButton.setDisable(busy);
    usernameField.setChangeable(!busy);
    passwordField.setChangeable(!busy);
    loginPane.setVisible(progress < 1.0);
    progressBar.setVisible(busy && progress < 1.0);
  }

  /**
   * Performs the login and replaces the login pane by the main view.
   */
  public void login() {
    if (progressBar.getProgress() > 0.0) {
      LOGGER.warning("login already in progress");
      return;
    }

    setProgress(0.1);
    setMessage(null);

    TrackerWebApplication application = TrackerJProApplication.getWebApplication();
    WebAPI webAPI = jproApplication.getWebAPI();
    String instanceID = webAPI.getInstanceID();
    Locale locale = LocaleProvider.getInstance().getEffectiveLocale(webAPI.getLocale());

    // getPassword() blanks the component's character buffer
    SessionInfo sessionInfo = application.createSessionInfo(usernameField.getText(), passwordField.getPassword(), null);
    sessionInfo.setLocale(locale);
    application.addWebSession(instanceID, sessionInfo);

    Task<Session> task = new Task<>() {
      @Override
      protected Session call() {
        // authenticates against the middle tier and takes a session out of the multi-user pool
        Session session = application.getSession(instanceID);
        if (session == null) {
          throw new IllegalStateException("no session for web session key " + instanceID);
        }
        return session;
      }
    };
    task.setOnSucceeded(_ -> loginSucceeded(instanceID, sessionInfo, task.getValue(), locale, webAPI));
    task.setOnFailed(_ -> loginFailed(instanceID, sessionInfo, task.getException()));

    Thread thread = new Thread(task, "login-" + instanceID);
    thread.setDaemon(true);
    thread.start();
  }


  /**
   * Creates the user's scope and switches over to the main view.
   */
  private void loginSucceeded(String instanceID, SessionInfo sessionInfo, Session session, Locale locale, WebAPI webAPI) {
    TrackerWebApplication application = TrackerJProApplication.getWebApplication();
    try {
      UserScope scope = UserScope.create(webAPI, sessionInfo, session, locale);
      webAPI.addInstanceCloseListener(() -> {
        UserScope.remove(instanceID);
        application.removeWebSession(instanceID);
        application.putSession(session);
      });

      Stage stage = getStage();
      UserScope.bindWindow(stage, scope);   // the primary stage is showing already

      // everything from here on belongs to this user and needs its session bound to the FX thread
      FxSessionBinder.run(scope, () -> {
        MainController controller = Fx.load(MainController.class);
        stage.setScene(Fx.createScene(controller.getView()));
      });
      setProgress(1.0);
    }
    catch (RuntimeException rx) {
      UserScope.remove(instanceID);
      application.putSession(session);
      loginFailed(instanceID, sessionInfo, rx);
    }
  }

  /**
   * Releases everything the failed attempt allocated and lets the user retry.
   */
  private void loginFailed(String instanceID, SessionInfo sessionInfo, Throwable cause) {
    LOGGER.warning("login failed for " + sessionInfo, cause);
    TrackerWebApplication application = TrackerJProApplication.getWebApplication();
    application.removeWebSession(instanceID);
    setProgress(0.0);
    setMessage(cause == null ? resources.getString("login failed")
                             : cause.getLocalizedMessage());
    passwordField.setPassword(null);
  }

}
