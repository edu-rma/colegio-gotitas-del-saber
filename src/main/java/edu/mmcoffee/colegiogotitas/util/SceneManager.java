package main.java.edu.mmcoffee.colegiogotitas.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.net.URL;
import main.java.edu.mmcoffee.colegiogotitas.controller.DashboardController;
import main.java.edu.mmcoffee.colegiogotitas.controller.LoginController;
import main.java.edu.mmcoffee.colegiogotitas.controller.RegistroController;
import main.java.edu.mmcoffee.colegiogotitas.repository.AuthRepository;
import main.java.edu.mmcoffee.colegiogotitas.repository.EstudianteRepository;
import main.java.edu.mmcoffee.colegiogotitas.service.AuthService;
import main.java.edu.mmcoffee.colegiogotitas.service.DashboardService;

public class SceneManager {

    private final Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // ==========================================================
    // LOGIN
    // ==========================================================

    public void showLoginView() throws Exception {
       
        URL fxmlUrl = Thread.currentThread().getContextClassLoader().getResource("main/resources/view/login-view.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException("No se pudo encontrar el archivo FXML en: view/login-view.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) {
                AuthRepository authRepository = new AuthRepository();
                AuthService authService = new AuthService(authRepository);
                return new LoginController(authService, this);
            }

            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage(), e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 640);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // ==========================================================
    // REGISTRO
    // ==========================================================

    public void showRegisterView() throws Exception {
        URL fxmlUrl = Thread.currentThread().getContextClassLoader().getResource("main/resources/view/registro-view.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException("No se pudo encontrar el archivo FXML en: view/registro-view.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        loader.setControllerFactory(clazz -> {
            if (clazz == RegistroController.class) {
                AuthRepository authRepository = new AuthRepository();
                AuthService authService = new AuthService(authRepository);
                return new RegistroController(authService, this);
            }

            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage(), e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 720);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // ==========================================================
    // DASHBOARD
    // ==========================================================

    public void showDashBoardView() throws Exception {
        URL fxmlUrl = Thread.currentThread().getContextClassLoader().getResource("main/resources/view/dashboard-view.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException("No se pudo encontrar el archivo FXML en: view/dashboard-view.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        loader.setControllerFactory(clazz -> {
            if (clazz == DashboardController.class) {
                EstudianteRepository dashboardRepository = new EstudianteRepository();
                DashboardService dashboardService = new DashboardService(dashboardRepository);
                return new DashboardController(dashboardService, this);
            }

            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage(), e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // ==========================================================
    // ALERTA
    // ==========================================================

    public void showInfoAlert(String head, String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(this.primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.showAndWait();
    }
}