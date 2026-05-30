<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="error-container">
    <h1>Что-то пошло не так</h1>

    <c:if test="${not empty message}">
        <p class="error-message">${message}</p>
    </c:if>

    <c:if test="${empty message}">
        <p class="error-message">Произошла непредвиденная ошибка</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Вернуться на главную</a>
</div>
</body>
</html>