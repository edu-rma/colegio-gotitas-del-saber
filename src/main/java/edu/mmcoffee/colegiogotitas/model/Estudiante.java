/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.model;

/**
 *
 * @author informatica
 */
public class Estudiante {
    private String idEstudainte;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String nombreSeccion;
    private String nombreCurso;
    private String nombreDocente;
    private String apellidoDocente;
 
    public Estudiante(String idEstudainte, String nombre, String apellido, String correoElectronico, String nombreSeccion, String nombreCurso, String nombreDocente, String apellidoDocente) {
        this.idEstudainte = idEstudainte;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.nombreSeccion = nombreSeccion;
        this.nombreCurso = nombreCurso;
        this.nombreDocente = nombreDocente;
        this.apellidoDocente = apellidoDocente;
    }
 
    public String getIdEstudainte() {
        return idEstudainte;
    }
 
    public void setIdEstudainte(String idEstudainte) {
        this.idEstudainte = idEstudainte;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public String getApellido() {
        return apellido;
    }
 
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
 
    public String getCorreoElectronico() {
        return correoElectronico;
    }
 
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
 
    public String getNombreSeccion() {
        return nombreSeccion;
    }
 
    public void setNombreSeccion(String nombreSeccion) {
        this.nombreSeccion = nombreSeccion;
    }
 
    public String getNombreCurso() {
        return nombreCurso;
    }
 
    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }
 
    public String getNombreDocente() {
        return nombreDocente;
    }
 
    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }
 
    public String getApellidoDocente() {
        return apellidoDocente;
    }
 
    public void setApellidoDocente(String apellidoDocente) {
        this.apellidoDocente = apellidoDocente;
    }

}