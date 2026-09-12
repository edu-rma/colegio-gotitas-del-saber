/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.controller;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import main.java.edu.mmcoffee.colegiogotitas.model.Calificacion;
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
    private Button btnAgregarNotas;
    @FXML
    private Button btnVerNotasEstudiante;
    @FXML
    private Button btregistrar;
    @FXML
    private Button bteditar;
    @FXML
    private Button btactualizar;
    @FXML
    private Button Btborrar;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private TextField txtBuscarNombre;
    @FXML
    private Button btnBuscar;

    public DashboardController(DashboardService dashboardService, SceneManager sceneManager) {
        this.dashboardService = dashboardService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            handleLoadTablestuden();
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", "No se pudo cargar la tabla",
                    e.getMessage(), Alert.AlertType.ERROR);
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

        Optional<String[]> resultado = mostrarDialogoEstudiante("Editar estudiante",
                seleccionado.getNombre(), seleccionado.getApellido(), seleccionado.getCorreoElectronico(), false);

        resultado.ifPresent(datos -> {
            try {
                dashboardService.actualizarEstudiante(seleccionado.getIdEstudiante(), datos[0], datos[1], datos[2]);
                handleLoadTablestuden();
            } catch (Exception e) {
                sceneManager.showInfoAlert("Error", "No se pudo actualizar",
                        e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleActualizar() {
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
                    dashboardService.eliminarEstudiante(seleccionado.getIdEstudiante());
                    handleLoadTablestuden();
                } catch (Exception e) {
                    sceneManager.showInfoAlert("Error", "No se pudo eliminar",
                            e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleAgregarNotas() {
        Estudiante seleccionado = tvEstudiante.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showInfoAlert("Sin selección", "Selecciona un estudiante",
                    "Debes seleccionar un estudiante de la tabla para agregar notas.",
                    Alert.AlertType.WARNING);
            return;
        }

        Dialog<double[]> dialog = new Dialog<>();
        dialog.setTitle("Agregar Calificación");
        dialog.setHeaderText("Estudiante: " + seleccionado.getNombre() + " " + seleccionado.getApellido());
        
        java.net.URL cssUrlAgregar = getClass().getResource("main/resources/css/styles.css");
         if (cssUrlAgregar != null) {
    dialog.getDialogPane().getStylesheets().add(cssUrlAgregar.toExternalForm());
          }
         
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField txtNota = new TextField();
        txtNota.setPromptText("Ej. 85.5");
        TextField txtDescripcion = new TextField();
        txtDescripcion.setPromptText("Ej. Examen Parcial");

        grid.add(new Label("Nota (0-100):"), 0, 0);
        grid.add(txtNota, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(txtDescripcion, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    double nota = Double.parseDouble(txtNota.getText().trim());
                    String idEst = seleccionado.getIdEstudiante();
                    
                    dashboardService.registrarCalificacion(idEst, "DOC001", "CUR001", nota, txtDescripcion.getText().trim());
                    
                    sceneManager.showInfoAlert("Éxito", "Calificación Guardada",
                            "La nota se registró correctamente.", Alert.AlertType.INFORMATION);
                } catch (NumberFormatException e) {
                    sceneManager.showInfoAlert("Error", "Nota inválida",
                            "Debe ingresar un valor numérico para la nota.", Alert.AlertType.ERROR);
                } catch (Exception e) {
                    sceneManager.showInfoAlert("Error", "No se pudo guardar la nota",
                            e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleVerNotas() {
        Estudiante seleccionado = tvEstudiante.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showInfoAlert("Sin selección", "Selecciona un estudiante",
                    "Debes seleccionar un estudiante de la tabla para ver sus notas.",
                    Alert.AlertType.WARNING);
            return;
        }

        try {
            String idEst = seleccionado.getIdEstudiante();
            
            ObservableList<Calificacion> notas = dashboardService.obtenerCalificacionesEstudiante(idEst);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Notas del Estudiante");
            dialog.setHeaderText("Historial de Notas: " + seleccionado.getNombre() + " " + seleccionado.getApellido());
            
            java.net.URL cssUrl = getClass().getResource("main/resources/css/styles.css");
            if (cssUrl != null) {
                dialog.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
            }
            
            dialog.getDialogPane().getStyleClass().add("custom-dialog");
            
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            TableView<Calificacion> tvNotas = new TableView<>();
            TableColumn<Calificacion, Double> colNota = new TableColumn<>("Nota");
            colNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

            TableColumn<Calificacion, String> colDesc = new TableColumn<>("Descripción");
            colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

            tvNotas.getColumns().addAll(colNota, colDesc);
            tvNotas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tvNotas.setItems(notas);

            dialog.getDialogPane().setContent(tvNotas);
            dialog.showAndWait();

        } catch (Exception e) {
            sceneManager.showInfoAlert("Información", "Sin registro",
                    e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    private Optional<String[]> mostrarDialogoEstudiante(String titulo, String nombreInicial,
            String apellidoInicial, String correoInicial, boolean mostrarCurso) {

        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(titulo);

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        DialogPane pane = dialog.getDialogPane();
        
        pane.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        pane.getStyleClass().add("custom-dialog");
        
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

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro que desea cerrar sesión?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                sceneManager.showLoginView();
            } catch (Exception e) {
                sceneManager.showInfoAlert("Error", "No se pudo cerrar sesión",
                        e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleBuscar() {
        String textoBusqueda = txtBuscarNombre.getText();
        try {
            tvEstudiante.setItems(dashboardService.buscarPorNombre(textoBusqueda));
        } catch (Exception e) {
            tvEstudiante.getItems().clear();
            sceneManager.showInfoAlert("Sin resultados", "Búsqueda sin coincidencias",
                    e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}