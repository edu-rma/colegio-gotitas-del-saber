package main.java.edu.mmcoffee.colegiogotitas.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.edu.mmcoffee.colegiogotitas.controller.DashboarController;
import main.java.edu.mmcoffee.colegiogotitas.controller.LoginController;
import main.java.edu.mmcoffee.colegiogotitas.controller.RegisterController;
import main.java.edu.mmcoffee.colegiogotitas.repository.AuthRepository;
import main.java.edu.mmcoffee.colegiogotitas.repository.EstudianteRepository;
import main.java.edu.mmcoffee.colegiogotitas.service.AuthService;
import main.java.edu.mmcoffee.colegiogotitas.service.DashboarService;

public class SceneManager {

  private Stage primaryStage;
    private final String FXML_PATH = "/main/resources/view/";
    public SceneManager(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    public void showLoginView()throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + "login-view.fxml"));
 
    loader.setControllerFactory(
    clazz -> { 
        if(clazz == LoginController.class){
            AuthRepository authRepository = new AuthRepository();
            AuthService authService = new AuthService(authRepository);
            return new LoginController(authService, this);
        }
        try{ 
            return clazz.getDeclaredConstructor().newInstance();
        }catch(Exception e){
            throw new RuntimeException("Error al crear el constructor" + e.getMessage());
        }
    });
    Parent root = loader.load();
    Scene scene = new Scene(root, 600, 600);
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
            }
    public void showRegisterView()throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + "register-view.fxml"));
 
        loader.setControllerFactory(clazz -> {
            if(clazz == RegisterController.class){
                AuthRepository authRepository = new AuthRepository();
                AuthService authService = new AuthService(authRepository);
                return new RegisterController(authService, this);
            }
            try{
                return clazz.getDeclaredConstructor().newInstance();
            }catch(Exception e){
                throw new RuntimeException("Error al crear el constructor" + e.getMessage());
            }
        });
 
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

   public void showDashBoardView()throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH+"dashboard-view.fxml"));
        loader.setControllerFactory(clazz -> {
            if(clazz == DashboarController.class){
                EstudianteRepository dashBoardRepository = new EstudianteRepository();
                DashboarService dasboardService = new DashboarService(dashBoardRepository);
               return new DashboarController(dasboardService, this);
            }
            try{
                return clazz.getDeclaredConstructor().newInstance();
            }catch(Exception e){
                throw new RuntimeException("error al crear el constructor" + e.getMessage());
            }
        }
        );
        Parent root = loader.load();
        Scene scene = new Scene(root , 600, 400);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showInfoAlert(String head, String title, String content, AlertType type){
        Alert alert = new Alert(type);
        alert.initOwner(this.primaryStage);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(head);
        alert.showAndWait();
    }
 
}