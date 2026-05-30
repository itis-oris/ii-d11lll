<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Event Outfit - Главная</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="home-body">
<header class="header">
    <h1>Event Outfit</h1>
    <div class="nav-buttons">
        <a href="${pageContext.request.contextPath}/favorites" class="btn btn-primary">Избранное</a>
        <a href="${pageContext.request.contextPath}/outfits" class="btn btn-primary">Мои образы</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-primary">Выйти</a>
    </div>
</header>
<div class="search-form" style="text-align: center; margin: 20px;">
    <form action="${pageContext.request.contextPath}/search" method="get">
        <input type="text" name="keyword" placeholder="Поиск образов..."
               style="padding: 10px; width: 300px; border-radius: 5px; border: 1px solid #ddd;">
        <button type="submit" class="btn btn-primary">Найти</button>
    </form>
</div>
<div class="event">
    <div class="title">
        <h2>Выберите мероприятие</h2>
        <p>Подберите идеальный образ для любого события!</p>
    </div>

    <div class="event-list">
        <c:forEach var="event" items="${events}">
            <div class="event-card">
                <h3>${event.name}</h3>
                <p>${event.description != null ? event.description : ""}</p>
                <div style="margin: 15px 0;">
                    <a href="${pageContext.request.contextPath}/outfit-catalog?eventId=${event.id}&gender=FEMALE"
                       class="btn btn-look">Женские</a>
                    <a href="${pageContext.request.contextPath}/outfit-catalog?eventId=${event.id}&gender=MALE"
                       class="btn btn-look">Мужские</a>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty events}">
            <div class="empty-state">
                <h2>Нет мероприятий</h2>
                <p>Мероприятия пока не добавлены</p>
            </div>
        </c:if>
    </div>
    <c:if test="${not empty popularOutfits}">
        <hr/>
        <h2>Популярные образы (много комментариев)</h2>
        <div class="outfit-list">
            <c:forEach var="outfit" items="${popularOutfits}">
                <div class="outfit-card">
                    <h3>${outfit.name}</h3>
                    <a href="/view-outfit?id=${outfit.id}">Посмотреть</a>
                </div>
            </c:forEach>
        </div>
    </c:if>
    <c:if test="${not empty expensiveOutfits}">
        <hr/>
        <h2>Дорогие образы (>50000 ₽)</h2>
        <div class="outfit-list">
            <c:forEach var="outfit" items="${expensiveOutfits}">
                <div class="outfit-card">
                    <h3>${outfit.name}</h3>
                    <p>${outfit.price} ₽</p>
                    <a href="/view-outfit?id=${outfit.id}">Посмотреть</a>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
</body>
</html>