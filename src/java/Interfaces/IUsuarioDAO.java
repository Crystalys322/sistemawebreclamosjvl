package Interfaces;

import Modelo_jvl.ClsUsuario_jvl;
import java.util.List;

/**
 * Define las operaciones de acceso a datos para los usuarios del sistema.
 */
public interface IUsuarioDAO {

    /**
     * Valida las credenciales y el captcha proporcionado para iniciar sesión.
     *
     * @param correo correo electrónico del usuario
     * @param password contraseña en texto plano o hash según se almacene
     * @param captchaIngresado valor que ingresa el usuario en el formulario
     * @param captchaEsperado valor esperado (almacenado en sesión)
     * @return instancia del usuario autenticado o {@code null} si falla la validación
     */
    ClsUsuario_jvl iniciarSesion(String correo, String password, String captchaIngresado, String captchaEsperado);

    /**
     * Registra un nuevo usuario aplicando las validaciones correspondientes.
     *
     * @param usuario entidad a persistir
     * @return {@code true} si se insertó correctamente
     */
    boolean registrarUsuario(ClsUsuario_jvl usuario);

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param usuario entidad con los datos a actualizar
     * @return {@code true} si se actualizó correctamente
     */
    boolean actualizarUsuario(ClsUsuario_jvl usuario);

    /**
     * Elimina (lógicamente o físicamente) un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return {@code true} si la eliminación fue exitosa
     */
    boolean eliminarUsuario(int idUsuario);

    /**
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return colección de usuarios
     */
    List<ClsUsuario_jvl> listarUsuarios();

    /**
     * Verifica si el correo ya está siendo utilizado.
     *
     * @param correo correo a validar
     * @param idUsuarioExcluir identificador para excluir de la validación (en actualizaciones)
     * @return {@code true} si el correo está disponible
     */
    boolean correoDisponible(String correo, Integer idUsuarioExcluir);
}
