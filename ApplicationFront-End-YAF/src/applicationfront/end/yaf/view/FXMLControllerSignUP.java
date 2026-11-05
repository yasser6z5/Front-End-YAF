/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package applicationfront.end.yaf.view;

import applicationfront.end.yaf.client.UserRESTClient;
import applicationfront.end.yaf.model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * this is my controller view of Sign Up
 * @author Godsfavor
 */
public class FXMLControllerSignUP implements Initializable {
    
   
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
   //ENVIAR PETICION HTTP CON METODO GET ALL
    //backend URL= http://localhost:8080/WebYAFapplication/webresources
    
   
    
   
    //Ejecutar el método handleSignUp().
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
                throw new Exception("Por favor, completa los campos obligatorios.");
            }
            if (!password.equals(confirmPassword)) {
            throw new Exception( "Las contraseñas no coinciden.");
            
            }
            if (password.length() < 8) {
            throw new Exception( "La contraseña debe tener al menos 8 caracteres.");
          
            }
            
            
            
            //CONEXIÓN CON EL BACKEND
            //// Instanciar el objeto entidad/modelo con los datos del formulario
            User user = new User();
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
        
        
        }catch (Exception e) {
            showAlert(e.getMessage(), AlertType.ERROR);
        } 
        
    }
    
 
   
 //Ejecutar el método handleCancel().
   @FXML
    void handleCancel(ActionEvent event) {
        clearFields();
    }
   // Ejecutar el método goToSignIn().
    @FXML
    void goToSignIn(ActionEvent event)
    {
      
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
    
    
    
}

