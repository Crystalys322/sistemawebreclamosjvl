<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo_jvl.ClsUsuario_jvl"%>
<%@page import="Modelo_jvl.ClsRol_jvl"%>
<%
    ClsUsuario_jvl usuarioSesion = (ClsUsuario_jvl) session.getAttribute("usuarioSesion");
    if (usuarioSesion == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<ClsUsuario_jvl> usuarios = (List<ClsUsuario_jvl>) request.getAttribute("usuarios");
    List<ClsRol_jvl> roles = (List<ClsRol_jvl>) request.getAttribute("roles");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Gestión de Usuarios</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" integrity="sha384-dLDL1NVr7DiIP9N6byN1Nsx3Rp3XIan+FJxuxMxDPZWS9Vyhi3F7S3Uz7NePL3wP" crossorigin="anonymous">
    </head>
    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="ControladorPrincipal?accion=vistaAdministrador">Administrador</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarUsuarios" aria-controls="navbarUsuarios" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarUsuarios">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link" href="ControladorPrincipal?accion=listarReclamos">Reclamos</a></li>
                        <li class="nav-item"><a class="nav-link" href="ControladorPrincipal?accion=reportes">Reportes</a></li>
                        <li class="nav-item"><a class="nav-link active" href="ControladorPrincipal?accion=listarUsuarios">Usuarios</a></li>
                    </ul>
                    <div class="d-flex align-items-center text-light">
                        <i class="fas fa-user-shield me-2"></i> <span class="me-3"><%= usuarioSesion.getNombre() %></span>
                        <a class="btn btn-outline-light" href="ControladorPrincipal?accion=logout"><i class="fas fa-door-open me-2"></i>Salir</a>
                    </div>
                </div>
            </div>
        </nav>
        <main class="container my-4">
            <div class="row g-4">
                <div class="col-lg-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white border-0">
                            <h2 class="h5 mb-0"><i class="fas fa-user-plus text-primary me-2"></i>Registrar usuario</h2>
                        </div>
                        <div class="card-body">
                            <% if (mensaje != null) { %>
                                <div class="alert alert-info"><%= mensaje %></div>
                            <% } %>
                            <form method="post" action="ControladorPrincipal">
                                <input type="hidden" name="accion" value="registrarUsuario" />
                                <div class="mb-3">
                                    <label class="form-label">Nombre completo</label>
                                    <input type="text" name="nombre" class="form-control" required maxlength="120" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Correo electrónico</label>
                                    <input type="email" name="correo" class="form-control" required maxlength="120" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Contraseña</label>
                                    <input type="password" name="password" class="form-control" required minlength="6" />
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Rol</label>
                                    <select name="idRol" class="form-select" required>
                                        <option value="" disabled selected>Seleccione</option>
                                        <% if (roles != null) {
                                               for (ClsRol_jvl rol : roles) {
                                        %>
                                            <option value="<%= rol.getIdRol() %>"><%= rol.getNombreRol() %></option>
                                        <%
                                               }
                                           }
                                        %>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">IP autorizada</label>
                                    <input type="text" name="ipAutorizada" class="form-control" placeholder="Opcional" maxlength="45" />
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Registrar</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-8">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                            <h2 class="h5 mb-0"><i class="fas fa-users text-primary me-2"></i>Usuarios registrados</h2>
                            <a href="ControladorPrincipal?accion=listarUsuarios" class="btn btn-outline-primary btn-sm"><i class="fas fa-rotate"></i> Actualizar</a>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Correo</th>
                                            <th>Rol</th>
                                            <th>IP autorizada</th>
                                            <th class="text-center">Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% if (usuarios != null && !usuarios.isEmpty()) {
                                               for (ClsUsuario_jvl usuario : usuarios) {
                                                   String formUpdateId = "updateForm" + usuario.getIdUsuario();
                                                   String formDeleteId = "deleteForm" + usuario.getIdUsuario();
                                        %>
                                        <tr>
                                            <td>#<%= usuario.getIdUsuario() %></td>
                                            <td><input form="<%= formUpdateId %>" type="text" name="nombre" value="<%= usuario.getNombre() %>" class="form-control form-control-sm" required /></td>
                                            <td><input form="<%= formUpdateId %>" type="email" name="correo" value="<%= usuario.getCorreo() %>" class="form-control form-control-sm" required /></td>
                                            <td>
                                                <select form="<%= formUpdateId %>" name="idRol" class="form-select form-select-sm" required>
                                                    <% if (roles != null) {
                                                           for (ClsRol_jvl rol : roles) {
                                                    %>
                                                        <option value="<%= rol.getIdRol() %>" <%= usuario.getIdRol() == rol.getIdRol() ? "selected" : "" %>><%= rol.getNombreRol() %></option>
                                                    <%
                                                           }
                                                       }
                                                    %>
                                                </select>
                                            </td>
                                            <td><input form="<%= formUpdateId %>" type="text" name="ipAutorizada" value="<%= usuario.getIpAutorizada() != null ? usuario.getIpAutorizada() : "" %>" class="form-control form-control-sm" /></td>
                                            <td class="text-center">
                                                <div class="btn-group" role="group">
                                                    <button form="<%= formUpdateId %>" type="submit" class="btn btn-sm btn-success"><i class="fas fa-save"></i></button>
                                                    <button form="<%= formDeleteId %>" type="submit" class="btn btn-sm btn-danger" onclick="return confirm('¿Desea eliminar este usuario?');"><i class="fas fa-trash"></i></button>
                                                </div>
                                            </td>
                                        </tr>
                                        <form id="<%= formUpdateId %>" method="post" action="ControladorPrincipal" class="d-none">
                                            <input type="hidden" name="accion" value="actualizarUsuario" />
                                            <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>" />
                                            <input type="hidden" name="password" value="<%= usuario.getPassword() != null ? usuario.getPassword() : "" %>" />
                                        </form>
                                        <form id="<%= formDeleteId %>" method="post" action="ControladorPrincipal" class="d-none">
                                            <input type="hidden" name="accion" value="eliminarUsuario" />
                                            <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>" />
                                        </form>
                                        <%
                                               }
                                           } else {
                                        %>
                                        <tr>
                                            <td colspan="6" class="text-center py-4 text-muted">No hay usuarios registrados.</td>
                                        </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
