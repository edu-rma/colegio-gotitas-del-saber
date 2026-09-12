package main.java.edu.mmcoffee.colegiogotitas;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.util.SceneManager;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Validar la conexión a la base de datos al arrancar
        try {
            DataBaseConnection.getConnectionDataBase();
            System.out.println("CONECTADO A LA BASE DE DATOS!");
        } catch (Exception e) { // Cambiado a Exception para coincidir con DataBaseConnection
            System.err.println("ERROR EN LA CONEXION: " + e.getMessage());
        }

        try {
            SceneManager sceneManager = new SceneManager(primaryStage);
            sceneManager.showLoginView();
            primaryStage.setTitle("Colegio Gotitas del Saber");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}