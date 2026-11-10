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

    // Expresión regular para validar el email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Limpiar el mensaje de error cuando el usuario modifique los campos (Requisito de Diseño)
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

        // 1. Validaciones Locales
        if (email.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Por favor, introduce tu email y contraseña.");
            if (email.isEmpty()) txtEmail.requestFocus();
            else pwdPassword.requestFocus();
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            lblMessage.setText("Formato de correo electrónico no válido.");
            txtEmail.requestFocus();
            return;
        }

        // 2. Conexión con el BackEnd (Sign In)
        try {
            UserRESTClient client = new UserRESTClient();
            
            // Obtenemos todos los usuarios (según el método findAll_XML de vuestro RESTClient)
            List<User> userList = client.findAll_XML(new GenericType<List<User>>() {});
            
            boolean loginSuccess = false;
            User loggedInUser = null;
            
            // Comprobamos si las credenciales coinciden con algún usuario de la BD
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
                // Login correcto: Guardar usuario y abrir MainWindow
                client.close();
                abrirMainWindow(event, loggedInUser);
            } else {
                lblMessage.setText("Credenciales incorrectas o usuario no registrado.");
            }

        } catch (WebApplicationException ex) {
            showAlert("Error al conectar con el servidor. ¿Está Glassfish encendido?", AlertType.ERROR);
        } catch (Exception ex) {
            showAlert("Error inesperado: " + ex.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleSignUpNavigation(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Cargar la vista Sign Up de Favor
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationfront/end/yaf/view/SignUP.fxml"));
            Parent root = loader.load();
            
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");
            signUpStage.setResizable(false);
            signUpStage.setScene(new Scene(root));
            signUpStage.show();
            
            currentStage.close(); 
            
        } catch (IOException e) {
            showAlert("No se pudo cargar la ventana de Registro.", AlertType.ERROR);
        }
    }
    
    private void abrirMainWindow(ActionEvent event, User user) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Cargar la vista Main Window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationfront/end/yaf/view/MainWindow.fxml"));
            Parent root = loader.load();
            
            // (Opcional) Pasar los datos del usuario al controlador de la Main Window
            FXMLControllerMainWindow mainController = loader.getController();
            mainController.initData(user);
            
            Stage mainStage = new Stage();
            mainStage.setTitle("Main Window - Application Dashboard");
            mainStage.setScene(new Scene(root, 800, 600));
            
            // Controlar el cierre con la X de la ventana
            mainStage.setOnCloseRequest(e -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmar salida");
                alert.setHeaderText(null);
                alert.setContentText("¿Estás seguro de que deseas salir de la aplicación?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() != ButtonType.OK) {
                    e.consume();
                }
            });

            mainStage.show();
            currentStage.close();
            
        } catch (IOException e) {
            showAlert("No se pudo cargar la ventana Principal.", AlertType.ERROR);
        }
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}