Проект “Обмен валют”

REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и совершать расчёт конвертации произвольных сумм из одной валюты в другую.

Описание [REST API](https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/#rest-api) и детальное ТЗ представлено в [roadmap](https://zhukovsd.github.io/java-backend-learning-course/projects/currency-exchange/) Сергея Жукова.

База данных (currency.db) располагается в корне репозитория, при локальном деплое необходимо ее разместить по адресу /var/tmp и дать права на запись в файл (в случае linux chmod 666 /vat/tmp/currency.db).

Запуск проекта осуществляется посредством запуска команды 'mvn package' в корневой папке проекта для создания war файла (/target/CurrencyExchange-1.0.war) и деплоя его в tomcat10.

Также в корне репозитория находится коллекция Postman (CurrencyExchange.postman_collection.json) для тестирования REST API.
