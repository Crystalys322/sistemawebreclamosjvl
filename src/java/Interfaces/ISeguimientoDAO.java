package Interfaces;

import Modelo_jvl.ClsSeguimiento_jvl;
import java.util.List;

/**
 * Define la interacción con la tabla de seguimientos de reclamos.
 */
public interface ISeguimientoDAO {

    /**
     * Registra una nueva entrada de seguimiento.
     *
     * @param seguimiento entidad a almacenar
     * @return {@code true} si se registró correctamente
     */
    boolean registrarSeguimiento(ClsSeguimiento_jvl seguimiento);

    /**
     * Obtiene el historial de seguimientos para un reclamo específico.
     *
     * @param idReclamo identificador del reclamo
     * @return lista cronológica de seguimientos
     */
    List<ClsSeguimiento_jvl> listarPorReclamo(int idReclamo);
}
