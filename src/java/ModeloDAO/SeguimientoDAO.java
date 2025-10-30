package ModeloDAO;

import Config_jvl.ClsConexion;
import Interfaces.ISeguimientoDAO;
import Modelo_jvl.ClsSeguimiento_jvl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones de persistencia para los seguimientos de reclamos.
 */
public class SeguimientoDAO implements ISeguimientoDAO {

    private final ClsConexion conexion;

    public SeguimientoDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public boolean registrarSeguimiento(ClsSeguimiento_jvl seguimiento) {
        String sql = "INSERT INTO seguimientos(idReclamo, idUsuario, fecha, observacion, nuevoEstado) VALUES (?,?,?,?,?)";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, seguimiento.getIdReclamo());
            ps.setInt(2, seguimiento.getIdUsuario());
            Timestamp fecha = seguimiento.getFecha() != null ? seguimiento.getFecha() : new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(3, fecha);
            ps.setString(4, seguimiento.getObservacion());
            ps.setString(5, seguimiento.getNuevoEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar seguimiento: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<ClsSeguimiento_jvl> listarPorReclamo(int idReclamo) {
        List<ClsSeguimiento_jvl> seguimientos = new ArrayList<>();
        String sql = "SELECT idSeguimiento, idReclamo, idUsuario, fecha, observacion, nuevoEstado FROM seguimientos WHERE idReclamo = ? ORDER BY fecha DESC";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReclamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClsSeguimiento_jvl seguimiento = new ClsSeguimiento_jvl();
                    seguimiento.setIdSeguimiento(rs.getInt("idSeguimiento"));
                    seguimiento.setIdReclamo(rs.getInt("idReclamo"));
                    seguimiento.setIdUsuario(rs.getInt("idUsuario"));
                    seguimiento.setFecha(rs.getTimestamp("fecha"));
                    seguimiento.setObservacion(rs.getString("observacion"));
                    seguimiento.setNuevoEstado(rs.getString("nuevoEstado"));
                    seguimientos.add(seguimiento);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar seguimientos: " + e.getMessage());
        }
        return seguimientos;
    }
}
