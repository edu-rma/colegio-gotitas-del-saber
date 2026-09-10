/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;
import main.java.edu.mmcoffee.colegiogotitas.service.DashboardService;
import main.java.edu.mmcoffee.colegiogotitas.util.SceneManager;


/**
* FXML Controller class
*
* @author anton
*/
public class DashboardController implements Initializable {
    private DashboardService dashboardService;
    private SceneManager sceneManager;
         @FXML
    private TableView<Estudiante> tvEstudiante;
    @FXML
    private TableColumn<Estudiante, String> fbcolumnid;
     @FXML
    private TableColumn<Estudiante, String> dbcolumnnombre;
     @FXML
    private TableColumn<Estudiante, String> dbapellidocolumn;
     @FXML
    private TableColumn<Estudiante, String> dbcorreocolumn;
     @FXML
    private TableColumn<Estudiante, String> dbseccioncolumn;
     @FXML
    private TableColumn<Estudiante, String> dbcursocolumn;
          @FXML
    private TableColumn<Estudiante, String> dbnombredcolumn;
               @FXML
    private TableColumn<Estudiante, String> dbapellidodcolumn;


 
    public DashboardController(DashboardService dashboarService, SceneManager sceneManager) {
        this.dashboardService = dashboarService;
        this.sceneManager = sceneManager;
    }
 
  
 
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            handleLoadTablestuden();
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", "No se pudo cargar la tabla",
                    e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
        }
    }    

    private void handleLoadTablestuden()throws Exception{
        fbcolumnid.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        dbcolumnnombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        dbapellidocolumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        dbcorreocolumn.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
        dbseccioncolumn.setCellValueFactory(new PropertyValueFactory<>("nombreSeccion"));
        dbcursocolumn.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        dbnombredcolumn.setCellValueFactory(new PropertyValueFactory<>("nombreDocente"));
        dbapellidodcolumn.setCellValueFactory(new PropertyValueFactory<>("apellidoDocente"));
        tvEstudiante.setItems(dashboardService.listStudent());
    }  
}
