package Interfaces;

import Modelo_jvl.ClsReclamo_jvl;
import java.util.List;
import java.util.Map;

/**
 * Contrato para la persistencia de reclamos y sus reportes asociados.
 */
public interface IReclamoDAO {

    /**
     * Registra un reclamo con estado inicial "Pendiente".
     *
     * @param reclamo entidad a persistir
     * @return {@code true} si se insertó correctamente
     */
    boolean registrarReclamo(ClsReclamo_jvl reclamo);

    /**
     * Lista los reclamos asociados a un usuario específico.
     *
     * @param idUsuario identificador del usuario
     * @return lista de reclamos
     */
    List<ClsReclamo_jvl> listarReclamosPorUsuario(int idUsuario);

    /**
     * Lista todos los reclamos registrados (para el panel administrador).
     *
     * @return lista completa de reclamos
     */
    List<ClsReclamo_jvl> listarTodos();

    /**
     * Actualiza el estado de un reclamo y registra el seguimiento correspondiente.
     *
     * @param idReclamo identificador del reclamo
     * @param nuevoEstado estado a establecer
     * @param observacion detalle de la atención
     * @param idUsuarioAtiende identificador del usuario que atiende el reclamo
     * @return {@code true} si la operación se realizó correctamente
     */
    boolean actualizarEstadoYRegistrarSeguimiento(int idReclamo, String nuevoEstado, String observacion, int idUsuarioAtiende);

    /**
     * Obtiene un resumen de reclamos agrupados por estado.
     *
     * @return mapa estado -> cantidad
     */
    Map<String, Integer> obtenerResumenPorEstado();

    /**
     * Obtiene un resumen de reclamos agrupados por categoría.
     *
     * @return mapa categoría -> cantidad
     */
    Map<String, Integer> obtenerResumenPorCategoria();
}
