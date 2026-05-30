<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Поиск: ${keyword} - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>Результаты поиска: "${keyword}"</h1>
    <div class="nav-buttons">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Главная</a>
        <a href="${pageContext.request.contextPath}/outfits" class="btn btn-secondary">Мои образы</a>
    </div>
</header>
<form action="${pageContext.request.contextPath}/search" method="get" style="margin: 20px; text-align: center;">
    <input type="text" name="keyword" placeholder="Новый поиск..."
           style="padding: 10px; width: 300px;">
    <button type="submit" class="btn btn-primary">Искать</button>
</form>
<c:choose>
    <c:when test="${empty results}">
        <div class="empty-state">
            <h2>Ничего не найдено :(</h2>
            <p>Попробуйте другое ключевое слово</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Вернуться на главную</a>
        </div>
    </c:when>
    <c:otherwise>
        <p>Найдено образов: ${resultCount}</p>
        <div class="outfit-list">
            <c:forEach var="outfit" items="${results}">
                <div class="outfit-card">
                    <c:if test="${not empty outfit.images and not empty outfit.images[0]}">
                        <img src="${outfit.images[0].imageUrl}" alt="${outfit.name}" class="outfit-image"
                             onerror="this.onerror=null; this.src='https://via.placeholder.com/300x200?text=No+Image'">
                    </c:if>
                    <h3>${outfit.name}</h3>
                    <p>${outfit.description != null ? outfit.description : ""}</p>
                    <p><strong>Для:</strong> ${outfit.gender == 'FEMALE' ? 'Женский' : 'Мужской'}</p>
                    <a href="${pageContext.request.contextPath}/view-outfit?id=${outfit.id}" class="btn btn-look">Посмотреть</a>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>