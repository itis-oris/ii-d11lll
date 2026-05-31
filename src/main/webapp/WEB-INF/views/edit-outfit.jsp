<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Редактировать образ - ${outfit.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="form-body">
<div class="form-container">
    <h1>Редактировать образ</h1>

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


    <form action="${pageContext.request.contextPath}/outfits/edit" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="id" value="${outfit.id}">

        <div class="form-group">
            <label for="name">Название образа:<span style="color: red;">*</span></label>
            <input type="text" id="name" name="name" value="${outfit.name}" required>
        </div>

        <div class="form-group">
            <label for="description">Описание:</label>
            <textarea id="description" name="description">${outfit.description != null ? outfit.description : ''}</textarea>
        </div>

        <div class="form-group">
            <label>Текущие фото:</label>
            <div class="current-images">
                <c:forEach var="image" items="${outfit.images}">
                    <div class="image-preview" data-image-id="${image.id}">
                        <img src="${image.imageUrl}" alt="Фото">
                        <button type="button" class="remove-image-btn" onclick="removeImage(${image.id}, this)">✖️</button>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="form-group">
            <label>Добавить новые фото:</label>
            <div id="new-images-container">
                <div class="image-url-group">
                    <input type="url" name="imageUrls" placeholder="https://example.com/new-photo.jpg" class="image-url-input">
                </div>
            </div>
            <button type="button" id="add-image-btn" class="btn btn-secondary">Добавить еще фото</button>
        </div>

        <div class="form-group">
            <label for="eventId">Мероприятие:<span style="color: red;">*</span></label>
            <select id="eventId" name="eventId" required>
                <c:forEach var="event" items="${events}">
                    <option value="${event.id}" ${outfit.event.id == event.id ? 'selected' : ''}>${event.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Стили:</label>
            <div class="styles-checkboxes">
                <c:forEach var="style" items="${styles}">
                    <label class="style-checkbox">
                        <input type="checkbox" name="styleIds" value="${style.id}"
                        <c:forEach var="outfitStyle" items="${outfit.styles}">
                               <c:if test="${outfitStyle.id == style.id}">checked</c:if>
                        </c:forEach>>
                            ${style.name}
                    </label>
                </c:forEach>
            </div>
        </div>

        <div class="form-group">
            <label>Для кого:<span style="color: red;">*</span></label>
            <div>
                <label style="display: inline; margin-right: 20px;">
                    <input type="radio" name="gender" value="FEMALE" ${outfit.gender == 'FEMALE' ? 'checked' : ''}> Женский
                </label>
                <label style="display: inline;">
                    <input type="radio" name="gender" value="MALE" ${outfit.gender == 'MALE' ? 'checked' : ''}> Мужской
                </label>
            </div>
        </div>

        <div class="form-buttons">
            <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            <a href="${pageContext.request.contextPath}/outfits" class="btn btn-secondary">Отмена</a>
            <a href="${pageContext.request.contextPath}/outfits/delete?id=${outfit.id}" class="btn btn-danger"
               onclick="return confirm('Удалить образ?')">Удалить образ</a>
        </div>
    </form>
</div>

<script>
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    function removeImage(imageId, button) {
        if (!confirm('Удалить это фото?')) return;

        fetch('${pageContext.request.contextPath}/outfits/deleteImage?imageId=' + imageId, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === 'success') {
                    button.closest('.image-preview').remove();
                    alert('Фото удалено');
                } else {
                    alert('Ошибка при удалении');
                }
            });
    }

    document.getElementById('add-image-btn').addEventListener('click', function() {
        const container = document.getElementById('new-images-container');
        const newGroup = document.createElement('div');
        newGroup.className = 'image-url-group';
        newGroup.innerHTML = '<input type="url" name="imageUrls" placeholder="https://example.com/photo.jpg" class="image-url-input">' +
            '<button type="button" class="remove-image-btn">✖Удалить</button>';
        container.appendChild(newGroup);

        newGroup.querySelector('.remove-image-btn').onclick = function() {
            newGroup.remove();
        };
    });
</script>
</body>
</html>