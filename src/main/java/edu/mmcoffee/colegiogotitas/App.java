package main.java.edu.mmcoffee.colegiogotitas;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.util.SceneManager;
import java.sql.SQLException;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage)throws Exception {
        this.primaryStage = primaryStage;
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginView();
        primaryStage.show();
    }

  public static void main(String[] args) throws Exception {
        launch();
        try{
            DataBaseConnection.getConnectionDataBase();
            System.out.println("CONECTADO!");
        }catch(SQLException e){
            System.out.println("ERROR EN LA CONEXION");
        }
    }
 
}