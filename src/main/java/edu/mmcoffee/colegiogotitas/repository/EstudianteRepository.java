/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.model.Curso;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;

public class EstudianteRepository {

    /**
     * Solo trae estudiantes que YA tienen curso asignado (matricula +
     * asignacion_cursos). Se usa INNER JOIN a propósito: si un estudiante
     * no tiene curso, no debe aparecer en la tabla.
     */
    public ObservableList<Estudiante> findAll() {

        String sql = "SELECT "
                + "e.id_estudiante, "
                + "e.nombre AS nombre_estudiante, "
                + "e.apellido AS apellido_estudiante, "
                + "e.correo_electronico, "
                + "s.nombre_seccion, "
                + "c.nombre_curso, "
                + "d.nombre AS nombre_docente, "
                + "d.apellido AS apellido_docente "
                + "FROM estudiantes AS e "
                + "INNER JOIN matriculas AS m "
                + "ON m.id_estudiante = e.id_estudiante "
                + "INNER JOIN asignacion_cursos AS ac "
                + "ON ac.id_matricula = m.id_matricula "
                + "INNER JOIN secciones AS s "
                + "ON s.id_seccion = ac.id_seccion "
                + "INNER JOIN cursos AS c "
                + "ON c.id_curso = ac.id_curso "
                + "INNER JOIN docentes AS d "
                + "ON d.id_docente = ac.id_docente";

        ObservableList<Estudiante> studentList = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {

                studentList.add(new Estudiante(
                        rs.getString("id_estudiante"),
                        rs.getString("nombre_estudiante"),
                        rs.getString("apellido_estudiante"),
                        rs.getString("correo_electronico"),
                        rs.getString("nombre_seccion"),
                        rs.getString("nombre_curso"),
                        rs.getString("nombre_docente"),
                        rs.getString("apellido_docente")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error en la consulta: " + e.getMessage(), e
            );
        }

        return studentList;
    }

    /**
     * Trae todos los cursos disponibles, para llenar el ComboBox
     * del diálogo de "Nuevo estudiante".
     */
    public ObservableList<Curso> findAllCursos() {
        String sql = "SELECT id_curso, nombre_curso FROM cursos ORDER BY nombre_curso";

        ObservableList<Curso> cursos = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                cursos.add(new Curso(
                        rs.getString("id_curso"),
                        rs.getString("nombre_curso")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al cargar los cursos: " + e.getMessage(), e
            );
        }

        return cursos;
    }

    // NOTA: se asume que la tabla "estudiantes" tiene las columnas
    // id_estudiante (PK, formato "EST###"... SIN autoincrement),
    // nombre, apellido, correo_electronico.

    /**
     * Genera el siguiente id con un prefijo dado (EST/MAT/ASG) buscando el
     * número más alto ya existente en la columna indicada. Debe ejecutarse
     * usando la MISMA conexión que el resto de la transacción para que
     * cuente también los ids insertados en pasos previos aún no confirmados.
     */
    private String generarNuevoId(Connection conn, String tabla, String columna, String prefijo, int longitud) {
        String sql = "SELECT " + columna + " FROM " + tabla
                + " ORDER BY CAST(SUBSTRING(" + columna + ", " + (prefijo.length() + 1) + ") AS UNSIGNED) DESC LIMIT 1";

        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            int siguiente = 1;
            if (rs.next()) {
                String ultimoId = rs.getString(1);
                int numeroActual = Integer.parseInt(ultimoId.substring(prefijo.length()));
                siguiente = numeroActual + 1;
            }
            return String.format("%s%0" + longitud + "d", prefijo, siguiente);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al generar el id de " + tabla + ": " + e.getMessage(), e
            );
        }
    }

    public void insertar(Estudiante estudiante) {
        Connection conn;
        try {
            conn = DataBaseConnection.getConnectionDataBase();
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage(), e);
        }

        String nuevoId = generarNuevoId(conn, "estudiantes", "id_estudiante", "EST", 3);
        String sql = "INSERT INTO estudiantes (id_estudiante, nombre, apellido, correo_electronico) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, nuevoId);
            pstm.setString(2, estudiante.getNombre());
            pstm.setString(3, estudiante.getApellido());
            pstm.setString(4, estudiante.getCorreoElectronico());
            pstm.executeUpdate();

            estudiante.setIdEstudainte(nuevoId);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al registrar el estudiante: " + e.getMessage(), e
            );
        }
    }

    /**
     * Registra un estudiante nuevo Y lo matricula automáticamente en el
     * curso indicado: crea el estudiante, busca (o reutiliza) una sección
     * de ese curso, crea su matrícula y su asignación de curso. Todo en
     * una sola transacción: si un paso falla, no queda nada a medias.
     *
     * @param estudiante datos del estudiante (sin id todavía)
     * @param idCurso    id del curso elegido en el ComboBox
     */
    public void registrarConCurso(Estudiante estudiante, String idCurso) {
        Connection conn;
        try {
            conn = DataBaseConnection.getConnectionDataBase();
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage(), e);
        }

        try {
            conn.setAutoCommit(false);

            // 1) Crear el estudiante
            String nuevoIdEstudiante = generarNuevoId(conn, "estudiantes", "id_estudiante", "EST", 3);
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO estudiantes (id_estudiante, nombre, apellido, correo_electronico) VALUES (?, ?, ?, ?)")) {
                pstm.setString(1, nuevoIdEstudiante);
                pstm.setString(2, estudiante.getNombre());
                pstm.setString(3, estudiante.getApellido());
                pstm.setString(4, estudiante.getCorreoElectronico());
                pstm.executeUpdate();
            }

            // 2) Buscar una sección que pertenezca a ese curso
            String idSeccion = null;
            try (PreparedStatement pstm = conn.prepareStatement(
                    "SELECT id_seccion FROM secciones WHERE id_curso = ? LIMIT 1")) {
                pstm.setString(1, idCurso);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        idSeccion = rs.getString(1);
                    }
                }
            }
            if (idSeccion == null) {
                // Ese curso todavía no tiene ninguna sección: se crea una
                // automáticamente para poder matricular al estudiante.
                idSeccion = generarNuevoId(conn, "secciones", "id_seccion", "SEC", 3);
                String nombreSeccionNueva = "Sección " + idSeccion;
                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO secciones (id_seccion, id_curso, nombre_seccion) VALUES (?, ?, ?)")) {
                    pstm.setString(1, idSeccion);
                    pstm.setString(2, idCurso);
                    pstm.setString(3, nombreSeccionNueva);
                    pstm.executeUpdate();
                }
            }

            // 3) Crear la matrícula (estudiante + sección)
            String nuevoIdMatricula = generarNuevoId(conn, "matriculas", "id_matricula", "MAT", 3);
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO matriculas (id_matricula, id_seccion, id_estudiante) VALUES (?, ?, ?)")) {
                pstm.setString(1, nuevoIdMatricula);
                pstm.setString(2, idSeccion);
                pstm.setString(3, nuevoIdEstudiante);
                pstm.executeUpdate();
            }

            // 4) Buscar un docente que ya imparta ese curso (para reutilizarlo).
            //    Si ningún docente lo imparte todavía, se toma el primer
            //    docente disponible como valor por defecto.
            String idDocente = null;
            try (PreparedStatement pstm = conn.prepareStatement(
                    "SELECT id_docente FROM asignacion_cursos WHERE id_curso = ? LIMIT 1")) {
                pstm.setString(1, idCurso);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        idDocente = rs.getString(1);
                    }
                }
            }
            if (idDocente == null) {
                try (PreparedStatement pstm = conn.prepareStatement(
                        "SELECT id_docente FROM docentes LIMIT 1");
                     ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        idDocente = rs.getString(1);
                    }
                }
            }
            if (idDocente == null) {
                throw new RuntimeException(
                        "No hay ningún docente registrado para asignar al curso.");
            }

            // 5) Crear la asignación de curso (sección + curso + docente + matrícula)
            String nuevoIdAsignacion = generarNuevoId(conn, "asignacion_cursos", "id_asignacion", "ASG", 3);
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO asignacion_cursos (id_asignacion, id_seccion, id_curso, id_docente, id_matricula) "
                            + "VALUES (?, ?, ?, ?, ?)")) {
                pstm.setString(1, nuevoIdAsignacion);
                pstm.setString(2, idSeccion);
                pstm.setString(3, idCurso);
                pstm.setString(4, idDocente);
                pstm.setString(5, nuevoIdMatricula);
                pstm.executeUpdate();
            }

            conn.commit();
            estudiante.setIdEstudainte(nuevoIdEstudiante);

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                // si ni siquiera se puede hacer rollback, se ignora y se
                // deja que se propague el error original
            }
            throw new RuntimeException(
                    "Error al registrar el estudiante con su curso: " + e.getMessage(), e
            );
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // no crítico
            }
        }
    }

    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiantes "
                + "SET nombre = ?, apellido = ?, correo_electronico = ? "
                + "WHERE id_estudiante = ?";

        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase().prepareStatement(sql)) {

            pstm.setString(1, estudiante.getNombre());
            pstm.setString(2, estudiante.getApellido());
            pstm.setString(3, estudiante.getCorreoElectronico());
            pstm.setString(4, estudiante.getIdEstudainte());
            pstm.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al actualizar el estudiante: " + e.getMessage(), e
            );
        }
    }

    /**
     * Elimina un estudiante junto con toda su información dependiente
     * (asignacion_cursos y matriculas) para evitar el error de
     * "Cannot delete or update a parent row: a foreign key constraint fails".
     * Todo se ejecuta dentro de una misma transacción: si algo falla,
     * se revierte y no queda nada a medio borrar.
     */
    public void eliminar(String idEstudiante) {
        String sqlAsignaciones = "DELETE ac FROM asignacion_cursos ac "
                + "INNER JOIN matriculas m ON m.id_matricula = ac.id_matricula "
                + "WHERE m.id_estudiante = ?";
        String sqlMatriculas = "DELETE FROM matriculas WHERE id_estudiante = ?";
        String sqlEstudiante = "DELETE FROM estudiantes WHERE id_estudiante = ?";

        Connection conn;
        try {
            conn = DataBaseConnection.getConnectionDataBase();
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión: " + e.getMessage(), e);
        }

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmAsignaciones = conn.prepareStatement(sqlAsignaciones)) {
                pstmAsignaciones.setString(1, idEstudiante);
                pstmAsignaciones.executeUpdate();
            }

            try (PreparedStatement pstmMatriculas = conn.prepareStatement(sqlMatriculas)) {
                pstmMatriculas.setString(1, idEstudiante);
                pstmMatriculas.executeUpdate();
            }

            try (PreparedStatement pstmEstudiante = conn.prepareStatement(sqlEstudiante)) {
                pstmEstudiante.setString(1, idEstudiante);
                pstmEstudiante.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                // si ni siquiera se puede hacer rollback, se ignora y se
                // deja que se propague el error original
            }
            throw new RuntimeException(
                    "Error al eliminar el estudiante: " + e.getMessage(), e
            );
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // no crítico
            }
        }
    }
}
