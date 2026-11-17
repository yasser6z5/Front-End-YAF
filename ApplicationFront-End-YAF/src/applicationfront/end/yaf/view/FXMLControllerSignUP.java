/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package applicationfront.end.yaf.view;

import applicationfront.end.yaf.client.UserRESTClient;
import applicationfront.end.yaf.model.User;
import jakarta.validation.Path.Node;
import jakarta.ws.rs.WebApplicationException;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.Action;

/**
 * this is my controller view of Sign Up
 * @author Godsfavor
 */
public class FXMLControllerSignUP {
    
     private static final Logger logger = Logger.getLogger("applicationfront.end.yaf.view");
    
    @FXML 
    private TextField firstNameField;
    @FXML 
    private TextField middleIntialField;
    @FXML 
    private TextField lastNameField;
    @FXML 
    private TextField emailField;
    @FXML 
    private TextField streetField;
    @FXML 
    private TextField cityField;
    @FXML 
    private TextField stateField;
    @FXML 
    private TextField zipField;
    @FXML 
    private TextField phoneField;
    @FXML 
    private PasswordField passwordField;
    @FXML 
    private PasswordField confirmPasswordField;
    
    private Stage stage;

    /**
     * Sets the stage associated with this view.
     *
     * @param stage The primary {@link Stage} for the Sign In window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Initialization method.
     * @todo Add Actionevent handler handleSignUp to btnSignUp using setOnAction       
     *       convenience method and a method reference. Remember to assign fx:id
     *       value to the button in the FXML and define an FXML annotated attribute 
     *       for it in this controller class.
     * @param url
     * @param rb 
     */
    @FXML
    public void initialize() {
        logger.info("Initializing Sign Up controller.");
        btnSignUp.setOnAction(ActionEvent.ACTION((this::handleSignUp));  
    }    
   //ENVIAR PETICION HTTP CON METODO GET ALL
    //backend URL= http://localhost:8080/WebYAFapplication/webresources
    /**
     * Sign Up Action handler.
     * @todo Mostrar por consola en el tratamiento de excepciones una traza 
     *       de log tipo warning con el mensaje que se está mostrando al usuario en la ventana.  
     * @param event 
     */
    public void handleSignUp(ActionEvent event){
        //Lectura de campos
        
        try{
            String firstName = firstNameField.getText();
            String middleInitial = middleIntialField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String street = streetField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            Integer zip = zipField.getText().isEmpty() ? null : Integer.valueOf(zipField.getText().trim());
            Integer phone = phoneField.getText().isEmpty() ? null : Integer.valueOf(phoneField.getText().trim());
            
            // 2. Validaciones locales
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ){
                throw new Exception("Please fill in all required fields.");
            }
            if (!password.equals(confirmPassword)) {
            throw new Exception( "Passwords do not match.");
            
            }
            if (password.length() < 8) {
            throw new Exception( "Password must be at least 8 characters long.");
            
            }
            //CONEXIÓN CON EL BACKEND
            //// Instanciar el objeto entidad/modelo con los datos del formulario
            User user = new User();
            //GENERAR EL ID ALEATORIAMNETE 
            Long id = 0l;
            while (id<=0)
                id = new Random().nextLong();
            user.setId(id);
            user.setFirstName(firstName);
            user.setMiddleInitial(middleInitial);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setStreet(street);
            user.setCity(city);
            user.setState(state);
            user.setZip(zip);
            user.setPhone(phone);
            user.setPassword(password);
            
            UserRESTClient client = new UserRESTClient();
            client.create_XML(user);
         logger.info("User registered successfully.");
        showAlert("User registered successfully!", AlertType.INFORMATION);
            clearFields();
        }catch (WebApplicationException e) {   
             logger.warning(e.getMessage());
            showAlert("User gmail already exists!!", Alert.AlertType.ERROR);  
        } catch (NumberFormatException e) {
             logger.severe(e.getMessage());
            showAlert("The phone number or ZIP code format is invalid .", Alert.AlertType.ERROR);
        }catch (Exception e) {
             logger.severe(e.getMessage());
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
        
    }
   
    /**
     * Cancel action handler.
     * @param event 
     */
   @FXML
    void handleCancel(ActionEvent event) {
        clearFields();
    }
    /**
     * Go to sign in action handler.
     * @todo Cerrar el currentStage en este método para volver a Sign In.
     * @param event 
     */
    @FXML
    void goToSignIn(ActionEvent event)
    {
        try {
            
            
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
                        Parent root = loader.load();

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                        showAlert("Error loading Sign In view.", Alert.AlertType.ERROR);
                    }

    }
//
    private void showAlert(String error, AlertType alertType) {
        //Shows error dialog.
        Alert alert=new Alert(alertType,
                              error,
                              ButtonType.OK);
        
        alert.showAndWait();
        
    }

    private void clearFields() {
        firstNameField.clear();
        middleIntialField.clear();
        lastNameField.clear();
        emailField.clear();
        streetField.clear();
        cityField.clear();
        stateField.clear();
        zipField.clear();
        phoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private static class btnSignUp {

        public btnSignUp() {
        }
    }

    private static class tfLogin {

        public tfLogin() {
        }
    }

    
    
    
}

