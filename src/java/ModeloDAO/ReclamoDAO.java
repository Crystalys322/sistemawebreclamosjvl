package ModeloDAO;

import Config_jvl.ClsConexion;
import Interfaces.IReclamoDAO;
import Modelo_jvl.ClsReclamo_jvl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementa las operaciones JDBC para los reclamos.
 */
public class ReclamoDAO implements IReclamoDAO {

    private final ClsConexion conexion;

    public ReclamoDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public boolean registrarReclamo(ClsReclamo_jvl reclamo) {
        String sql = "INSERT INTO reclamos(idUsuario, idCategoria, descripcion, fechaRegistro, estado) VALUES (?,?,?,?,?)";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reclamo.getIdUsuario());
            ps.setInt(2, reclamo.getIdCategoria());
            ps.setString(3, reclamo.getDescripcion());
            Timestamp fecha = reclamo.getFechaRegistro() != null ? reclamo.getFechaRegistro() : new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(4, fecha);
            ps.setString(5, reclamo.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar reclamo: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<ClsReclamo_jvl> listarReclamosPorUsuario(int idUsuario) {
        List<ClsReclamo_jvl> reclamos = new ArrayList<>();
        String sql = "SELECT idReclamo, idUsuario, idCategoria, descripcion, fechaRegistro, estado FROM reclamos WHERE idUsuario = ? ORDER BY fechaRegistro DESC";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamo(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reclamos por usuario: " + e.getMessage());
        }
        return reclamos;
    }

    @Override
    public List<ClsReclamo_jvl> listarTodos() {
        List<ClsReclamo_jvl> reclamos = new ArrayList<>();
        String sql = "SELECT idReclamo, idUsuario, idCategoria, descripcion, fechaRegistro, estado FROM reclamos ORDER BY fechaRegistro DESC";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reclamos.add(mapearReclamo(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar todos los reclamos: " + e.getMessage());
        }
        return reclamos;
    }

    @Override
    public boolean actualizarEstadoYRegistrarSeguimiento(int idReclamo, String nuevoEstado, String observacion, int idUsuarioAtiende) {
        String sqlUpdate = "UPDATE reclamos SET estado = ? WHERE idReclamo = ?";
        String sqlSeguimiento = "INSERT INTO seguimientos(idReclamo, idUsuario, fecha, observacion, nuevoEstado) VALUES (?,?,?,?,?)";
        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                return false;
            }
            con.setAutoCommit(false);
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
                    PreparedStatement psSeguimiento = con.prepareStatement(sqlSeguimiento)) {

                psUpdate.setString(1, nuevoEstado);
                psUpdate.setInt(2, idReclamo);
                psUpdate.executeUpdate();

                psSeguimiento.setInt(1, idReclamo);
                psSeguimiento.setInt(2, idUsuarioAtiende);
                psSeguimiento.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                psSeguimiento.setString(4, observacion);
                psSeguimiento.setString(5, nuevoEstado);
                psSeguimiento.executeUpdate();

                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de reclamo: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Map<String, Integer> obtenerResumenPorEstado() {
        Map<String, Integer> resumen = new HashMap<>();
        String sql = "SELECT estado, COUNT(*) AS total FROM reclamos GROUP BY estado";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resumen.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener resumen por estado: " + e.getMessage());
        }
        return resumen;
    }

    @Override
    public Map<String, Integer> obtenerResumenPorCategoria() {
        Map<String, Integer> resumen = new HashMap<>();
        String sql = "SELECT c.nombreCategoria AS categoria, COUNT(*) AS total "
                + "FROM reclamos r INNER JOIN categorias c ON r.idCategoria = c.idCategoria GROUP BY c.nombreCategoria";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resumen.put(rs.getString("categoria"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener resumen por categoría: " + e.getMessage());
        }
        return resumen;
    }

    private ClsReclamo_jvl mapearReclamo(ResultSet rs) throws SQLException {
        ClsReclamo_jvl reclamo = new ClsReclamo_jvl();
        reclamo.setIdReclamo(rs.getInt("idReclamo"));
        reclamo.setIdUsuario(rs.getInt("idUsuario"));
        reclamo.setIdCategoria(rs.getInt("idCategoria"));
        reclamo.setDescripcion(rs.getString("descripcion"));
        reclamo.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
        reclamo.setEstado(rs.getString("estado"));
        return reclamo;
    }
}
