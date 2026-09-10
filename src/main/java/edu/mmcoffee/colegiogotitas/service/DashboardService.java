package main.java.edu.mmcoffee.colegiogotitas.service;

import javafx.collections.ObservableList;
import main.java.edu.mmcoffee.colegiogotitas.model.Curso;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;
import main.java.edu.mmcoffee.colegiogotitas.repository.EstudianteRepository;

public class DashboardService {

    private EstudianteRepository estudianteRepository;

    public DashboardService(EstudianteRepository dashboardRepository) {
        this.estudianteRepository = dashboardRepository;
    }

    // ==========================================================
    // LISTAR ESTUDIANTES
    // ==========================================================

    public ObservableList<Estudiante> listStudent() throws Exception {
        ObservableList<Estudiante> estudiantes = estudianteRepository.findAll();

        if (estudiantes == null || estudiantes.isEmpty()) {
            throw new RuntimeException("Sin datos que mostrar");
        }

        return estudiantes;
    }

    // ==========================================================
    // LISTAR CURSOS
    // ==========================================================

    /**
     * Obtiene los cursos disponibles para llenar
     * el ComboBox del formulario de registro.
     */
    public ObservableList<Curso> listCursos() throws Exception {
        return estudianteRepository.findAllCursos();
    }

    // ==========================================================
    // CREAR ESTUDIANTE
    // ==========================================================

    /**
     * Crea un estudiante y lo matricula automáticamente
     * en el curso seleccionado.
     */
    public void crearEstudiante(
            String nombre,
            String apellido,
            String correo,
            String idCurso) throws Exception {

        validarDatosBasicos(nombre, apellido, correo);

        if (idCurso == null || idCurso.trim().isEmpty()) {
            throw new RuntimeException("Debes seleccionar un curso para el estudiante.");
        }

        Estudiante nuevo = new Estudiante(
                null,
                nombre,
                apellido,
                correo,
                null,
                null,
                null,
                null
        );

        estudianteRepository.registrarConCurso(nuevo, idCurso);
    }

    // ==========================================================
    // ACTUALIZAR ESTUDIANTE
    // ==========================================================

    /**
     * Actualiza los datos básicos de un estudiante.
     */
    public void actualizarEstudiante(
            String idEstudiante,
            String nombre,
            String apellido,
            String correo) throws Exception {

        if (idEstudiante == null || idEstudiante.trim().isEmpty()) {
            throw new RuntimeException("No hay un estudiante seleccionado para actualizar.");
        }

        validarDatosBasicos(nombre, apellido, correo);

        Estudiante actualizado = new Estudiante(
                idEstudiante,
                nombre,
                apellido,
                correo,
                null,
                null,
                null,
                null
        );

        estudianteRepository.actualizar(actualizado);
    }

    // ==========================================================
    // ELIMINAR ESTUDIANTE
    // ==========================================================

    /**
     * Elimina un estudiante junto con sus
     * registros relacionados.
     */
    public void eliminarEstudiante(String idEstudiante) throws Exception {
        if (idEstudiante == null || idEstudiante.trim().isEmpty()) {
            throw new RuntimeException("No hay un estudiante seleccionado para eliminar.");
        }

        estudianteRepository.eliminar(idEstudiante);
    }

    // ==========================================================
    // VALIDAR DATOS
    // ==========================================================

    private void validarDatosBasicos(
            String nombre,
            String apellido,
            String correo) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio.");
        }

        if (correo == null || correo.trim().isEmpty()) {
            throw new RuntimeException("El correo es obligatorio.");
        }
    }
}