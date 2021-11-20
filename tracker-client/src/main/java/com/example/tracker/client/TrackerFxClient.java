/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.client;

import com.example.tracker.common.TrackerPreferences;
import com.example.tracker.common.Version;
import com.example.tracker.gui.GuiBundle;
import com.example.tracker.gui.main.MainController;
import com.example.tracker.pdo.md.User;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.tentackle.fx.FxUtilities;
import org.tentackle.fx.rdc.app.FxApplication;
import org.tentackle.fx.rdc.update.UpdatableDesktopApplication;
import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.Pdo;
import org.tentackle.prefs.PersistedPreferencesFactory;


/**
 * The FX client application.
 */
@SuppressWarnings("unchecked")    // due to getUser() below
public class TrackerFxClient extends UpdatableDesktopApplication<MainController> {

  /**
   * Creates the application.
   */
  public TrackerFxClient() {
    super("FX-Client", Version.RELEASE);
  }

  @Override
  public Class<? extends FxApplication> getApplicationClass() {
    return TrackerLoginApplication.class;
  }

  @Override
  public Class<MainController> getMainControllerClass() {
    return MainController.class;
  }

  @Override
  public void configureMainStage(Stage mainStage) {
    showApplicationStatus(GuiBundle.getString("initialize GUI..."), 0.9);
    super.configureMainStage(mainStage);
    mainStage.setTitle("Tracker");
    mainStage.setOnCloseRequest((WindowEvent ev) -> {
      ev.consume();
      getMainController().exit();
    });
  }

  @Override
  protected void configurePreferences() {
    super.configurePreferences();
    User user = getUser(getDomainContext());    // must exist!
    PersistedPreferencesFactory.getInstance().setSystemOnly(user.isSystemPreferencesOnly());
    PersistedPreferencesFactory.getInstance().setReadOnly(!user.isChangingPreferencesAllowed());
    TrackerPreferences.getInstance().getSystemPrefs().addPreferenceChangeListener(evt -> {
      switch (evt.getKey()) {
        case TrackerPreferences.HELP_URL:
          configureHelpURL();
          break;
        // other application specific pref changes go here...
      }
    });
    configureHelpURL();
  }

  @Override
  @SuppressWarnings("unchecked")
  public User getUser(DomainContext context, long userId) {
    return Pdo.create(User.class, context).selectCached(userId);
  }


  private void configureHelpURL() {
    FxUtilities.getInstance().setHelpURL(TrackerPreferences.getInstance().getHelpUrl());
  }

  /**
   * Starts the FX client application.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    new TrackerFxClient().start(args);
  }

}
