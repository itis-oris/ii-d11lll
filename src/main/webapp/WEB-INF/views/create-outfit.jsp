<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Создать образ - Event Outfit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="form-body">
<div class="form-container">
    <h1>Создать новый образ</h1>

    <c:if test="${not empty success}">
        <div class="alert alert-success" style="background: #d4edda; color: #155724; padding: 10px; margin-bottom: 20px; border-radius: 5px;">
                ${success}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" style="background: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 20px; border-radius: 5px;">
                ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/outfits/create" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group">
            <label for="name">Название образа:<span style="color: red;">*</span></label>
            <input type="text" id="name" name="name" required placeholder="Например: Вечернее платье для свадьбы">
        </div>

        <div class="form-group">
            <label for="description">Описание:</label>
            <textarea id="description" name="description" placeholder="Опишите ваш образ..."></textarea>
        </div>

        <div class="form-group">
            <label>Фотографии (можно добавить несколько):</label>
            <div id="image-urls-container">
                <div class="image-url-group">
                    <input type="url" name="imageUrls" placeholder="https://example.com/photo1.jpg" class="image-url-input">
                </div>
            </div>
            <button type="button" id="add-image-btn" class="btn btn-secondary">Добавить еще фото</button>
        </div>

        <div class="form-group">
            <label for="eventId">Мероприятие:<span style="color: red;">*</span></label>
            <select id="eventId" name="eventId" required>
                <option value="">Выберите мероприятие</option>
                <c:forEach var="event" items="${events}">
                    <option value="${event.id}">${event.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Стили:</label>
            <div class="styles-checkboxes">
                <c:forEach var="style" items="${styles}">
                    <label class="style-checkbox">
                        <input type="checkbox" name="styleIds" value="${style.id}">
                            ${style.name}
                    </label>
                </c:forEach>
            </div>
        </div>

        <div class="form-group">
            <label>Для кого:<span style="color: red;">*</span></label>
            <div>
                <label style="display: inline; margin-right: 20px;">
                    <input type="radio" name="gender" value="FEMALE" required>Женский
                </label>
                <label style="display: inline;">
                    <input type="radio" name="gender" value="MALE" required>Мужской
                </label>
            </div>
        </div>

        <div class="form-group">
            <label for="price">Цена (в RUB)</label>
            <input type="number" step="0.01" class="form-control" id="price" name="price" placeholder="Например: 5000">
        </div>

        <div class="form-buttons">
            <button type="submit" class="btn btn-primary">Сохранить образ</button>
            <a href="${pageContext.request.contextPath}/outfits" class="btn btn-secondary">Отмена</a>
        </div>
    </form>
</div>

<script>
    document.getElementById('add-image-btn').addEventListener('click', function() {
        const container = document.getElementById('image-urls-container');
        const newGroup = document.createElement('div');
        newGroup.className = 'image-url-group';
        newGroup.innerHTML = '<input type="url" name="imageUrls" placeholder="https://example.com/photo.jpg" class="image-url-input">' +
            '<button type="button" class="remove-image-btn">Удалить</button>';
        container.appendChild(newGroup);

        newGroup.querySelector('.remove-image-btn').onclick = function() {
            newGroup.remove();
        };
    });
</script>
</body>
</html>