/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.gui.password;

import com.example.tracker.gui.GuiBundle;
import com.example.tracker.pdo.md.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.tentackle.bind.Bindable;
import org.tentackle.common.StringHelper;
import org.tentackle.fx.AbstractFxController;
import org.tentackle.fx.Fx;
import org.tentackle.fx.FxControllerService;
import org.tentackle.fx.component.FxButton;
import org.tentackle.fx.component.FxPasswordField;
import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.DomainContextProvider;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for changing the user's password.
 *
 * @author harald
 */
@FxControllerService
public class ChangePasswordView extends AbstractFxController implements DomainContextProvider {

  /**
   * Shows the dialog and prompts the user to change his password.
   *
   * @param owner the owner window or node
   * @param user the user
   * @param admin invoked by admin user
   */
  public static void showDialog(Object owner, User user, boolean admin) {
    if (admin || user.isEditAllowed() || user.isPasswordChangeable()) {
      ChangePasswordView controller = Fx.load(ChangePasswordView.class);
      Stage stage = Fx.createStage(Modality.NONE);
      Scene scene = Fx.createScene(controller.getView());
      stage.setScene(scene);
      stage.setTitle(MessageFormat.format(GuiBundle.getString("password {0}"), user));
      controller.setUser(user, admin);
      Fx.show(stage);
    }
    else {
      Fx.info(owner, GuiBundle.getString("you're not allowed to change your password"));
    }
  }


  @Bindable
  private char[] oldPassword;
  @Bindable
  private char[] newPassword;
  @Bindable
  private char[] confirmedNewPassword;

  @FXML
  private FxPasswordField oldPasswordField;
  @FXML
  private FxPasswordField newPasswordField;
  @FXML
  private FxPasswordField confirmedNewPasswordField;
  @FXML
  private FxButton saveButton;
  @FXML
  private FxButton cancelButton;

  @FXML
  private ResourceBundle resources;

  private User user;
  private boolean admin;
  private String oldPasswordHash;

  @FXML
  private void initialize() {
    saveButton.setGraphic(Fx.createGraphic("save"));
    saveButton.setOnAction(e -> save());

    cancelButton.setGraphic(Fx.createGraphic("close"));
    cancelButton.setOnAction(e -> close());
  }

  private void setUser(User user, boolean admin) {
    this.user = user;
    this.admin = admin;
    this.oldPasswordHash = user.selectPasswordHash();
    oldPasswordField.setChangeable(!admin);
    getContainer().updateView();
  }

  @Override
  public DomainContext getDomainContext() {
    return user.getDomainContext();
  }

  private void save() {
    if (admin || Objects.equals(user.hash(oldPassword), oldPasswordHash)) {
      if (!Arrays.equals(newPassword, confirmedNewPassword)) {
        Fx.error(getView(), resources.getString("new passwords do not match"));
      }
      else {
        user = user.reload();
        if (newPassword == null) {
          Fx.yes(getView(), resources.getString("are you sure to disable the password check?"), false, () -> saveAndClose(null));
        }
        else {
          Fx.yes(getView(), resources.getString("save new password?"), false, () -> saveAndClose(user.hash(newPassword)));
        }
      }
    }
    else {
      Fx.error(getView(), resources.getString("wrong password"));
    }
  }

  private void saveAndClose(String hash) {
    user.updatePasswordHash(hash);
    close();
  }

  private void close() {
    StringHelper.blank(oldPassword);
    StringHelper.blank(newPassword);
    StringHelper.blank(confirmedNewPassword);
    getStage().close();
  }
}
