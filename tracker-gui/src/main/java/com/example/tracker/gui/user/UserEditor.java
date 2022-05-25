/*
 * Tracker generated by tentackle-project-archetype.
 */

package com.example.tracker.gui.user;

import com.example.tracker.gui.password.ChangePasswordView;
import com.example.tracker.pdo.md.User;
import com.example.tracker.pdo.md.UserGroup;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.stage.Modality;

import org.tentackle.bind.Bindable;
import org.tentackle.fx.Fx;
import org.tentackle.fx.FxControllerService;
import org.tentackle.fx.component.FxButton;
import org.tentackle.fx.component.FxCheckBox;
import org.tentackle.fx.component.FxTableView;
import org.tentackle.fx.component.FxTextArea;
import org.tentackle.fx.component.FxTextField;
import org.tentackle.fx.rdc.PdoEditor;
import org.tentackle.fx.rdc.Rdc;
import org.tentackle.fx.rdc.table.TablePopup;
import org.tentackle.pdo.DomainContext;
import org.tentackle.pdo.Pdo;

import java.util.List;
import java.util.ResourceBundle;



/**
 * Editor for User.
 */
@FxControllerService
public class UserEditor extends PdoEditor<User> {

  @Bindable
  private User user;

  @FXML
  private FxTextField userNameField;

  @FXML
  private FxTextArea userCommentField;

  @FXML
  private FxButton passwordButton;

  @FXML
  private FxCheckBox userLoginAllowedField;

  @FXML
  private FxCheckBox userPasswordChangeableField;

  @FXML
  private FxCheckBox userChangingPreferencesAllowedField;

  @FXML
  private FxCheckBox userSystemPreferencesOnlyField;

  @FXML
  private FxTableView<UserGroup> userUserGroupsNode;

  @FXML
  private ResourceBundle resources;

  private TablePopup<UserGroup> popup;
  private Label emptyLabel;


  @FXML
  private void initialize() {
    userUserGroupsNode.setRowFactory(t -> {
      TableRow<UserGroup> row = new TableRow<>();
      row.setContextMenu(createContextMenu(row));
      return row;
    });
    emptyLabel = new Label(resources.getString("no groups"));
    userUserGroupsNode.setPlaceholder(emptyLabel);
    popup = Rdc.createTablePopup(userUserGroupsNode, "UserEditor", resources.getString("Groups"));
  }

  @Override
  public User getPdo() {
    return user;
  }

  @Override
  public void setPdo(User pdo) {
    user = pdo;
    getBinder().putBindingProperty(DomainContext.class, pdo.getDomainContext());
    emptyLabel.setContextMenu(createContextMenu(null));
  }

  @Override
  public void setChangeable(boolean changeable) {
    super.setChangeable(changeable);
    passwordButton.setDisable(!changeable || user.isNew() || !user.isEditAllowed());
  }

  @Override
  public void requestInitialFocus() {
    userNameField.requestFocus();
  }

  @Override
  public void configure() {
    popup.loadPreferences();
    passwordButton.setOnAction(event -> ChangePasswordView.showDialog(user, true));
  }


  @SuppressWarnings("unchecked")
  private ContextMenu createContextMenu(TableRow<UserGroup> row) {
    ContextMenu contextMenu = null;
    if (user != null && user.isEditAllowed()) {
      contextMenu = Fx.create(ContextMenu.class);
      MenuItem addMenuItem = Fx.create(MenuItem.class);
      addMenuItem.setText(resources.getString("add group to user"));
      addMenuItem.setOnAction(e -> Rdc.displaySearchStage(
          Pdo.create(UserGroup.class, getBinder().getBindingProperty(DomainContext.class)),
          Modality.APPLICATION_MODAL, getStage(), true, groups -> {
            if (!groups.isEmpty()) {
              UserGroup group = groups.get(0);
              if (!userUserGroupsNode.getItems().contains(group)) {
                ((List<UserGroup>) ((SortedList<UserGroup>) userUserGroupsNode.getItems()).getSource()).add(group);
                userUserGroupsNode.triggerViewModified();
              }
            }
          }));
      contextMenu.getItems().add(addMenuItem);

      if (row != null) {
        MenuItem removeMenuItem = Fx.create(MenuItem.class);
        removeMenuItem.setText(resources.getString("remove group from user"));
        removeMenuItem.disableProperty().bind(row.emptyProperty());
        removeMenuItem.setOnAction(e -> {
          ((SortedList<UserGroup>) userUserGroupsNode.getItems()).getSource().remove(row.getItem());
          userUserGroupsNode.triggerViewModified();
        });
        contextMenu.getItems().add(removeMenuItem);
      }
    }
    return contextMenu;
  }

}
