<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Избранное - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>Мои избранные образы</h1>
    <div class="nav-buttons">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Главная</a>
        <a href="${pageContext.request.contextPath}/outfits" class="btn btn-secondary">Мои образы</a>
        <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-secondary">Выйти</button>
        </form>    </div>
</header>

<c:choose>
    <c:when test="${empty favorites}">
        <div class="empty-state">
            <h2>У вас пока нет избранных образов</h2>
            <p>Добавляйте понравившиеся образы в избранное, чтобы вернуться к ним позже!</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Перейти к каталогу</a>
        </div>
    </c:when>
    <c:otherwise>
        <h2>Ваши избранные образы (${favorites.size()})</h2>
        <div class="outfit-list">
            <c:forEach var="outfit" items="${favorites}">
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
                    <p class="outfit-meta">${outfit.event.name}</p>
                    <div class="outfit-actions">
                        <a href="${pageContext.request.contextPath}/view-outfit?id=${outfit.id}" class="btn btn-look">Просмотр</a>
                        <button class="btn btn-danger" onclick="removeFromFavorites(${outfit.id}, this)">Удалить</button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<script>
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    function removeFromFavorites(outfitId, button) {
        if (!confirm('Удалить этот образ из избранного?')) return;

        fetch('${pageContext.request.contextPath}/favorites/remove?outfitId=' + outfitId, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === 'SUCCESS') {
                    button.closest('.outfit-card').remove();
                    alert('Удалено из избранного');
                    if (document.querySelectorAll('.outfit-card').length === 0) {
                        location.reload();
                    }
                } else {
                    alert('Ошибка при удалении');
                }
            });
    }
</script>
</body>
</html>