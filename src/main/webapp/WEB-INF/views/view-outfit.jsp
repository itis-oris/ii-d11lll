<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${outfit.name} - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="view-container">
    <a href="javascript:history.back()" class="back-btn">← Назад</a>

    <div class="outfit-header">
        <h1>${outfit.name}</h1>
        <div class="outfit-meta">
            <c:choose>
                <c:when test="${outfit.gender == 'FEMALE'}">Женский образ</c:when>
                <c:otherwise>Мужской образ</c:otherwise>
            </c:choose>
            &nbsp;|&nbsp;
            ${outfit.event.name}
        </div>
    </div>

    <c:if test="${not empty outfit.description}">
        <div class="outfit-description">
            <p>${outfit.description}</p>
        </div>
    </c:if>

    <c:if test="${not empty outfit.styles}">
        <div class="outfit-styles">
            <strong>Стили:</strong>
            <c:forEach var="style" items="${outfit.styles}" varStatus="status">
                <span class="style-tag">${style.name}</span>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${not empty outfit.price}">
        <div class="price-block">
            <h3>Примерная стоимость:</h3>
            <p>${priceInfo['RUB']}</p>
            <p>${priceInfo['USD']} | ${priceInfo['EUR']}</p>
            <small class="exchange-note">* Курс обновляется автоматически</small>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty outfit.images}">
            <div class="carousel">
                <div class="carousel-images" id="carouselImages">
                    <c:forEach var="image" items="${outfit.images}">
                        <div class="carousel-image">
                            <img src="${image.imageUrl}" alt="${outfit.name}"
                                 onerror="this.src='https://via.placeholder.com/600x400?text=No+Image'">
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${outfit.images.size() > 1}">
                    <button class="carousel-prev" onclick="prevSlide()">❮</button>
                    <button class="carousel-next" onclick="nextSlide()">❯</button>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-images">
                <p>Нет фотографий для этого образа</p>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="comments-section">
        <h3>Комментарии (${comments.size()})</h3>

        <c:if test="${empty comments}">
            <p class="no-comments">Пока нет комментариев. Будьте первым!</p>
        </c:if>

        <c:forEach var="comment" items="${comments}">
            <div class="comment-item">
                <div class="comment-header">
                    <strong>${comment.user.username}</strong>
                    <span class="comment-date">${comment.createdAt}</span>
                </div>
                <p>${comment.text}</p>
            </div>
        </c:forEach>

        <div class="add-comment">
            <h4>Добавить комментарий</h4>
            <form action="${pageContext.request.contextPath}/view-outfit/add-comment" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="outfitId" value="${outfit.id}">
                <textarea name="text" rows="3" placeholder="Ваш комментарий..." required></textarea>
                <button type="submit" class="btn btn-primary">Отправить</button>
            </form>
        </div>
    </div>

    <div class="action-buttons">
        <a href="${pageContext.request.contextPath}/outfits" class="btn btn-secondary">К моим образам</a>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">На главную</a>
    </div>
</div>

<script>
    let currentSlide = 0;
    const slides = document.querySelectorAll('.carousel-image');
    const totalSlides = slides.length;

    function showSlide(index) {
        if (index < 0) index = totalSlides - 1;
        if (index >= totalSlides) index = 0;
        currentSlide = index;
        const carousel = document.getElementById('carouselImages');
        carousel.scrollLeft = carousel.children[currentSlide].offsetLeft;
    }

    function nextSlide() {
        showSlide(currentSlide + 1);
    }

    function prevSlide() {
        showSlide(currentSlide - 1);
    }
</script>
</body>
</html>