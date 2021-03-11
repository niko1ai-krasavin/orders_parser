## Console application for parsing files

File parsing and data conversion are performed parallel with several threads.

The app is developed using:
- Java 8
- Spring Boot
- Spring Batch
- Jackson
- Maven

### Input data for the application work

The application requires *two* files.

One file is in <code> .csv </code> format and another file is in <code> .json </code> format.

#### Example of <code> .csv </code> file entries

```
1,100,USD,оплата заказа
2,123,EUR,оплата заказа
```
Column assignment:
- order identifier
- amount
- currency
- comment

#### Example of <code> .json </code> file entries

```
{"orderId":2,"amount":1.23,"currency":"USD","comment":"оплата заказа"}
{"orderId":3,"amount":1.24,"currency":"EUR","comment":"оплата заказа"}
```

#### An example of the output of the program

```
{"id":1, "amount":100.0, "comment":"оплата заказа", "filename":"orders.csv", "line":1, "result":"OK"}
{"id":2, "amount":123.0, "comment":"оплата заказа", "filename":"orders.csv", "line":2, "result":"OK"}
{"id":3, "amount":100.0, "comment":"оплата заказа", "filename":"orders.json", "line":1, "result":"OK"}
{"id":4, "amount":123.0, "comment":"оплата заказа", "filename":"orders.json", "line":2, "result":"OK"}
```

Column assignment:
- *id* - identifier of order 
- *amount* - amount of order 
- *currency* - currency of order 
- *comment* - comment on the order
- *filename* - source file name
- *line*  - line number of the source file
- *result* - the result of parsing the source file record:
    - *OK* - if the record is converted correctly
    - *Description of the error* - if the recording failed to convert

### The application is built by the command

<code>mvn clean install</code>

### Launching the application for execution

Example of a run command:

<code>java -jar orders_parser.jar orders.csv orders.json</code>

Enter the following command, if you **do not see the letters of the Russian language** when displaying the program:

<code> ChCp 65001 </code> - required to change the encoding on the command line.

***

## Консольное приложение для парсинга входящих данных

Парсинг файлов и конвертирование данных выполняются параллельно в несколько потоков.

Приложение написано с использованием:
- Java 8
- Spring Boot
- Spring Batch
- Jackson
- Maven

### Входные данные для работы приложения

Для работы приложения необходимы *два* файла.  

Один файл в формате <code>.csv</code>, а другой файл в формате <code>.json</code>.

#### Пример записей файла <code>.csv</code>

```
1,100,USD,оплата заказа
2,123,EUR,оплата заказа
```
Назначение столбцов:
- идентификатор ордера
- сумма
- валюта
- комментарий

#### Пример записей файла <code>.json</code>

```
{"orderId":2,"amount":1.23,"currency":"USD","comment":"оплата заказа"}
{"orderId":3,"amount":1.24,"currency":"EUR","comment":"оплата заказа"}
```

#### Пример результата работы программы 

```
{"id":1, "amount":100.0, "comment":"оплата заказа", "filename":"orders.csv", "line":1, "result":"OK"}
{"id":2, "amount":123.0, "comment":"оплата заказа", "filename":"orders.csv", "line":2, "result":"OK"}
{"id":3, "amount":100.0, "comment":"оплата заказа", "filename":"orders.json", "line":1, "result":"OK"}
{"id":4, "amount":123.0, "comment":"оплата заказа", "filename":"orders.json", "line":2, "result":"OK"}
```
Назначение столбцов:
- *id* - идентификатор заказа
- *amount* - сумма заказа
- *currency* - валюта суммы заказа
- *comment* - комментарий по заказу
- *filename* - имя исходного файла
- *line* - номер строки исходного файла
- *result* - результат парсинга записи исходного файла:
  - *OK* - если запись конвертирована корректно
  - *Описание ошибки* - если запись конвертировать не удалось 

### Сборка приложения осуществляется командой

<code>mvn clean install</code>

### Запуск приложения на исполнение

Пример команды запуска: 

<code>java -jar orders_parser.jar orders.csv orders.json</code>

Если у Вас **не отображаются буквы русского языка** при выводе программы, введите следующую команду:

<code>ChCp 65001</code> - необходима для изменения кодировки в командной строке.

