
package main.java.edu.mmcoffee.colegiogotitas.repository;
    
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import java.sql.SQLException;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.LoginRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.LoginResponse;
public class AuthRepository {
    //atributos
    //buena practica: asumir que todo va a fallar
    private boolean sqlStatus = false;
    //contructor
    /*los metodos son acciones especificas, son tareas individuales
    algunos metodos realizan alguna tara pero no retornan nada, son void,
    otros realiazan tareas y retornan un dato primitivo o compuesto
    un metodo debe de ser el unico que realiza una tarea especifica
    el nombre de ese metodo debe de ser modular, directo
*/
    public LoginResponse findUserByEmail(LoginRequest loginRequest) throws Exception{
       String sql = "select d.nombre, d.apellido, u.contrasena_hash from usuarios as u" +
" right join docentes as d" +
" on d.id_docente = u.id_docente" +
" where email = ? ";
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
    
    public AuthRepository(){
        
    }
    
    
    
}
