<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Мои образы - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>Мои образы</h1>
    <div class="nav-buttons">
        <a href="${pageContext.request.contextPath}/outfits/create" class="btn btn-primary">Создать образ</a>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Главная</a>
        <a href="${pageContext.request.contextPath}/favorites" class="btn btn-secondary">Избранное</a>
        <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-secondary">Выйти</button>
        </form>    </div>
</header>

<c:if test="${not empty success}">
    <div class="alert alert-success" style="background: #d4edda; color: #155724; padding: 10px; margin: 10px; border-radius: 5px;">
            ${success}
    </div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger" style="background: #f8d7da; color: #721c24; padding: 10px; margin: 10px; border-radius: 5px;">
            ${error}
    </div>
</c:if>

<c:choose>
    <c:when test="${empty outfits}">
        <div class="empty-state">
            <h2>У вас пока нет образов</h2>
            <p>Создайте свой первый образ для мероприятия!</p>
            <a href="${pageContext.request.contextPath}/outfits/create" class="btn btn-primary" style="font-size: 16px; padding: 12px 24px;">
                Создать первый образ
            </a>
        </div>
    </c:when>
    <c:otherwise>
        <h2>Ваши образы (${outfits.size()})</h2>
        <div class="outfit-list">
            <c:forEach var="outfit" items="${outfits}">
                <div class="outfit-card">
                    <div class="outfit-images">
                        <c:choose>
                            <c:when test="${not empty outfit.images}">
                                <img src="${outfit.images[0].imageUrl}" alt="${outfit.name}" class="outfit-image"
                                     onerror="this.onerror=null; this.src='https://via.placeholder.com/300x200?text=No+Image'">
                            </c:when>
                            <c:otherwise>
                                <div class="image-placeholder">Нет фото</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <h3>${outfit.name}</h3>
                    <p>${outfit.description != null ? outfit.description : ""}</p>
                    <p class="outfit-meta">
                        ${outfit.event.name} &nbsp;|&nbsp;
                        <c:choose>
                            <c:when test="${outfit.gender == 'FEMALE'}">Женский</c:when>
                            <c:otherwise>Мужской</c:otherwise>
                        </c:choose>
                    </p>
                    <div class="outfit-actions">
                        <a href="${pageContext.request.contextPath}/view-outfit?id=${outfit.id}" class="btn btn-look">Просмотр</a>
                        <a href="${pageContext.request.contextPath}/outfits/edit?id=${outfit.id}" class="btn btn-edit">Редактировать</a>
                        <a href="${pageContext.request.contextPath}/outfits/delete?id=${outfit.id}" class="btn btn-danger"
                           onclick="return confirm('Удалить образ "${outfit.name}"?')">Удалить</a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>