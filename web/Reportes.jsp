<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="Modelo_jvl.ClsUsuario_jvl"%>
<%
    ClsUsuario_jvl usuarioSesion = (ClsUsuario_jvl) session.getAttribute("usuarioSesion");
    if (usuarioSesion == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 🔹 Versión de Git (Upstream)
    if (usuarioSesion.getIdRol() != 1) {
        response.sendRedirect("ControladorPrincipal?accion=vistaUsuario");
        return;
    }

    Map<String, Integer> resumenEstados = (Map<String, Integer>) request.getAttribute("resumenEstados");
    Map<String, Integer> resumenCategorias = (Map<String, Integer>) request.getAttribute("resumenCategorias");
    if (resumenEstados == null) {
        resumenEstados = new LinkedHashMap<String, Integer>();
    }
    if (resumenCategorias == null) {
        resumenCategorias = new LinkedHashMap<String, Integer>();
    }

    StringBuilder estadosLabels = new StringBuilder("[");
    StringBuilder estadosValues = new StringBuilder("[");
    int index = 0;
    for (Map.Entry<String, Integer> entry : resumenEstados.entrySet()) {
        if (index > 0) {
            estadosLabels.append(",");
            estadosValues.append(",");
        }
        estadosLabels.append("'" + entry.getKey().replace("'", "\\'") + "'");
        estadosValues.append(entry.getValue());
        index++;
    }
    estadosLabels.append("]");
    estadosValues.append("]");

    StringBuilder categoriasLabels = new StringBuilder("[");
    StringBuilder categoriasValues = new StringBuilder("[");
    index = 0;
    for (Map.Entry<String, Integer> entry : resumenCategorias.entrySet()) {
        if (index > 0) {
            categoriasLabels.append(",");
            categoriasValues.append(",");
        }
        categoriasLabels.append("'" + entry.getKey().replace("'", "\\'") + "'");
        categoriasValues.append(entry.getValue());
        index++;
    }
    categoriasLabels.append("]");
    categoriasValues.append("]");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Reportes de Reclamos</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" integrity="sha384-dLDL1NVr7DiIP9N6byN1Nsx3Rp3XIan+FJxuxMxDPZWS9Vyhi3F7S3Uz7NePL3wP" crossorigin="anonymous">
        <style>
            .chart-card {
                min-height: 420px;
            }
        </style>
    </head>
    <body class="bg-light">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="ControladorPrincipal?accion=vistaAdministrador">Administrador</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarReportes" aria-controls="navbarReportes" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarReportes">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link" href="ControladorPrincipal?accion=listarReclamos">Reclamos</a></li>
                        <li class="nav-item"><a class="nav-link active" href="ControladorPrincipal?accion=reportes">Reportes</a></li>
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
            <div class="row g-4">
                <div class="col-lg-6">
                    <div class="card shadow-sm chart-card">
                        <div class="card-header bg-white border-0">
                            <h2 class="h5 mb-0"><i class="fas fa-chart-pie text-primary me-2"></i>Reclamos por estado</h2>
                        </div>
                        <div class="card-body">
                            <canvas id="chartEstados"></canvas>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card shadow-sm chart-card">
                        <div class="card-header bg-white border-0">
                            <h2 class="h5 mb-0"><i class="fas fa-layer-group text-primary me-2"></i>Reclamos por categoría</h2>
                        </div>
                        <div class="card-body">
                            <canvas id="chartCategorias"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card shadow-sm mt-4">
                <div class="card-header bg-white border-0">
                    <h2 class="h5 mb-0">Detalle resumido</h2>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h3 class="h6 text-muted">Estados</h3>
                            <ul class="list-group list-group-flush">
                                <% for (Map.Entry<String, Integer> entry : resumenEstados.entrySet()) { %>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span><i class="fas fa-circle text-primary me-2"></i><%= entry.getKey() %></span>
                                        <span class="badge bg-primary rounded-pill"><%= entry.getValue() %></span>
                                    </li>
                                <% } %>
                            </ul>
                        </div>
                        <div class="col-md-6">
                            <h3 class="h6 text-muted">Categorías</h3>
                            <ul class="list-group list-group-flush">
                                <% for (Map.Entry<String, Integer> entry : resumenCategorias.entrySet()) { %>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span><i class="fas fa-tag text-success me-2"></i><%= entry.getKey() %></span>
                                        <span class="badge bg-success rounded-pill"><%= entry.getValue() %></span>
                                    </li>
                                <% } %>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.2/dist/chart.umd.min.js" integrity="sha384-8GZm4G6dkprdFM5lTyBC0bYKT1eZqUPVHtV6nRvWmvMRGsiE9zraFMvx6bMpiKFF" crossorigin="anonymous"></script>
        <script>
            const colors = ['#1d4ed8', '#f97316', '#16a34a', '#6b21a8', '#0891b2', '#facc15'];
            const ctxEstados = document.getElementById('chartEstados');
            new Chart(ctxEstados, {
                type: 'doughnut',
                data: {
                    labels: <%= estadosLabels.toString() %>,
                    datasets: [{
                            data: <%= estadosValues.toString() %>,
                            backgroundColor: colors,
                            borderWidth: 1
                        }]
                },
                options: {
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });
            const ctxCategorias = document.getElementById('chartCategorias');
            new Chart(ctxCategorias, {
                type: 'bar',
                data: {
                    labels: <%= categoriasLabels.toString() %>,
                    datasets: [{
                            data: <%= categoriasValues.toString() %>,
                            backgroundColor: '#1d4ed8'
                        }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                precision: 0
                            }
                        }
                    }
                }
            });
        </script>
    </body>
</html>
