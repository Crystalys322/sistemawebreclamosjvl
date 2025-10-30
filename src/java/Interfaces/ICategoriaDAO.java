package Interfaces;

import Modelo_jvl.ClsCategoria_jvl;
import java.util.List;

/**
 * Define operaciones para gestionar las categorías de reclamos.
 */
public interface ICategoriaDAO {

    /**
     * Lista todas las categorías disponibles.
     *
     * @return colección de categorías
     */
    List<ClsCategoria_jvl> listarCategorias();
}
