
package main.java.edu.mmcoffee.colegiogotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.RegisterRequest;
import main.java.edu.mmcoffee.colegiogotitas.service.AuthService;
import main.java.edu.mmcoffee.colegiogotitas.util.SceneManager;

public class RegisterController implements Initializable {

   private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML
    private TextField txtFieldNombre;
    @FXML
    private TextField txtFieldApellido;
    @FXML
    private TextField txtFieldEmail;
    @FXML
    private PasswordField txtFieldPassword;
    @FXML
    private PasswordField txtFieldConfirmPassword;

    public RegisterController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void handleRegister() {
        if (txtFieldNombre.getText().trim().isEmpty() || txtFieldApellido.getText().trim().isEmpty() ||
            txtFieldEmail.getText().trim().isEmpty() || txtFieldPassword.getText().trim().isEmpty() ||
            txtFieldConfirmPassword.getText().trim().isEmpty()) {

            sceneManager.showInfoAlert("Campos vacíos", "Revisar información", "Llena todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        if (!txtFieldPassword.getText().equals(txtFieldConfirmPassword.getText())) {
            sceneManager.showInfoAlert("Error", "Validación de contraseña", "Las contraseñas no coinciden.", Alert.AlertType.ERROR);
            return;
        }

        try {
            RegisterRequest request = new RegisterRequest(
                txtFieldNombre.getText().trim(),
                txtFieldApellido.getText().trim(),
                txtFieldEmail.getText().trim(),
                txtFieldPassword.getText()
            );

            authService.registerUser(request);

            sceneManager.showInfoAlert("Éxito", "Registro completado", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);
            sceneManager.showLoginView();

        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", "No se pudo registrar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleGoBackToLogin() throws Exception {
        sceneManager.showLoginView();
    }
}