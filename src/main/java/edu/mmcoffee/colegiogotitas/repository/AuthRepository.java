
package main.java.edu.mmcoffee.colegiogotitas.repository;
    
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import java.sql.SQLException;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.LoginRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.LoginResponse;
public class AuthRepository {
        //atributos
    private boolean sqlStatus = false;
    //constructor
    /* 
    los metodos : son acciones especificas
    son tareas individuales, algunos métodos 
    solo realizan una tarea, pero no retornan nada
    son "void", otros métodos, realizan tareas y retornan
    un tipo de dato primitivo o compuesto(clases).
    Divide y venceras:  un metodo debe de ser encargadi de realizar 
    unicamente una tarea especifica, el nombre de ese metodo debe de ser
    modular, directo
    */    
    
    
    public LoginResponse findUserByEmail(LoginRequest loginRequest)throws Exception{
        String sql = "SELECT nombre, apellido, contrasena_hash FROM usuarios WHERE email = ? ";
        try(PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)){
            pstm.setString(1, loginRequest.getEmail());
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                return new LoginResponse(rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("contrasena_hash"));
            }
        
            
        }catch(SQLException e){

            System.out.println("error al encontrar el EMAIL" + e.getMessage());
            
        }
        return null;
    }

    public boolean existsByEmail(String email) throws Exception{
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ? ";
        try(PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)){
            pstm.setString(1, email);
            try(ResultSet rs = pstm.executeQuery()){
                return rs.next();
            }
        }
    }

    public void save(String nombre, String apellido, String email, String contrasenaHash) throws Exception{
        String sql = "INSERT INTO usuarios (nombre, apellido, email, contrasena_hash) VALUES (?, ?, ?, ?)";
        try(PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)){
            pstm.setString(1, nombre);
            pstm.setString(2, apellido);
            pstm.setString(3, email);
            pstm.setString(4, contrasenaHash);
            pstm.executeUpdate();
        }
    }
    
    
}