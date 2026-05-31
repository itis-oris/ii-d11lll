<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-register-body">
<div class="login-register-form">
    <h1>Event Outfit</h1>
    <h2>Вход в систему</h2>

    <% if (request.getAttribute("success") != null) { %>
    <div class="success"><%= request.getAttribute("success") %></div>
    <% } %>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (request.getParameter("logout") != null) { %>
    <div class="success">Вы успешно вышли из системы</div>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form-group">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" required>
        </div>

        <div class="form-group">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit" class="btn btn-primary">Войти</button>
    </form>

    <p style="text-align: center; margin-top: 20px;">
        Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Зарегистрируйтесь</a>
    </p>
</div>
</body>
</html>