/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.model.Curso;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import main.java.edu.mmcoffee.colegiogotitas.model.Calificacion;

public class EstudianteRepository {

    /**
     * Obtiene todos los estudiantes que tienen curso asignado.
     */
  public ObservableList<Estudiante> findAll() throws Exception {

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
                .getConnectionDataBase()
                .prepareStatement(sql);
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

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error en la consulta de estudiantes: "
                    + e.getMessage(), e);
        }

        return studentList;
    }

    /**
     * Busca estudiantes cuyo nombre o apellido coincidan parcialmente.
     */
    public ObservableList<Estudiante> findByNombre(String nombre) throws Exception {

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
                + "ON d.id_docente = ac.id_docente "
                + "WHERE e.nombre LIKE ? "
                + "OR e.apellido LIKE ?";

        ObservableList<Estudiante> studentList = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {

            String filtro = "%" + nombre.trim() + "%";
            pstm.setString(1, filtro);
            pstm.setString(2, filtro);

            try (ResultSet rs = pstm.executeQuery()) {

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
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al buscar estudiantes por nombre: "
                    + e.getMessage(), e);
        }

        return studentList;
    }

    /**
     * Obtiene todos los cursos disponibles.
     */
    public ObservableList<Curso> findAllCursos() throws Exception {

        String sql = "SELECT id_curso, nombre_curso "
                + "FROM cursos "
                + "ORDER BY nombre_curso";

        ObservableList<Curso> cursos = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {

                cursos.add(new Curso(
                        rs.getString("id_curso"),
                        rs.getString("nombre_curso")
                ));
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al cargar los cursos: "
                    + e.getMessage(), e);
        }

        return cursos;
    }

    /**
     * Genera automáticamente un nuevo ID.
     */
    private String generarNuevoId(
            Connection conn,
            String tabla,
            String columna,
            String prefijo,
            int longitud) {

        String sql = "SELECT " + columna
                + " FROM " + tabla
                + " ORDER BY CAST(SUBSTRING("
                + columna + ", "
                + (prefijo.length() + 1)
                + ") AS UNSIGNED) DESC LIMIT 1";

        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            int siguiente = 1;

            if (rs.next()) {

                String ultimoId = rs.getString(1);

                if (ultimoId != null
                        && ultimoId.length() > prefijo.length()) {

                    int numeroActual = Integer.parseInt(
                            ultimoId.substring(prefijo.length()));

                    siguiente = numeroActual + 1;
                }
            }

            return String.format(
                    "%s%0" + longitud + "d",
                    prefijo,
                    siguiente);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al generar el ID de "
                    + tabla + ": "
                    + e.getMessage(), e);
        }
    }

    /**
     * Inserta un estudiante.
     */
    public void insertar(Estudiante estudiante) {

        Connection conn;

        try {

            conn = DataBaseConnection.getConnectionDataBase();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error de conexión: "
                    + e.getMessage(), e);
        }

        String nuevoId = generarNuevoId(
                conn,
                "estudiantes",
                "id_estudiante",
                "EST",
                3);

        String sql = "INSERT INTO estudiantes "
                + "(id_estudiante, nombre, apellido, correo_electronico) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, nuevoId);
            pstm.setString(2, estudiante.getNombre());
            pstm.setString(3, estudiante.getApellido());
            pstm.setString(4, estudiante.getCorreoElectronico());

            pstm.executeUpdate();

            estudiante.setIdEstudiante(nuevoId);

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al registrar el estudiante: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Registra un estudiante y lo asigna a un curso dentro de una transacción.
     */
    public void registrarConCurso(Estudiante estudiante, String idCurso) {

        Connection conn;

        try {

            conn = DataBaseConnection.getConnectionDataBase();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error de conexión: "
                    + e.getMessage(), e);
        }

        try {

            conn.setAutoCommit(false);

            // 1. CREAR ESTUDIANTE
            String nuevoIdEstudiante = generarNuevoId(
                    conn,
                    "estudiantes",
                    "id_estudiante",
                    "EST",
                    3);

            String sqlEstudiante =
                    "INSERT INTO estudiantes "
                    + "(id_estudiante, nombre, apellido, correo_electronico) "
                    + "VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(sqlEstudiante)) {

                pstm.setString(1, nuevoIdEstudiante);
                pstm.setString(2, estudiante.getNombre());
                pstm.setString(3, estudiante.getApellido());
                pstm.setString(4, estudiante.getCorreoElectronico());

                pstm.executeUpdate();
            }

            // 2. BUSCAR SECCIÓN DEL CURSO
            String idSeccion = null;

            String sqlBuscarSeccion =
                    "SELECT id_seccion "
                    + "FROM secciones "
                    + "WHERE id_curso = ? "
                    + "LIMIT 1";

            try (PreparedStatement pstm = conn.prepareStatement(sqlBuscarSeccion)) {

                pstm.setString(1, idCurso);

                try (ResultSet rs = pstm.executeQuery()) {

                    if (rs.next()) {
                        idSeccion = rs.getString("id_seccion");
                    }
                }
            }

            // 3. CREAR SECCIÓN SI NO EXISTE
            if (idSeccion == null) {

                idSeccion = generarNuevoId(
                        conn,
                        "secciones",
                        "id_seccion",
                        "SEC",
                        3);

                String nombreSeccionNueva = "Sección " + idSeccion;

                String sqlSeccion =
                        "INSERT INTO secciones "
                        + "(id_seccion, id_curso, nombre_seccion) "
                        + "VALUES (?, ?, ?)";

                try (PreparedStatement pstm = conn.prepareStatement(sqlSeccion)) {

                    pstm.setString(1, idSeccion);
                    pstm.setString(2, idCurso);
                    pstm.setString(3, nombreSeccionNueva);

                    pstm.executeUpdate();
                }
            }

            // 4. CREAR MATRÍCULA
            String nuevoIdMatricula = generarNuevoId(
                    conn,
                    "matriculas",
                    "id_matricula",
                    "MAT",
                    3);

            String sqlMatricula =
                    "INSERT INTO matriculas "
                    + "(id_matricula, id_seccion, id_estudiante) "
                    + "VALUES (?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(sqlMatricula)) {

                pstm.setString(1, nuevoIdMatricula);
                pstm.setString(2, idSeccion);
                pstm.setString(3, nuevoIdEstudiante);

                pstm.executeUpdate();
            }

            // 5. BUSCAR DOCENTE
            String idDocente = null;

            String sqlBuscarDocente =
                    "SELECT id_docente "
                    + "FROM asignacion_cursos "
                    + "WHERE id_curso = ? "
                    + "LIMIT 1";

            try (PreparedStatement pstm = conn.prepareStatement(sqlBuscarDocente)) {

                pstm.setString(1, idCurso);

                try (ResultSet rs = pstm.executeQuery()) {

                    if (rs.next()) {
                        idDocente = rs.getString("id_docente");
                    }
                }
            }

            if (idDocente == null) {

                String sqlDocente =
                        "SELECT id_docente "
                        + "FROM docentes "
                        + "LIMIT 1";

                try (PreparedStatement pstm = conn.prepareStatement(sqlDocente);
                     ResultSet rs = pstm.executeQuery()) {

                    if (rs.next()) {
                        idDocente = rs.getString("id_docente");
                    }
                }
            }

            if (idDocente == null) {
                throw new RuntimeException(
                        "No hay ningún docente registrado para asignar al curso.");
            }

            // 6. CREAR ASIGNACIÓN DEL CURSO
            String nuevoIdAsignacion = generarNuevoId(
                    conn,
                    "asignacion_cursos",
                    "id_asignacion",
                    "ASG",
                    3);

            String sqlAsignacion =
                    "INSERT INTO asignacion_cursos "
                    + "(id_asignacion, id_seccion, id_curso, id_docente, id_matricula) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstm = conn.prepareStatement(sqlAsignacion)) {

                pstm.setString(1, nuevoIdAsignacion);
                pstm.setString(2, idSeccion);
                pstm.setString(3, idCurso);
                pstm.setString(4, idDocente);
                pstm.setString(5, nuevoIdMatricula);

                pstm.executeUpdate();
            }

            conn.commit();
            estudiante.setIdEstudiante(nuevoIdEstudiante);

        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (SQLException ex) {
                // Se conserva el error original.
            }

            throw new RuntimeException(
                    "Error al registrar el estudiante con su curso: "
                    + e.getMessage(), e);

        } finally {

            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // No crítico.
            }
        }
    }

    /**
     * Sobrecarga del método registrarConCurso aceptando un objeto Curso.
     */
    public void registrarConCurso(Estudiante estudiante, Curso curso) {
        registrarConCurso(estudiante, curso.getIdCurso());
    }

    /**
     * Actualiza un estudiante existente.
     */
    public void actualizar(Estudiante estudiante) throws Exception {

        String sql =
                "UPDATE estudiantes "
                + "SET nombre = ?, "
                + "apellido = ?, "
                + "correo_electronico = ? "
                + "WHERE id_estudiante = ?";

        try (PreparedStatement pstm =
                DataBaseConnection
                        .getConnectionDataBase()
                        .prepareStatement(sql)) {

            pstm.setString(1, estudiante.getNombre());
            pstm.setString(2, estudiante.getApellido());
            pstm.setString(3, estudiante.getCorreoElectronico());
            pstm.setString(4, estudiante.getIdEstudiante());

            pstm.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al actualizar el estudiante: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Elimina un estudiante y sus relaciones asociadas.
     */
    public void eliminar(String idEstudiante) {

        String sqlAsignaciones =
                "DELETE ac "
                + "FROM asignacion_cursos ac "
                + "INNER JOIN matriculas m "
                + "ON m.id_matricula = ac.id_matricula "
                + "WHERE m.id_estudiante = ?";

        String sqlMatriculas =
                "DELETE FROM matriculas "
                + "WHERE id_estudiante = ?";

        String sqlEstudiante =
                "DELETE FROM estudiantes "
                + "WHERE id_estudiante = ?";

        Connection conn;

        try {

            conn = DataBaseConnection.getConnectionDataBase();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error de conexión: "
                    + e.getMessage(), e);
        }

        try {

            conn.setAutoCommit(false);

            try (PreparedStatement pstm = conn.prepareStatement(sqlAsignaciones)) {
                pstm.setString(1, idEstudiante);
                pstm.executeUpdate();
            }

            try (PreparedStatement pstm = conn.prepareStatement(sqlMatriculas)) {
                pstm.setString(1, idEstudiante);
                pstm.executeUpdate();
            }

            try (PreparedStatement pstm = conn.prepareStatement(sqlEstudiante)) {
                pstm.setString(1, idEstudiante);
                pstm.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (SQLException ex) {
                // Se conserva el error original.
            }

            throw new RuntimeException(
                    "Error al eliminar el estudiante: "
                    + e.getMessage(), e);

        } finally {

            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // No crítico.
            }
        }
    }

    /**
     * Guarda una nueva calificación para un estudiante.
     */
   /**
     * Guarda una nueva calificación para un estudiante.
     */
    public void guardarCalificacion(String idEstudiante, String idDocente, String idCurso, double nota, String descripcion) throws Exception {
        
        Connection conn = DataBaseConnection.getConnectionDataBase();

        String sql = "INSERT INTO calificaciones "
                + "(id_estudiante, id_docente, id_curso, nota, descripcion, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, CURDATE())";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, idEstudiante);
            pstm.setString(2, idDocente);
            pstm.setString(3, idCurso);
            pstm.setDouble(4, nota);
            pstm.setString(5, descripcion);

            pstm.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al guardar la calificación: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Consulta el listado de calificaciones de un estudiante.
     */
    public ObservableList<Calificacion> findCalificacionesByEstudiante(String idEstudiante) throws Exception {

        String sql = "SELECT id_calificacion, id_estudiante, id_docente, id_curso, nota, descripcion, fecha_registro "
                + "FROM calificaciones "
                + "WHERE id_estudiante = ?";

        ObservableList<Calificacion> lista = FXCollections.observableArrayList();

        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {

            pstm.setString(1, idEstudiante);

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {

                    // Conversión de java.sql.Date a java.time.LocalDate
                    java.sql.Date sqlDate = rs.getDate("fecha_registro");
                    LocalDate fecha = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    lista.add(new Calificacion(
                            rs.getInt("id_calificacion"),
                            rs.getString("id_estudiante"),
                            rs.getString("id_docente"),
                            rs.getString("id_curso"),
                            rs.getDouble("nota"),
                            rs.getString("descripcion"),
                            fecha
                    ));
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error al consultar las calificaciones: "
                    + e.getMessage(), e);
        }

        return lista;
    }
}
