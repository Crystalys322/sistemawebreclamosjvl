package ModeloDAO;

import Config_jvl.ClsConexion;
import Interfaces.IRolDAO;
import Modelo_jvl.ClsRol_jvl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para los roles del sistema.
 */
public class RolDAO implements IRolDAO {

    private final ClsConexion conexion;

    public RolDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<ClsRol_jvl> listarRoles() {
        List<ClsRol_jvl> roles = new ArrayList<>();
        String sql = "SELECT idRol, nombreRol FROM roles";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClsRol_jvl rol = new ClsRol_jvl();
                rol.setIdRol(rs.getInt("idRol"));
                rol.setNombreRol(rs.getString("nombreRol"));
                roles.add(rol);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar roles: " + e.getMessage());
        }
        return roles;
    }
}
