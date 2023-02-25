# Бот для профбюро №4 ППОСА ГУАП
## Цели
Основная цель бота - автоматизация некоторых процессов работы со студентами, 
а также выдача им некоторых фич.
## Фичи
- Авторизация через ЛК ГУАП с подтягиванием ФИО студента
- Создание временных каналов
- Прослушивание музыки
- Копирование постов из группы в ВК
## Сборка проекта
Для корректной работы необходимо задать переменные следующие переменные окружения:

```properties
discord.bot.token=
discord.new-member-role-id=
discord.registred-member-role-id=
discord.guild-id=
server.port=

app.suai.url=


spring.r2dbc.username=
spring.r2dbc.password=
spring.r2dbc.url=
spring.r2dbc.properties.schema=
spring.r2dbc.pool.enabled=true
spring.r2dbc.pool.initial-size=10
spring.r2dbc.pool.max-idle-time=1m
spring.r2dbc.pool.max-size=30
spring.data.r2dbc.repositories.enabled=true
```

Для сборки необходимо выполнить:
```shell
gradle build
```
