/*
 * Tracker JPro web client.
 */

package com.example.tracker.jpro;

import com.jpro.webapi.JProApplication;
import javafx.stage.Stage;

import org.tentackle.app.Application;
import org.tentackle.fx.Fx;
import org.tentackle.fx.FxFactory;

/**
 * The JPro entry point.
 * <p>
 * JPro creates one instance of this class per browser session, all of them sharing the single
 * JavaFX application thread and the single {@link TrackerWebApplication} of this JVM.
 * <p>
 * Do <em>not</em> start the web application from an instance initializer: it would run on every
 * instance construction and the second call would fail with
 * {@code register(): application already registered}.
 */
public class TrackerJProApplication extends JProApplication {

  /**
   * Gets the web application of this JVM.
   *
   * @return the web application, null if not started yet
   */
  public static TrackerWebApplication getWebApplication() {
    Application application = Application.getInstance();
    return application instanceof TrackerWebApplication webApplication ? webApplication : null;
  }


  @Override
  public void start(Stage stage) {
    TrackerJProBootstrap.ensureStarted();   // defensive: jpro.onFXStartup may not be configured

    Login controller = FxFactory.getInstance().createController(
            Login.class, null, null, TrackerJProApplication.class.getResource("Login.css"));
    controller.setJProApplication(this);
    stage.setTitle("Tracker");
    stage.setScene(Fx.createScene(controller.getView()));
    stage.show();     // not Fx.show(): the primary stage of an instance is not a JPro tab
  }

}
