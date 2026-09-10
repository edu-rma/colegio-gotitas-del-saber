/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.model;

/**
 * Representa un curso para poder mostrarlo en un ComboBox
 * (el toString() controla lo que se ve en pantalla; el id_curso
 * se guarda internamente para usarlo al registrar al estudiante).
 */
public class Curso {
    private final String idCurso;
    private final String nombreCurso;

    public Curso(String idCurso, String nombreCurso) {
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
    }

    public String getIdCurso() {
        return idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    @Override
    public String toString() {
        return nombreCurso;
    }
}
