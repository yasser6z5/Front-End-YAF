package applicationfront.end.yaf.view;

import applicationfront.end.yaf.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Controller class for Main Window (WP2)
 * @author Yasser
 */
public class FXMLControllerMainWindow implements Initializable {

    @FXML
    private MenuItem miChangePassword;

    @FXML
    private MenuItem miSignOut;
    
    @FXML
    private MenuItem miExit;

    @FXML
    private Label lblWelcome;
    
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización básica
    }    
    
    // Método para recibir los datos del usuario que se acaba de loguear
    public void initData(User user) {
        this.currentUser = user;
        if (user != null && user.getFirstName() != null) {
            lblWelcome.setText("Welcome, " + user.getFirstName() + "!");
        }
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        try {
            // Cargar la vista de Change Password (de Adam)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationfront/end/yaf/view/ChangePassword.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            
            // Pasar el usuario a la ventana de cambio de clave
            // FXMLControllerChangePassword controller = loader.getController();
            // controller.initData(currentUser);
            
            stage.show();
        } catch (IOException e) {
            showAlert("No se pudo cargar la vista de Cambio de Contraseña.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que quieres cerrar la sesión?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Cerrar la ventana actual usando el MenuItem como referencia
                // En JavaFX, los MenuItems no extienden de Node, hay que sacar la ventana así:
                Stage currentStage = (Stage) lblWelcome.getScene().getWindow();
                
                // Volver al Sign In
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/applicationfront/end/yaf/view/SignIn.fxml"));
                Parent root = loader.load();
                
                Stage signInStage = new Stage();
                signInStage.setTitle("Sign In");
                signInStage.setScene(new Scene(root));
                signInStage.setResizable(false);
                signInStage.show();
                
                currentStage.close();
            } catch (IOException e) {
                showAlert("Error al volver a la pantalla de Sign In.", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleExitAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas salir de la aplicación?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit(); // Cierra completamente la aplicación JavaFX
        }
    }
    
    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}