/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;
 
public class EstudianteRepository {
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
                + "FROM asignacion_cursos AS ac "
                + "INNER JOIN matriculas AS m "
                + "ON m.id_matricula = ac.id_matricula "
                + "INNER JOIN secciones AS s "
                + "ON s.id_seccion = ac.id_seccion "
                + "INNER JOIN cursos AS c "
                + "ON c.id_curso = ac.id_curso "
                + "INNER JOIN docentes AS d "
                + "ON d.id_docente = ac.id_docente "
                + "INNER JOIN estudiantes AS e "
                + "ON e.id_estudiante = m.id_estudiante";
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
}