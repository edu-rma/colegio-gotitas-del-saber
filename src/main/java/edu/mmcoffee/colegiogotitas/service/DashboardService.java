/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public ObservableList<Estudiante> listStudent() throws Exception {
        if (estudianteRepository.findAll() == null) {
            throw new RuntimeException("sin datos que mostrar");
        } else {
            return estudianteRepository.findAll();
        }
    }

    /**
     * Cursos disponibles para llenar el ComboBox del diálogo de registro.
     */
    public ObservableList<Curso> listCursos() throws Exception {
        return estudianteRepository.findAllCursos();
    }

    /**
     * Registra un estudiante y lo matricula automáticamente en el curso
     * elegido (la sección, la matrícula y la asignación de curso se
     * generan solas a partir del id_curso).
     */
    public void crearEstudiante(String nombre, String apellido, String correo, String idCurso) throws Exception {
        validarDatosBasicos(nombre, apellido, correo);
        if (idCurso == null || idCurso.isEmpty()) {
            throw new RuntimeException("Debes seleccionar un curso para el estudiante.");
        }
        Estudiante nuevo = new Estudiante(null, nombre, apellido, correo, null, null, null, null);
        estudianteRepository.registrarConCurso(nuevo, idCurso);
    }

    public void actualizarEstudiante(String idEstudiante, String nombre, String apellido, String correo) throws Exception {
        if (idEstudiante == null || idEstudiante.isEmpty()) {
            throw new RuntimeException("No hay un estudiante seleccionado para actualizar.");
        }
        validarDatosBasicos(nombre, apellido, correo);
        Estudiante actualizado = new Estudiante(idEstudiante, nombre, apellido, correo, null, null, null, null);
        estudianteRepository.actualizar(actualizado);
    }

    public void eliminarEstudiante(String idEstudiante) throws Exception {
        if (idEstudiante == null || idEstudiante.isEmpty()) {
            throw new RuntimeException("No hay un estudiante seleccionado para eliminar.");
        }
        estudianteRepository.eliminar(idEstudiante);
    }

    private void validarDatosBasicos(String nombre, String apellido, String correo) {
        if (nombre == null || nombre.trim().isEmpty()
                || apellido == null || apellido.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()) {
            throw new RuntimeException("Nombre, apellido y correo son obligatorios.");
        }
    }

}
