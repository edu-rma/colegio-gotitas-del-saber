package main.java.edu.mmcoffee.colegiogotitas.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.edu.mmcoffee.colegiogotitas.config.DataBaseConnection;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.LoginRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.request.RegistroRequest;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.LoginResponse;
import main.java.edu.mmcoffee.colegiogotitas.dto.response.RegistroResponse;

public class AuthRepository {

    public AuthRepository() {
    }

    public LoginResponse findUserByEmail(LoginRequest loginRequest) throws Exception {
        String sql = "SELECT nombre, apellido, contrasena_hash FROM usuarios WHERE email = ?";

        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, loginRequest.getEmail());
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new LoginResponse(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("contrasena_hash")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar el EMAIL: " + e.getMessage());
        }
        return null;
    }

    public boolean existsByEmail(String email) throws Exception {
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";

        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, email);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        }
    }

    public RegistroResponse saveUser(RegistroRequest registerRequest) throws Exception {
        String sql = "INSERT INTO usuarios (nombre, apellido, email, contrasena_hash) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, registerRequest.getNombre());
            pstmt.setString(2, registerRequest.getApellido());
            pstmt.setString(3, registerRequest.getEmail());
            pstmt.setString(4, registerRequest.getPassword());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                return new RegistroResponse(
                    registerRequest.getNombre(),
                    registerRequest.getEmail(),
                    "Usuario registrado correctamente"
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar en la BD: " + e.getMessage(), e);
        }
        return null;
    }
}