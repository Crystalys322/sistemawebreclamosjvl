package ModeloDAO;

import Config_jvl.ClsConexion;
import Interfaces.IUsuarioDAO;
import Modelo_jvl.ClsUsuario_jvl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de las operaciones de usuario.
 */
public class UsuarioDAO implements IUsuarioDAO {

    private final ClsConexion conexion;

    public UsuarioDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public ClsUsuario_jvl iniciarSesion(String correo, String password, String captchaIngresado, String captchaEsperado) {
        if (captchaEsperado == null || captchaIngresado == null) {
            return null;
        }
        if (!captchaEsperado.equalsIgnoreCase(captchaIngresado.trim())) {
            return null;
        }

        String sql = "SELECT idUsuario, nombre, correo, password, idRol, ipAutorizada "
                + "FROM usuarios WHERE correo = ? AND password = ?";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClsUsuario_jvl usuario = new ClsUsuario_jvl();
                    usuario.setIdUsuario(rs.getInt("idUsuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setIdRol(rs.getInt("idRol"));
                    usuario.setIpAutorizada(rs.getString("ipAutorizada"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean registrarUsuario(ClsUsuario_jvl usuario) {
        if (!correoDisponible(usuario.getCorreo(), null)) {
            return false;
        }
        String sql = "INSERT INTO usuarios(nombre, correo, password, idRol, ipAutorizada) VALUES (?,?,?,?,?)";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getIdRol());
            ps.setString(5, usuario.getIpAutorizada());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarUsuario(ClsUsuario_jvl usuario) {
        if (!correoDisponible(usuario.getCorreo(), usuario.getIdUsuario())) {
            return false;
        }
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, password = ?, idRol = ?, ipAutorizada = ? WHERE idUsuario = ?";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getIdRol());
            ps.setString(5, usuario.getIpAutorizada());
            ps.setInt(6, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE idUsuario = ?";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<ClsUsuario_jvl> listarUsuarios() {
        List<ClsUsuario_jvl> usuarios = new ArrayList<>();
        String sql = "SELECT idUsuario, nombre, correo, password, idRol, ipAutorizada FROM usuarios";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClsUsuario_jvl usuario = new ClsUsuario_jvl();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPassword(rs.getString("password"));
                usuario.setIdRol(rs.getInt("idRol"));
                usuario.setIpAutorizada(rs.getString("ipAutorizada"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean correoDisponible(String correo, Integer idUsuarioExcluir) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM usuarios WHERE correo = ?");
        if (idUsuarioExcluir != null) {
            sql.append(" AND idUsuario <> ?");
        }
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, correo);
            if (idUsuarioExcluir != null) {
                ps.setInt(2, idUsuarioExcluir);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar correo: " + e.getMessage());
        }
        return false;
    }
}
