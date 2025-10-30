package Controlador;

import Interfaces.ICategoriaDAO;
import Interfaces.IReclamoDAO;
import Interfaces.IRolDAO;
import Interfaces.IUsuarioDAO;
import ModeloDAO.CategoriaDAO;
import ModeloDAO.ReclamoDAO;
import ModeloDAO.RolDAO;
import ModeloDAO.UsuarioDAO;
import Modelo_jvl.ClsReclamo_jvl;
import Modelo_jvl.ClsUsuario_jvl;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Controlador principal que coordina las acciones entre las vistas y la capa DAO.
 */
@WebServlet(name = "ControladorPrincipal", urlPatterns = {"/ControladorPrincipal"})
public class ControladorPrincipal extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final IUsuarioDAO usuarioDAO = new UsuarioDAO();
    private final IReclamoDAO reclamoDAO = new ReclamoDAO();
    private final IRolDAO rolDAO = new RolDAO();
    private final ICategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "loginForm";
        }
        switch (accion) {
            case "login":
                autenticar(request, response);
                break;
            case "logout":
                cerrarSesion(request, response);
                break;
            case "registroReclamo":
                registrarReclamo(request, response);
                break;
            case "misReclamos":
                mostrarReclamosUsuario(request, response);
                break;
            case "listarReclamos":
                mostrarReclamosAdministrador(request, response);
                break;
            case "actualizarEstado":
                actualizarEstadoReclamo(request, response);
                break;
            case "reportes":
                mostrarReportes(request, response);
                break;
            case "listarUsuarios":
                listarUsuarios(request, response);
                break;
            case "registrarUsuario":
                registrarUsuario(request, response);
                break;
            case "actualizarUsuario":
                actualizarUsuario(request, response);
                break;
            case "eliminarUsuario":
                eliminarUsuario(request, response);
                break;
            case "vistaUsuario":
                mostrarVistaUsuario(request, response);
                break;
            case "vistaAdministrador":
                mostrarVistaAdministrador(request, response);
                break;
            default:
                mostrarLogin(request, response);
                break;
        }
    }

    private void autenticar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String captchaIngresado = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String captchaEsperado = (String) session.getAttribute("CAPTCHA_SESSION");

        ClsUsuario_jvl usuario = usuarioDAO.iniciarSesion(correo, password, captchaIngresado, captchaEsperado);
        if (usuario != null) {
            session.setAttribute("usuarioSesion", usuario);
            session.removeAttribute("CAPTCHA_SESSION");
            if (esAdministrador(usuario)) {
                response.sendRedirect("VistaAdministrador.jsp");
            } else {
                response.sendRedirect("VistaUsuario.jsp");
            }
            return;
        }

        request.setAttribute("mensajeError", "Credenciales o captcha incorrectos");
        request.setAttribute("correo", correo);
        mostrarLogin(request, response);
    }

    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }

    private void registrarReclamo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String categoriaParam = request.getParameter("idCategoria");
        String descripcion = request.getParameter("descripcion");

        int idCategoria;
        try {
            idCategoria = Integer.parseInt(categoriaParam);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Seleccione una categoría válida.");
            mostrarReclamosUsuario(request, response);
            return;
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            request.setAttribute("mensaje", "Ingrese una descripción para el reclamo.");
            mostrarReclamosUsuario(request, response);
            return;
        }

        descripcion = descripcion.trim();

        ClsReclamo_jvl reclamo = new ClsReclamo_jvl();
        reclamo.setIdUsuario(usuarioSesion.getIdUsuario());
        reclamo.setIdCategoria(idCategoria);
        reclamo.setDescripcion(descripcion);
        reclamo.setEstado("Pendiente");
        reclamo.setFechaRegistro(new Timestamp(System.currentTimeMillis()));

        boolean registrado = reclamoDAO.registrarReclamo(reclamo);
        request.setAttribute("mensaje", registrado ? "Reclamo registrado correctamente" : "No se pudo registrar el reclamo");
        mostrarReclamosUsuario(request, response);
    }

    private void mostrarReclamosUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<ClsReclamo_jvl> reclamos = reclamoDAO.listarReclamosPorUsuario(usuarioSesion.getIdUsuario());
        request.setAttribute("listaReclamos", reclamos);
        request.setAttribute("categorias", categoriaDAO.listarCategorias());
        despachar(request, response, "VistaUsuario.jsp");
    }

    private void mostrarReclamosAdministrador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<ClsReclamo_jvl> reclamos = reclamoDAO.listarTodos();
        request.setAttribute("listaReclamos", reclamos);
        despachar(request, response, "VistaAdministrador.jsp");
    }

    private void actualizarEstadoReclamo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idReclamoParam = request.getParameter("idReclamo");
        String nuevoEstado = request.getParameter("estado");
        String observacion = request.getParameter("observacion");

        int idReclamo;
        try {
            idReclamo = Integer.parseInt(idReclamoParam);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Identificador de reclamo inválido.");
            mostrarReclamosAdministrador(request, response);
            return;
        }

        if (!esEstadoValido(nuevoEstado)) {
            request.setAttribute("mensaje", "Seleccione un estado permitido.");
            mostrarReclamosAdministrador(request, response);
            return;
        }

        if (observacion != null) {
            observacion = observacion.trim();
        }

        boolean actualizado = reclamoDAO.actualizarEstadoYRegistrarSeguimiento(idReclamo, nuevoEstado, observacion, usuarioSesion.getIdUsuario());
        request.setAttribute("mensaje", actualizado ? "Estado actualizado" : "No se pudo actualizar el estado");
        mostrarReclamosAdministrador(request, response);
    }

    private void mostrarReportes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }
        Map<String, Integer> resumenEstados = reclamoDAO.obtenerResumenPorEstado();
        Map<String, Integer> resumenCategorias = reclamoDAO.obtenerResumenPorCategoria();
        request.setAttribute("resumenEstados", resumenEstados);
        request.setAttribute("resumenCategorias", resumenCategorias);
        despachar(request, response, "Reportes.jsp");
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.setAttribute("usuarios", usuarioDAO.listarUsuarios());
        request.setAttribute("roles", rolDAO.listarRoles());
        despachar(request, response, "GestionUsuarios.jsp");
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String idRolParam = request.getParameter("idRol");
        String ipAutorizada = request.getParameter("ipAutorizada");

        if (!textoValido(nombre) || !correoValido(correo) || !passwordValido(password)) {
            request.setAttribute("mensaje", "Complete los datos obligatorios con un formato válido.");
            listarUsuarios(request, response);
            return;
        }

        int idRol;
        try {
            idRol = Integer.parseInt(idRolParam);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Seleccione un rol válido.");
            listarUsuarios(request, response);
            return;
        }

        ClsUsuario_jvl usuario = new ClsUsuario_jvl();
        usuario.setNombre(nombre.trim());
        usuario.setCorreo(correo.trim().toLowerCase());
        usuario.setPassword(password);
        usuario.setIdRol(idRol);
        usuario.setIpAutorizada(ipAutorizada != null && !ipAutorizada.trim().isEmpty() ? ipAutorizada.trim() : null);

        boolean registrado = usuarioDAO.registrarUsuario(usuario);
        request.setAttribute("mensaje", registrado ? "Usuario registrado" : "No se pudo registrar el usuario");
        listarUsuarios(request, response);
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idUsuarioParam = request.getParameter("idUsuario");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String idRolParam = request.getParameter("idRol");
        String ipAutorizada = request.getParameter("ipAutorizada");

        int idUsuario;
        int idRol;
        try {
            idUsuario = Integer.parseInt(idUsuarioParam);
            idRol = Integer.parseInt(idRolParam);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Datos numéricos inválidos para la actualización.");
            listarUsuarios(request, response);
            return;
        }

        if (!textoValido(nombre) || !correoValido(correo) || !passwordValido(password)) {
            request.setAttribute("mensaje", "Verifique la información ingresada para el usuario.");
            listarUsuarios(request, response);
            return;
        }

        ClsUsuario_jvl usuario = new ClsUsuario_jvl();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombre(nombre.trim());
        usuario.setCorreo(correo.trim().toLowerCase());
        usuario.setPassword(password);
        usuario.setIdRol(idRol);
        usuario.setIpAutorizada(ipAutorizada != null && !ipAutorizada.trim().isEmpty() ? ipAutorizada.trim() : null);

        boolean actualizado = usuarioDAO.actualizarUsuario(usuario);
        request.setAttribute("mensaje", actualizado ? "Usuario actualizado" : "No se pudo actualizar el usuario");
        listarUsuarios(request, response);
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClsUsuario_jvl usuarioSesion = obtenerUsuarioSesion(request);
        if (usuarioSesion == null || !esAdministrador(usuarioSesion)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idUsuarioParam = request.getParameter("idUsuario");
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idUsuarioParam);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "Identificador de usuario inválido.");
            listarUsuarios(request, response);
            return;
        }
        boolean eliminado = usuarioDAO.eliminarUsuario(idUsuario);
        request.setAttribute("mensaje", eliminado ? "Usuario eliminado" : "No se pudo eliminar el usuario");
        listarUsuarios(request, response);
    }

    private void mostrarVistaUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarReclamosUsuario(request, response);
    }

    private void mostrarVistaAdministrador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarReclamosAdministrador(request, response);
    }

    private void mostrarLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        despachar(request, response, "login.jsp");
    }

    private void despachar(HttpServletRequest request, HttpServletResponse response, String vista) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(vista);
        dispatcher.forward(request, response);
    }

    private ClsUsuario_jvl obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (ClsUsuario_jvl) session.getAttribute("usuarioSesion") : null;
    }

    private boolean esAdministrador(ClsUsuario_jvl usuario) {
        return usuario != null && usuario.getIdRol() == 1;
    }

    private boolean esEstadoValido(String estado) {
        return estado != null && ("Pendiente".equalsIgnoreCase(estado)
                || "En atención".equalsIgnoreCase(estado)
                || "Resuelto".equalsIgnoreCase(estado));
    }

    private boolean textoValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    private boolean passwordValido(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean correoValido(String correo) {
        return correo != null && correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
}
