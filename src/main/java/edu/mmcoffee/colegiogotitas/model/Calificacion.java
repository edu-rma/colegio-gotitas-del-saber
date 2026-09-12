
package main.java.edu.mmcoffee.colegiogotitas.model;

import java.time.LocalDate;



public class Calificacion {
    
    private final int idCalificacion;
    private final String idEstudiante;
    private final String idDocente;
    private final String idCurso;
    private final double nota;
    private final String descripcion;
    private final LocalDate fechaCalificacion;

    public Calificacion(int idCalificacion, String idEstudiante, String idDocente, String idCurso, double nota, String descripcion, LocalDate fechaCalificacion) {
        this.idCalificacion = idCalificacion;
        this.idEstudiante = idEstudiante;
        this.idDocente = idDocente;
        this.idCurso = idCurso;
        this.nota = nota;
        this.descripcion = descripcion;
        this.fechaCalificacion = fechaCalificacion;
    }

    public int getIdCalificacion() { return idCalificacion; }
    public String getIdEstudiante() { return idEstudiante; }
    public String getIdDocente() { return idDocente; }
    public String getIdCurso() { return idCurso; }
    public double getNota() { return nota; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaCalificacion() { return fechaCalificacion; }
}
    

