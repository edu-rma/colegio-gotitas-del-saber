package main.java.edu.mmcoffee.colegiogotitas.service;

import main.java.edu.mmcoffee.colegiogotitas.dto.request.LoginRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.LoginResponse;
import main.java.edu.mmcoffee.colegiogotitas.repository.AuthRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    
    private final AuthRepository authRepository;
    private boolean status = false;
    
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
       
    public LoginResponse login(LoginRequest loginRequest)throws Exception{
      if(loginRequest == null){
          throw new RuntimeException("Credencailes vacias.");
      }else if(loginRequest.getEmail() == null || loginRequest.getPassword() == null){
          throw new RuntimeException("El correo o contraseña no puede estar vacio");
      }
      LoginResponse response = authRepository.findUserByEmail(loginRequest);
     
      String contrasenaHashed = response.getContrasena_hash();
      
      if(contrasenaHashed == null){
          throw new RuntimeException("Contraseña invalida.");
      }else{
        if(BCrypt.checkpw(loginRequest.getPassword(), contrasenaHashed)){
            return response;
        }
      }

      return null;
    }
}
