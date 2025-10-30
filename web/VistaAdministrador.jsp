<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo_jvl.ClsReclamo_jvl"%>
<%@page import="Modelo_jvl.ClsUsuario_jvl"%>
<%
    ClsUsuario_jvl usuarioSesion = (ClsUsuario_jvl) session.getAttribute("usuarioSesion");
    if (usuarioSesion == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (usuarioSesion.getIdRol() != 1) {
        response.sendRedirect("ControladorPrincipal?accion=vistaUsuario");
        return;
    }
    List<ClsReclamo_jvl> reclamos = (List<ClsReclamo_jvl>) request.getAttribute("listaReclamos");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Panel de Administración</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" integrity="sha384-dLDL1NVr7DiIP9N6byN1Nsx3Rp3XIan+FJxuxMxDPZWS9Vyhi3F7S3Uz7NePL3wP" crossorigin="anonymous">
    </head>
    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="ControladorPrincipal?accion=vistaAdministrador">Administrador</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAdmin" aria-controls="navbarAdmin" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarAdmin">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link active" href="ControladorPrincipal?accion=listarReclamos">Reclamos</a></li>
                        <li class="nav-item"><a class="nav-link" href="ControladorPrincipal?accion=reportes">Reportes</a></li>
                        <li class="nav-item"><a class="nav-link" href="ControladorPrincipal?accion=listarUsuarios">Usuarios</a></li>
                    </ul>
                    <div class="d-flex align-items-center text-light">
                        <i class="fas fa-user-shield me-2"></i> <span class="me-3"><%= usuarioSesion.getNombre() %></span>
                        <a class="btn btn-outline-light" href="ControladorPrincipal?accion=logout"><i class="fas fa-door-open me-2"></i>Salir</a>
                    </div>
                </div>
            </div>
        </nav>
        <main class="container my-4">
            <div class="card shadow-sm">
                <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                    <div>
                        <h1 class="h4 mb-0">Gestión de reclamos</h1>
                        <small class="text-muted">Revise, cambie estado y deje observaciones</small>
                    </div>
                    <a href="ControladorPrincipal?accion=listarReclamos" class="btn btn-outline-primary btn-sm"><i class="fas fa-rotate"></i> Actualizar</a>
                </div>
                <div class="card-body">
                    <% if (mensaje != null) { %>
                        <div class="alert alert-info"><%= mensaje %></div>
                    <% } %>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Usuario</th>
                                    <th>Categoría</th>
                                    <th>Descripción</th>
                                    <th>Fecha</th>
                                    <th>Estado</th>
                                    <th>Observación</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (reclamos != null && !reclamos.isEmpty()) {
                                       for (ClsReclamo_jvl reclamo : reclamos) {
                                %>
                                <tr>
                                    <td>#<%= reclamo.getIdReclamo() %></td>
                                    <td><%= reclamo.getIdUsuario() %></td>
                                    <td><%= reclamo.getIdCategoria() %></td>
                                    <td><%= reclamo.getDescripcion() %></td>
                                    <td><%= reclamo.getFechaRegistro() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(reclamo.getFechaRegistro()) : "" %></td>
                                    <td>
                                        <span class="badge <%= "Resuelto".equalsIgnoreCase(reclamo.getEstado()) ? "bg-success" : ("En atención".equalsIgnoreCase(reclamo.getEstado()) ? "bg-info text-dark" : "bg-warning text-dark") %>"><%= reclamo.getEstado() %></span>
                                    </td>
                                    <td>
                                        <form class="d-flex flex-column flex-lg-row gap-2" method="post" action="ControladorPrincipal">
                                            <input type="hidden" name="accion" value="actualizarEstado" />
                                            <input type="hidden" name="idReclamo" value="<%= reclamo.getIdReclamo() %>" />
                                            <select name="estado" class="form-select form-select-sm" required>
                                                <option value="Pendiente" <%= "Pendiente".equalsIgnoreCase(reclamo.getEstado()) ? "selected" : "" %>>Pendiente</option>
                                                <option value="En atención" <%= "En atención".equalsIgnoreCase(reclamo.getEstado()) ? "selected" : "" %>>En atención</option>
                                                <option value="Resuelto" <%= "Resuelto".equalsIgnoreCase(reclamo.getEstado()) ? "selected" : "" %>>Resuelto</option>
                                            </select>
                                            <input type="text" name="observacion" class="form-control form-control-sm" placeholder="Observaciones" maxlength="200" />
                                            <div class="d-grid">
                                                <button type="submit" class="btn btn-sm btn-primary"><i class="fas fa-save me-1"></i>Guardar</button>
                                            </div>
                                        </form>
                                    </td>
                                    <td>
                                        <a class="btn btn-sm btn-outline-secondary" href="ControladorPrincipal?accion=reportes"><i class="fas fa-chart-pie"></i></a>
                                    </td>
                                </tr>
                                <%
                                       }
                                   } else {
                                %>
                                <tr>
                                    <td colspan="8" class="text-center py-4 text-muted">Sin reclamos registrados.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
