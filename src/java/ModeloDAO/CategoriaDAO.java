package ModeloDAO;

import Config_jvl.ClsConexion;
import Interfaces.ICategoriaDAO;
import Modelo_jvl.ClsCategoria_jvl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para las categorías de reclamos.
 */
public class CategoriaDAO implements ICategoriaDAO {

    private final ClsConexion conexion;

    public CategoriaDAO() {
        this.conexion = new ClsConexion();
    }

    @Override
    public List<ClsCategoria_jvl> listarCategorias() {
        List<ClsCategoria_jvl> categorias = new ArrayList<>();
        String sql = "SELECT idCategoria, nombreCategoria FROM categorias ORDER BY nombreCategoria";
        try (Connection con = conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClsCategoria_jvl categoria = new ClsCategoria_jvl();
                categoria.setIdCategoria(rs.getInt("idCategoria"));
                categoria.setNombreCategoria(rs.getString("nombreCategoria"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }
}
