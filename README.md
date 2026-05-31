# Event Outfit - Сервис подбора образов для мероприятий
Выполнила: Далила Мостюкова
Группа: 11-403

## О проекте

Event Outfit — это веб-приложение для подбора и создания образов для различных мероприятий (свадьба, выпускной, деловая встреча и др.). Пользователи могут создавать образы, добавлять их в избранное, оставлять комментарии, а также просматривать каталог образов других пользователей.

## Технологии
- Spring Boot 3.2.0 - основа приложения
- Spring MVC - архитектура приложения (MPA)
- Spring Data JPA - работа с базой данных
- Spring Security - аутентификация и авторизация
- PostgreSQL - реляционная база данных
- JSP + JSTL - шаблонизатор (представление)
- Maven - сборка проекта
- Docker - контейнеризация
- REST API - JSON-эндпоинты
- Swagger/OpenAPI - документация REST API
- OkHttp - HTTP-клиент для стороннего API
- Log4j2 - логирование

## Функциональные возможности

### Для всех пользователей
- Регистрация и вход в систему

### Для авторизованных пользователей
- Просмотр каталога образов по мероприятиям и полу
- Поиск образов по ключевому слову
- Создание, редактирование и удаление своих образов
- Добавление/удаление образов в избранное (AJAX)
- Просмотр избранных образов
- Добавление комментариев к образам

### REST API
- CRUD операции для образов (`/api/outfits`)
- Получение/управление избранным (`/api/favorites`)
- Конвертация цен в валюты (`/api/currency`)

## Запуск проекта

### Требования
- Java 17+
- PostgreSQL 15+
- Maven 3.8+
- Docker (опционально)

### Локальный запуск

1. Создайте базу данных PostgreSQL
CREATE DATABASE event_outfit_db;

2. Настройте application.properties
- spring.datasource.url - jdbc:postgresql://localhost:5432/event_outfit_db
- spring.datasource.username=postgres
- spring.datasource.password=ваш_пароль

3. Соберите и запустите проект
- mvn clean package
- java -jar target/event-outfit-1.0.0.jar
- Откройте в браузере http://localhost:8080

### Запуск через Docker
- docker-compose up -d
- Приложение будет доступно по адресу http://localhost:8080

### Структура проекта
- config/ - Конфигурации (Security, Cache)
- controller/ - Контроллеры (MVC + REST)
- converter/ - Конвертеры Entity ↔ DTO
- dto/ - Data Transfer Objects
- model/ - JPA сущности
- repository/ - Spring Data JPA репозитории
- service/ - Бизнес-логика
- util/ - Утилиты (ApiClient)

- src/main/resources/
    - application.properties
    - log4j2.xml

- src/main/webapp/WEB-INF/views/ - JSP страницы

### База данных
Сущности
- User users Пользователи
- Outfit outfits Образы
- Event events Мероприятия
- OutfitImage outfit_images Фотографии образов
- Comment comments Комментарии
- OutfitStyle outfit_styles Стили образов

Связи
- M2M: User ↔ Outfit (избранное), Outfit ↔ OutfitStyle (стили)
- O2M: Outfit → Comment, Outfit → OutfitImage

### REST API

Базовый URL http://localhost:8080/api

Эндпоинты
- GET	/outfits	Получить все образы
- GET	/outfits/{id}	Получить образ по ID
- GET	/outfits/search?keyword=	Поиск по ключевому слову
- GET	/outfits/popular	Популярные образы (подзапрос)
- POST	/outfits	Создать образ
- PUT	/outfits/{id}	Обновить образ
- DELETE	/outfits/{id}	Удалить образ
- GET	/currency/rates	Курсы валют
- GET	/currency/convert?outfitId=&currency=	Конвертация цены
- GET	/favorites/{userId}	Избранное пользователя
- POST	/favorites/{userId}/{outfitId}	Добавить в избранное
- DELETE	/favorites/{userId}/{outfitId}	Удалить из избранного

### Документация Swagger
http://localhost:8080/swagger-ui.html

### Тестирование
- HTTP-тесты (REST API)
- В корне проекта есть файл api-tests.http для IntelliJ IDEA.

### Docker
Образы
- eventoutfit_app - Spring Boot приложение
- eventoutfit_db - PostgreSQL

Переменные окружения
- SPRING_DATASOURCE_URL	jdbc:postgresql://db:5432/event_outfit_db
- SPRING_DATASOURCE_USERNAME - postgres
- SPRING_DATASOURCE_PASSWORD - postgres

### Дополнительные фичи
- Кэширование - @Cacheable для мероприятий
- Criteria Builder - динамические запросы
- Подзапрос - поиск образов с >2 комментариями
- AJAX - добавление/удаление из избранного без перезагрузки
- Стороннее API - курсы валют (exchangerate-api.com)
- CSRF защита - токены во всех формах и AJAX
