package main.java.edu.mmcoffee.colegiogotitas.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import main.java.edu.mmcoffee.colegiogotitas.controller.DashboardController;
import main.java.edu.mmcoffee.colegiogotitas.controller.LoginController;
import main.java.edu.mmcoffee.colegiogotitas.controller.RegistroController;
import main.java.edu.mmcoffee.colegiogotitas.repository.AuthRepository;
import main.java.edu.mmcoffee.colegiogotitas.repository.EstudianteRepository;
import main.java.edu.mmcoffee.colegiogotitas.service.AuthService;
import main.java.edu.mmcoffee.colegiogotitas.service.DashboardService;

public class SceneManager {

    private Stage primaryStage;
    private final String FXML_PATH = "/main/resources/view/";

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // ==========================================================
    // LOGIN
    // ==========================================================

    public void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXML_PATH + "login-view.fxml")
        );

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
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXML_PATH + "registro-view.fxml")
        );

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
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXML_PATH + "dashboard-view.fxml")
        );

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
        /*
         * Se mantiene 950 x 600 porque el Dashboard necesita
         * espacio para mostrar la tabla y las operaciones del CRUD.
         */
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // ==========================================================
    // ALERTA
    // ==========================================================

    public void showInfoAlert(
            String head,
            String title,
            String content,
            AlertType type) {

        Alert alert = new Alert(type);
        alert.initOwner(this.primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.showAndWait();
    }
}