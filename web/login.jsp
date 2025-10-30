<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    javax.servlet.http.HttpSession sessionActual = request.getSession();
    String captchaSesion = (String) sessionActual.getAttribute("CAPTCHA_SESSION");
    if (captchaSesion == null || captchaSesion.isEmpty()) {
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder generado = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            generado.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        captchaSesion = generado.toString();
        sessionActual.setAttribute("CAPTCHA_SESSION", captchaSesion);
    }
    String mensajeError = (String) request.getAttribute("mensajeError");
    String correo = (String) request.getAttribute("correo");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Ingreso al sistema</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
        <style>
            body {
                font-family: 'Roboto', sans-serif;
                background: linear-gradient(135deg, #0f172a, #1e40af);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 1rem;
            }
            .card {
                border: none;
                border-radius: 1rem;
                box-shadow: 0 1.5rem 3rem rgba(15, 23, 42, 0.35);
            }
            .captcha-badge {
                letter-spacing: .35rem;
                font-weight: 700;
                font-size: 1.25rem;
                text-transform: uppercase;
                background: rgba(15, 23, 42, 0.85);
                border-radius: .75rem;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6 col-lg-5">
                    <div class="card p-4 p-md-5 bg-light">
                        <h1 class="h3 mb-4 text-center">Sistema de Reclamos</h1>
                        <% if (mensajeError != null) { %>
                            <div class="alert alert-danger" role="alert"><%= mensajeError %></div>
                        <% } %>
                        <form method="post" action="ControladorPrincipal">
                            <input type="hidden" name="accion" value="login"/>
                            <div class="mb-3">
                                <label class="form-label">Correo electrónico</label>
                                <input type="email" name="correo" value="<%= correo != null ? correo : "" %>" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Contraseña</label>
                                <input type="password" name="password" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label class="form-label d-flex justify-content-between align-items-center">
                                    <span>Captcha de seguridad</span>
                                    <span class="badge text-light captcha-badge"><%= captchaSesion %></span>
                                </label>
                                <input type="text" name="captcha" class="form-control" placeholder="Ingrese el texto mostrado" required />
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">Ingresar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
