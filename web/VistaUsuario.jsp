<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo_jvl.ClsReclamo_jvl"%>
<%@page import="Modelo_jvl.ClsUsuario_jvl"%>
<%@page import="Modelo_jvl.ClsCategoria_jvl"%>
<%
    ClsUsuario_jvl usuarioSesion = (ClsUsuario_jvl) session.getAttribute("usuarioSesion");
    if (usuarioSesion == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<ClsReclamo_jvl> reclamos = (List<ClsReclamo_jvl>) request.getAttribute("listaReclamos");
    List<ClsCategoria_jvl> categorias = (List<ClsCategoria_jvl>) request.getAttribute("categorias");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Portal de Usuario</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" integrity="sha384-dLDL1NVr7DiIP9N6byN1Nsx3Rp3XIan+FJxuxMxDPZWS9Vyhi3F7S3Uz7NePL3wP" crossorigin="anonymous">
    </head>
    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
            <div class="container-fluid">
                <span class="navbar-brand">Mis Reclamos</span>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor" aria-controls="navbarColor" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarColor">
                    <ul class="navbar-nav ms-auto align-items-lg-center">
                        <li class="nav-item me-lg-3 text-light">
                            <i class="fas fa-user-circle me-2"></i><%= usuarioSesion.getNombre() %>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-light" href="ControladorPrincipal?accion=logout"><i class="fas fa-door-open me-2"></i>Salir</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <main class="container my-4">
            <div class="row g-4">
                <div class="col-lg-5">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white border-0">
                            <h2 class="h5 mb-0"><i class="fas fa-plus-circle text-primary me-2"></i>Registrar nuevo reclamo</h2>
                        </div>
                        <div class="card-body">
                            <% if (mensaje != null) { %>
                                <div class="alert alert-info"><%= mensaje %></div>
                            <% } %>
                            <form method="post" action="ControladorPrincipal">
                                <input type="hidden" name="accion" value="registroReclamo" />
                                <div class="mb-3">
                                    <label class="form-label">Categoría</label>
                                    <select name="idCategoria" class="form-select" required>
                                        <option value="" disabled selected>Seleccione</option>
                                        <% if (categorias != null) {
                                               for (ClsCategoria_jvl categoria : categorias) {
                                        %>
                                            <option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombreCategoria() %></option>
                                        <%
                                               }
                                           }
                                        %>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Descripción</label>
                                    <textarea name="descripcion" class="form-control" rows="4" maxlength="500" required placeholder="Describa el inconveniente"></textarea>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Guardar reclamo</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-7">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white border-0 d-flex justify-content-between align-items-center">
                            <h2 class="h5 mb-0"><i class="fas fa-list text-primary me-2"></i>Reclamos registrados</h2>
                            <a href="ControladorPrincipal?accion=misReclamos" class="btn btn-sm btn-outline-primary"><i class="fas fa-rotate"></i> Actualizar</a>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>ID</th>
                                            <th>Descripción</th>
                                            <th>Fecha</th>
                                            <th>Estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% if (reclamos != null && !reclamos.isEmpty()) {
                                               for (ClsReclamo_jvl reclamo : reclamos) {
                                                   String estado = reclamo.getEstado();
                                                   String badge = "bg-secondary";
                                                   if ("Pendiente".equalsIgnoreCase(estado)) {
                                                       badge = "bg-warning text-dark";
                                                   } else if ("En atención".equalsIgnoreCase(estado)) {
                                                       badge = "bg-info text-dark";
                                                   } else if ("Resuelto".equalsIgnoreCase(estado)) {
                                                       badge = "bg-success";
                                                   }
                                        %>
                                        <tr>
                                            <td>#<%= reclamo.getIdReclamo() %></td>
                                            <td><%= reclamo.getDescripcion() %></td>
                                            <td><%= reclamo.getFechaRegistro() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(reclamo.getFechaRegistro()) : "" %></td>
                                            <td><span class="badge <%= badge %>"><%= estado %></span></td>
                                        </tr>
                                        <%
                                               }
                                           } else {
                                        %>
                                        <tr>
                                            <td colspan="4" class="text-center py-4 text-muted">Aún no registra reclamos.</td>
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
