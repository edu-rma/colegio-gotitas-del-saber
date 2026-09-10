/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import main.java.edu.mmcoffee.colegiogotitas.model.Curso;
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

    @FXML
    private javafx.scene.control.Button btregistrar;
    @FXML
    private javafx.scene.control.Button bteditar;
    @FXML
    private javafx.scene.control.Button btactualizar;
    @FXML
    private javafx.scene.control.Button Btborrar;



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

    private void handleLoadTablestuden() throws Exception {
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

    @FXML
    private void handleRegistrar() {
        Optional<String[]> resultado = mostrarDialogoEstudiante("Nuevo estudiante", null, null, null, true);
        resultado.ifPresent(datos -> {
            try {
                // datos = [nombre, apellido, correo, idCurso]
                dashboardService.crearEstudiante(datos[0], datos[1], datos[2], datos[3]);
                handleLoadTablestuden();
            } catch (Exception e) {
                sceneManager.showInfoAlert("Error", "No se pudo registrar",
                        e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleEditar() {
        Estudiante seleccionado = tvEstudiante.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showInfoAlert("Sin selección", "Selecciona una fila",
                    "Debes seleccionar un estudiante de la tabla para editarlo.",
                    Alert.AlertType.WARNING);
            return;
        }

        // En edición no se pide curso (solo se editan sus datos básicos).
        Optional<String[]> resultado = mostrarDialogoEstudiante("Editar estudiante",
                seleccionado.getNombre(), seleccionado.getApellido(), seleccionado.getCorreoElectronico(), false);

        resultado.ifPresent(datos -> {
            try {
                dashboardService.actualizarEstudiante(seleccionado.getIdEstudainte(), datos[0], datos[1], datos[2]);
                handleLoadTablestuden();
            } catch (Exception e) {
                sceneManager.showInfoAlert("Error", "No se pudo actualizar",
                        e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleActualizar() {
        // Simula el "refrescar" de un navegador: vuelve a consultar la base
        // de datos desde cero y repinta toda la tabla con lo que haya ahora.
        try {
            handleLoadTablestuden();
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", "No se pudo refrescar la tabla",
                    e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleBorrar() {
        Estudiante seleccionado = tvEstudiante.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showInfoAlert("Sin selección", "Selecciona una fila",
                    "Debes seleccionar un estudiante de la tabla para eliminarlo.",
                    Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar estudiante");
        confirmacion.setContentText("¿Seguro que deseas eliminar a "
                + seleccionado.getNombre() + " " + seleccionado.getApellido() + "?");

        confirmacion.showAndWait().ifPresent(boton -> {
            if (boton == ButtonType.OK) {
                try {
                    dashboardService.eliminarEstudiante(seleccionado.getIdEstudainte());
                    handleLoadTablestuden();
                } catch (Exception e) {
                    sceneManager.showInfoAlert("Error", "No se pudo eliminar",
                            e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Diálogo reutilizable para capturar nombre, apellido, correo y
     * (solo cuando mostrarCurso es true) el curso al que se matriculará
     * el estudiante.
     *
     * Devuelve un arreglo [nombre, apellido, correo, idCurso] si se
     * confirma (idCurso viene null cuando mostrarCurso es false),
     * o vacío si se cancela.
     */
    private Optional<String[]> mostrarDialogoEstudiante(String titulo, String nombreInicial,
            String apellidoInicial, String correoInicial, boolean mostrarCurso) {

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(titulo);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        DialogPane pane = dialog.getDialogPane();
        pane.getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtNombre = new TextField(nombreInicial == null ? "" : nombreInicial);
        TextField txtApellido = new TextField(apellidoInicial == null ? "" : apellidoInicial);
        TextField txtCorreo = new TextField(correoInicial == null ? "" : correoInicial);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Apellido:"), 0, 1);
        grid.add(txtApellido, 1, 1);
        grid.add(new Label("Correo:"), 0, 2);
        grid.add(txtCorreo, 1, 2);

        ComboBox<Curso> cbCurso = new ComboBox<>();
        if (mostrarCurso) {
            try {
                ObservableList<Curso> cursos = dashboardService.listCursos();
                cbCurso.setItems(cursos);
                if (!cursos.isEmpty()) {
                    cbCurso.getSelectionModel().selectFirst();
                }
            } catch (Exception e) {
                sceneManager.showInfoAlert("Error", "No se pudieron cargar los cursos",
                        e.getMessage(), Alert.AlertType.ERROR);
            }
            grid.add(new Label("Curso:"), 0, 3);
            grid.add(cbCurso, 1, 3);
        }

        pane.setContent(grid);

        // El botón "Guardar" se deshabilita si faltan datos obligatorios,
        // para no dejar registrar un estudiante sin curso por accidente.
        javafx.scene.Node btnGuardarNode = pane.lookupButton(btnGuardar);
        Runnable validar = () -> {
            boolean camposLlenos = !txtNombre.getText().trim().isEmpty()
                    && !txtApellido.getText().trim().isEmpty()
                    && !txtCorreo.getText().trim().isEmpty();
            boolean cursoOk = !mostrarCurso || cbCurso.getValue() != null;
            btnGuardarNode.setDisable(!(camposLlenos && cursoOk));
        };
        txtNombre.textProperty().addListener((obs, old, nuevo) -> validar.run());
        txtApellido.textProperty().addListener((obs, old, nuevo) -> validar.run());
        txtCorreo.textProperty().addListener((obs, old, nuevo) -> validar.run());
        cbCurso.valueProperty().addListener((obs, old, nuevo) -> validar.run());
        validar.run();

        dialog.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                String idCursoSeleccionado = mostrarCurso && cbCurso.getValue() != null
                        ? cbCurso.getValue().getIdCurso() : null;
                return new String[]{txtNombre.getText(), txtApellido.getText(),
                        txtCorreo.getText(), idCursoSeleccionado};
            }
            return null;
        });

        return dialog.showAndWait();
    }

}
