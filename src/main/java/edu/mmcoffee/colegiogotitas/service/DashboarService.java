
package main.java.edu.mmcoffee.colegiogotitas.service;

import javafx.collections.ObservableList;
import main.java.edu.mmcoffee.colegiogotitas.model.Estudiante;
import main.java.edu.mmcoffee.colegiogotitas.repository.EstudianteRepository;

public class DashboarService {
    
      private EstudianteRepository estudianteRepository;
 
    public DashboarService(EstudianteRepository dashboardRepository) {
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
    

