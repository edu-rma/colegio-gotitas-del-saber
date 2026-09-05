package main.java.edu.mmcoffee.colegiogotitas.service;

import main.java.edu.mmcoffee.colegiogotitas.dto.request.LoginRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.LoginResponse;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.RegisterRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.RegisterResponse;
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
    
   // --- NUEVO MÉTODO AGREGADO PARA EL MÓDULO DE REGISTRO ---
    public RegisterResponse registerUser(RegisterRequest registerRequest) throws Exception {
        if (registerRequest == null) {
            throw new RuntimeException("Los datos de registro no pueden estar vacíos.");
        }

        if (registerRequest.getNombre() == null || registerRequest.getNombre().trim().isEmpty() ||
            registerRequest.getApellido() == null || registerRequest.getApellido().trim().isEmpty() ||
            registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty() ||
            registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            
            throw new RuntimeException("Todos los campos son obligatorios.");
        }

        // Generar hash seguro de la contraseña
        String passwordHashed = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt());
        registerRequest.setPassword(passwordHashed);

        // Guardar usuario en BD mediante el repositorio
        return authRepository.saveUser(registerRequest);
    }
}
