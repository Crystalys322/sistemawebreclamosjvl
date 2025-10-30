package Interfaces;

import Modelo_jvl.ClsRol_jvl;
import java.util.List;

/**
 * Operaciones relacionadas con los roles del sistema.
 */
public interface IRolDAO {

    /**
     * Obtiene todos los roles disponibles para asignar a los usuarios.
     *
     * @return lista de roles
     */
    List<ClsRol_jvl> listarRoles();
}
