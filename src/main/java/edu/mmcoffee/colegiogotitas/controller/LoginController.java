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
    private PasswordField txtFieldPass;
    
    public LoginController(AuthService authService, SceneManager sceneManager){
        this.authService = authService;
        this.sceneManager = sceneManager;
    } 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Vista de login inicializada");
    }    

    public void handleLogin() throws Exception {
        // 1. Validar si hay campos vacíos
        if (txtFieldEmail.getText().trim().isEmpty() || txtFieldPass.getText().trim().isEmpty()) {
            sceneManager.showInfoAlert(
                "Campos faltantes",
                "Revisar información",
                "Uno o más campos están vacíos", 
                Alert.AlertType.WARNING
            );
            return;
        }

        LoginResponse responseService = null;

        // 2. Intentar autenticar credenciales únicamente en este bloque
        try {
            LoginRequest request = new LoginRequest(txtFieldEmail.getText().trim(), txtFieldPass.getText());
            responseService = authService.login(request);

            if (responseService == null) {
                sceneManager.showInfoAlert("Datos incorrectos", "Revisa tu información", "Credenciales inválidas", Alert.AlertType.ERROR);
                return;
            }
        } catch (RuntimeException e) {
            sceneManager.showInfoAlert(
                "Datos incorrectos", 
                "Revisa tu información", 
                "Intenta de nuevo", 
                Alert.AlertType.ERROR
            );
            return; // Evita continuar si ocurrió un error en la BD o en las credenciales
        }

        // 3. Si el login fue exitoso, mostrar mensaje de bienvenida
        sceneManager.showInfoAlert(
            "Bienvenido a Gotitas del Saber", 
            "Inicio exitoso", 
            "Bienvenido: " + responseService.getNombre(), 
            Alert.AlertType.INFORMATION
        );

        // 4. Cargar el Dashboard en un bloque independiente para capturar errores de vista
        try {
            sceneManager.showDashBoardView();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime la traza exacta del error FXML en la consola del IDE
            sceneManager.showInfoAlert(
                "Error de interfaz", 
                "No se pudo cargar el Dashboard", 
                "Ocurrió un problema al abrir la pantalla principal: " + e.getMessage(), 
                Alert.AlertType.ERROR
            );
        }
    }
 
    public void handleGoToRegister() throws Exception {
        sceneManager.showRegisterView();
    }
}