package main.java.edu.mmcoffee.colegiogotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.java.edu.mmcoffee.colegiogotitas.service.AuthService;
import main.java.edu.mmcoffee.colegiogotitas.util.SceneManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.LoginRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.LoginResponse;
import javafx.scene.control.PasswordField;

public class LoginController implements Initializable {
    
  // atributos
    private final AuthService authService;
    private final SceneManager sceneManager;
    @FXML
    private TextField txtFieldEmail;
    @FXML
    private PasswordField txtFieldPass; // Cambiado a passwordField
    
   public LoginController(AuthService authService, SceneManager sceneManager){
       this.authService = authService;
       this.sceneManager = sceneManager;
   } 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("TODO LO QUE ESTE ACA, SE EJECUTA CUANDO SE MUESTRA LA VISTA");
    }    

    public void handleLogin()throws Exception{
        if(txtFieldEmail.getText().isEmpty()||txtFieldPass.getText().isEmpty()){
        sceneManager.showInfoAlert("campos faltantes",
                    "Revisar información ",
                    "uno o mas campos estan vacions", 
                    Alert.AlertType.WARNING);
        }else{
            try{
            LoginResponse responseService = authService.login(new LoginRequest(txtFieldEmail.getText(),txtFieldPass.getText()));
            LoginResponse userLogged = new LoginResponse(responseService.getNombre(),responseService.getApellido()); 
            sceneManager.showInfoAlert("bienvenido a Gotitas del saber", "inicio exitoso", "bienvenido :"+ userLogged.getNombre(), Alert.AlertType.INFORMATION);
            sceneManager.showDashBoardView();
        }catch(RuntimeException e){
            sceneManager.showInfoAlert("datos incorrectos", "Revisa tu informacion", "intenta de nuevo", Alert.AlertType.INFORMATION);
        }
        }
    }
 
    public void handleGoToRegister()throws Exception{
        sceneManager.showRegisterView();
    }
}