<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-register-body">
<div class="login-register-form">
    <h1>Event Outfit</h1>
    <h2>Регистрация</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="success"><%= request.getAttribute("success") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" required>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="password">Пароль (мин. 8 символов):</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Подтверждение пароля:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
        </div>

        <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
    </form>

    <p style="text-align: center; margin-top: 20px;">
        Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войдите</a>
    </p>
</div>
</body>
</html>