package applicationfront.end.yaf.view;

import applicationfront.end.yaf.client.UserRESTClient;
import applicationfront.end.yaf.model.User;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.GenericType;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for Sign In view (WP2)
 * @author Yasser
 */
public class FXMLControllerSignIn implements Initializable {

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private Button btnSignIn;

    @FXML
    private Button hlSignUp;

    @FXML
    private Label lblMessage;

    // Regular expression to validate email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Clear the error message when the user modifies the fields
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            lblMessage.setText("");
        });
        pwdPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            lblMessage.setText("");
        });
    }

    @FXML
    private void handleSignInAction(ActionEvent event) {
        String email = txtEmail.getText().trim();
        String password = pwdPassword.getText();

        // 1. Local Validations
        if (email.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter your email and password.");
            if (email.isEmpty()) txtEmail.requestFocus();
            else pwdPassword.requestFocus();
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            lblMessage.setText("Invalid email format.");
            txtEmail.requestFocus();
            return;
        }

        // 2. BackEnd Connection (Sign In)
        try {
            UserRESTClient client = new UserRESTClient();
            
            // Get all users 
            List<User> userList = client.findAll_XML(new GenericType<List<User>>() {});
            
            boolean loginSuccess = false;
            User loggedInUser = null;
            
            // Check if credentials match any user in the DB
            if (userList != null) {
                for (User u : userList) {
                    if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                        loginSuccess = true;
                        loggedInUser = u;
                        break;
                    }
                }
            }

            if (loginSuccess) {
                // Correct login: Save user and open MainWindow
                client.close();
                abrirMainWindow(event, loggedInUser);
            } else {
                lblMessage.setText("Incorrect credentials or unregistered user.");
            }

        } catch (WebApplicationException ex) {
            showAlert("Error connecting to the server. Is Glassfish running?", AlertType.ERROR);
        } catch (Exception ex) {
            showAlert("Unexpected error: " + ex.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleSignUpNavigation(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Load the Sign Up view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationfront/end/yaf/view/SignUP.FXML"));
            Parent root = loader.load();
            
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");
            signUpStage.setResizable(false);
            signUpStage.setScene(new Scene(root));
            signUpStage.show();
            // Sign in window is not closed
            //currentStage.close(); 
            
        } catch (IOException e) {
            showAlert("Could not load the Registration window.", AlertType.ERROR);
        }
    }
    
    private void abrirMainWindow(ActionEvent event, User user) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Load the Main Window view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationfront/end/yaf/view/MainWindow.fxml"));
            Parent root = loader.load();
            
            // (Optional) Pass user data to the Main Window controller
            FXMLControllerMainWindow mainController = loader.getController();
            mainController.initData(user);
            
            Stage mainStage = new Stage();
            mainStage.setTitle("Main Window - Application Dashboard");
            mainStage.setScene(new Scene(root, 800, 600));
            
            // Handle closing with the window's X button
            mainStage.setOnCloseRequest(e -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirm Exit");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to exit the application?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK) {
                    e.consume();
                }
            });

            mainStage.show();
            currentStage.close();
            
        } catch (IOException e) {
            showAlert("Could not load the Main Window.", AlertType.ERROR);
        }
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}