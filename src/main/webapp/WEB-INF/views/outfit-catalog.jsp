<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Каталог образов - ${genderDisplay}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="header">
    <h1>${genderDisplay}</h1>
    <div class="nav-buttons">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">На главную</a>
        <a href="${pageContext.request.contextPath}/favorites" class="btn btn-primary">Избранное</a>
        <a href="${pageContext.request.contextPath}/outfits" class="btn btn-primary">Мои образы</a>
    </div>
</header>

<div class="catalog-container">
    <c:if test="${empty outfits}">
        <div class="empty-state">
            <h2>Пока нет образов для этого мероприятия</h2>
            <p>Будьте первым, кто создаст образ!</p>
            <a href="${pageContext.request.contextPath}/outfits/create" class="btn btn-primary">Создать образ</a>
        </div>
    </c:if>

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
                <div class="outfit-actions">
                    <a href="${pageContext.request.contextPath}/view-outfit?id=${outfit.id}" class="btn btn-look">Посмотреть</a>
                    <button class="btn btn-primary" onclick="addToFavorites(${outfit.id}, this)">В избранное</button>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script>
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
    function addToFavorites(outfitId, button) {
        fetch('${pageContext.request.contextPath}/favorites/add?outfitId=' + outfitId, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === 'SUCCESS') {
                    alert('Добавлено в избранное!');
                    button.textContent = '✓ В избранном';
                    button.disabled = true;
                } else if (result === 'ALREADY_FAVORITE') {
                    alert('Уже в избранном!');
                } else {
                    alert('Ошибка: ' + result);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка при добавлении в избранное');
            });
    }
</script>
</body>
</html>