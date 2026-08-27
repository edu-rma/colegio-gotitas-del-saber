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


public class LoginController implements Initializable {
    
    private final AuthService authService;
    private final SceneManager sceneManager;
@FXML
private TextField txtFieldEmail;

@FXML
private TextField txtFieldPass;
    
    public LoginController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("TOODO LO QUE ESTE ACA SE EJECUTA CUANDO SE MUESTRA LA VISTA");
        
    }    
    
    //metodos
   public void handleLogin()throws Exception{
     //verificar si los campos estan llenos
       if(txtFieldEmail.getText().isEmpty() || txtFieldPass.getText().isEmpty()){
       sceneManager.showInfoAlert("campos faltantes",
                    "Revisar información ",
                    "uno o mas campos estan vacios", 
                    Alert.AlertType.INFORMATION);
                 
    }else{
try{
LoginResponse responseService = authService.login(new LoginRequest (txtFieldEmail.getText(), txtFieldPass.getText ()));
LoginResponse userLogged = new LoginResponse (responseService.getNombre (), responseService.getApellido ());
sceneManager. showInfoAlert ("Bienvenido a Gotitas del Saber", "Inicio exitoso", "Bievenido: " + userLogged. getNombre (), Alert. AlertType. INFORMATION);
}catch (RuntimeException e) {
sceneManager. showInfoAlert ("Datos incorrectos", "Revisa tu informacion", "Intenta de nuevo", Alert. AlertType. INFORMATION) ;
           }  
      } 
       
    }  
}
