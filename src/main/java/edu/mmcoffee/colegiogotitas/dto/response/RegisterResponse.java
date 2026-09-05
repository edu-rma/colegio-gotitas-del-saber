
package main.java.edu.mmcoffee.colegiogotitas.dto.response;


public class RegisterResponse {
    
    private String nombre;
    private String email;
    private String mensaje;

    public RegisterResponse() {
    }

    public RegisterResponse(String nombre, String email, String mensaje) {
        this.nombre = nombre;
        this.email = email;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

