/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.gui.about;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Popup;

import org.tentackle.fx.AbstractFxController;
import org.tentackle.fx.Fx;
import org.tentackle.fx.FxControllerService;
import org.tentackle.fx.FxFactory;
import org.tentackle.fx.FxUtilities;
import org.tentackle.fx.NotificationBuilder;
import org.tentackle.fx.component.FxLabel;

/**
 * Controller for showing an about popup.
 */
@FxControllerService(css = FxControllerService.RESOURCES_NONE)
public class AboutView extends AbstractFxController {

  /**
   * Shows the about popup.
   *
   * @param owner the popup owner
   */
  public static void show(Object owner) {
    Popup popup = new Popup();
    Parent notification =
      FxFactory.getInstance()
               .createNotificationBuilder()
               .type(NotificationBuilder.Type.NONE)
               .content(Fx.load(AboutView.class).getView())
               .duration(5000)
               .fadeOut(1500)
               .hide(popup::hide)
               .css(AboutView.class.getResource(FxUtilities.getInstance().isDarkMode() ? "AboutView-dark.css" : "AboutView.css"))
               .build();
    FxUtilities.getInstance().showNotification(owner, popup, notification, null);
  }



  @FXML
  private FxLabel applicationVersion;

  @FXML
  private FxLabel tentackleVersion;

  @FXML
  private FxLabel javaVersion;

  @FXML
  private void initialize() {
    applicationVersion.setText(com.example.tracker.common.Version.RELEASE + " (" + com.example.tracker.common.Version.DATE + ")");
    tentackleVersion.setText(org.tentackle.common.Version.RELEASE + " (" + org.tentackle.common.Version.DATE + ")");
    javaVersion.setText(Runtime.version().toString());
  }
}
