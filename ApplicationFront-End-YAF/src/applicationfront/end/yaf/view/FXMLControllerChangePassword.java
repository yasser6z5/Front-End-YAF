package applicationfront.end.yaf.view;

import applicationfront.end.yaf.client.UserRESTClient;
import applicationfront.end.yaf.model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
* Controller class for Change Password view (WP3)
* @author Adam 
*/
public class FXMLControllerChangePassword implements Initializable {

@FXML
private PasswordField currentPasswordField;

@FXML
private PasswordField newPasswordField;

@FXML
private PasswordField confirmNewPasswordField;

@FXML
private ProgressBar passwordStrengthBar;

@FXML
private Label passwordStrengthLabel;

private User currentUser;

@Override
public void initialize(URL url, ResourceBundle rb) {
// Real-time password strength indicator
newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
updatePasswordStrength(newValue);
});
}

// Crucial method: receives the logged-in user from MainWindow
public void initData(User user) {
this.currentUser = user;
}

private void updatePasswordStrength(String password) {
if (password == null || password.isEmpty()) {
passwordStrengthBar.setVisible(false);
passwordStrengthLabel.setVisible(false);
return;
}

passwordStrengthBar.setVisible(true);
passwordStrengthLabel.setVisible(true);

int score = 0;
if (password.length() >= 8) score++;
if (password.matches(".*[A-Z].*")) score++;
if (password.matches(".*[0-9].*")) score++;
if (password.matches(".*[^a-zA-Z0-9].*")) score++;

switch (score) {
case 0:
case 1:
passwordStrengthBar.setProgress(0.25);
passwordStrengthBar.setStyle("-fx-accent: #dc2626;");
passwordStrengthLabel.setText("Weak");
passwordStrengthLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
break;
case 2:
passwordStrengthBar.setProgress(0.50);
passwordStrengthBar.setStyle("-fx-accent: #f59e0b;");
passwordStrengthLabel.setText("Fair");
passwordStrengthLabel.setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");
break;
case 3:
passwordStrengthBar.setProgress(0.75);
passwordStrengthBar.setStyle("-fx-accent: #2563eb;");
passwordStrengthLabel.setText("Good");
passwordStrengthLabel.setStyle("-fx-text-fill: #2563eb; -fx-font-weight: bold;");
break;
case 4:
passwordStrengthBar.setProgress(1.0);
passwordStrengthBar.setStyle("-fx-accent: #16a34a;");
passwordStrengthLabel.setText("Strong");
passwordStrengthLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
break;
}
}

@FXML
private void handleSaveAction(ActionEvent event) {
String currentPass = currentPasswordField.getText();
String newPass = newPasswordField.getText();
String confirmPass = confirmNewPasswordField.getText();

// 1. Validate empty fields
if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
showAlert("All fields are required.", AlertType.ERROR);
return;
}

// 2. Validate that the current password matches
if (currentUser != null && currentUser.getPassword() != null && !currentPass.equals(currentUser.getPassword())) {
showAlert("The current password entered is incorrect.", AlertType.ERROR);
return;
}

// 3. Validate minimum password length
if (newPass.length() < 8) {
showAlert("The new password must be at least 8 characters long.", AlertType.ERROR);
return;
}

// 4. Validate matching passwords
if (!newPass.equals(confirmPass)) {
showAlert("The new passwords do not match.", AlertType.ERROR);
return;
}

// 5. Send update to BackEnd
try {
if (currentUser == null || currentUser.getId() == null) {
showAlert("User session data is incomplete (missing ID). Please sign in again.", AlertType.ERROR);
return;
}

currentUser.setPassword(newPass);

UserRESTClient client = new UserRESTClient();
client.edit_XML(currentUser, currentUser.getId().toString());
client.close();

showAlert("Password updated successfully!", AlertType.INFORMATION);
closeWindow(event);

} catch (Exception ex) {
showAlert("Error connecting to the server: " + ex.getMessage(), AlertType.ERROR);
ex.printStackTrace();
}
}

@FXML
private void handleCancel(ActionEvent event) {
closeWindow(event);
}

private void closeWindow(ActionEvent event) {
Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
stage.close();
}

private void showAlert(String message, AlertType alertType) {
Alert alert = new Alert(alertType, message, ButtonType.OK);
alert.showAndWait();
}
}