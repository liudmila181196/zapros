# ЗАПРОС II, III

В приложении реализованы методы вербального анализа решений ЗАПРОС II и ЗАПРОС III.

Работа проводилась в рамках курса Инновационный менеджмент в СПбПУ под руководством А.В. Щукина и В.А. Пархоменко.

С помощью данного приложения можно:

– Загружать исходные данные в формате csv;

– Отвечать на вопросы, сформированные на основе исходных данных;

– Или дать программе ответить на вопросы случайным образом;

– На основе ответов получить парные шкалы и единую шкалу;

– Для ЗАПРОС III автоматически назначаются ранги оценкам;

– На основе построенных шкал ранжируются альтернативы;

– Сохранять полученные результаты в файл.

## Скриншоты

Главное меню:

![alt text](src\zapros\screenshot\main_menu.png "Главное меню")

Настройки:

![alt text](src\zapros\screenshot\settings.png "")

Вопрос к пользователю при выборе метода ЗАПРОС II:

![alt text](src\zapros\screenshot\question2.png "")

Вопрос к пользователю при выборе метода ЗАПРОС III:

![alt text](src\zapros\screenshot\question2.png "")

Ответы пользователя отображаются в таблице:

![alt text](src\zapros\screenshot\answers.png "")

Шкалы для ЗАПРОС II:

![alt text](src\zapros\screenshot\scale2.png "")

Шкалы для ЗАПРОС III:

![alt text](src\zapros\screenshot\scale3.png "")

Результат ранжирования альтернатив:

![alt text](src\zapros\screenshot\alternative.png "")


## Исходные данные

Исходными данными являются следующие файлы:

– criteria.txt – файл со списком id критериев и их названием через точку с
запятой (Например: 1;Новизна);

– paircriteria.txt – файл со списком пар критериев (их id) (Например: 1;2);

– assesment.txt – файл со списком оценок критериев в порядке убывания
предпочтения :id критерия, id оценки, название оценки (Например: 1;1;Новый);

– alternative.txt – файл со списком альтернатив : id альтернативы, название
альтернативы, id оценки критерия (Например, для имеющихся 3 критериев
в порядке их id : 1, 2, 3 строка, отображающая альтернативу будет иметь
вид: 1;Статья 1;1;2;1);

– question.txt – файл с вопросом к ЛПР (Например: Учитывая, что остальные
критерии имеют наилучшие оценки, какой вариант Вам нравится больше?).

## Сохранение результатов

Файлы с результатами обработки сохраняются в папку с исходными файлами:

result_answers.txt – ответы ЛПР в виде id критерия 1, id критерия 2, оценка 11, 12, 21, 22, выбранный вариант ответа

result_scales.txt – шкалы, полученные в результате обработки ответов ЛПР и (если использовался ЗАПРОС III), ранги оценок

result_alternatives.txt – ранжированные альтернативы в виде id альтернативы, название, ранг

