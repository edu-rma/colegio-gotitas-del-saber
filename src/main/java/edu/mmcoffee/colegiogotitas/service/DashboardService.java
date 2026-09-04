/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.mmcoffee.colegiogotitas.service;

import javafx.collections.ObservableList;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;
import main.java.edu.mmcoffee.colegiogotitas.repository.EstudianteRepository;


public class DashboardService {
    private EstudianteRepository estudianteRepository;
 
    public DashboardService(EstudianteRepository dashboardRepository) {
        this.estudianteRepository = dashboardRepository;
    }
    public ObservableList<Estudiante> listStudent()throws Exception{
        if(estudianteRepository.findAll() == null){
            throw new RuntimeException("sin datos que mostrar");
        }else{
            return estudianteRepository.findAll();
        }
    } 

}