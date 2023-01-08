# ИДЗ 2
## Амирханов Никита БПИ219

### Важно
Строчки `require` должны содержаться на новых строках.
В них должно использоваться кавычки как в примере, кроме того должны быть указано расширение файла. Пример:
`require ‘Folder 2/File 2-1.txt’`

В реализации програмы предусмотренно 2 варианта объединения файлов:
0. Объединение как в задании - "если файл А, зависит от файла В, то файл
   А находится ниже файла В в списке".
1.  Объединение через нахождение файла с наибольшим количеством зависимостей, и последующей заменой все строк `require` на содержание файлов

Одно из этих чисел нужно будет ввести по запросу `Enter number of method (See README for more details):`

### Пример работы
На данных которые были указаны в файле с заданием
```
Enter base path: D:\test
Enter result file name: out.txt
Enter number of method (See README for more details): 0
D:\test\Folder 1\File 1-1.txt => [D:\test\Folder 2\File 2-1.txt [0], ]
D:\test\Folder 2\File 2-1.txt => []
D:\test\Folder 2\File 2-2.txt => [D:\test\Folder 1\File 1-1.txt [1], D:\test\Folder 2\File 2-1.txt [0], ]
Sorted list:
D:\test\Folder 2\File 2-1.txt
D:\test\Folder 1\File 1-1.txt
D:\test\Folder 2\File 2-2.txt
------------
Creating file: D:\test\out.txt
```
Выходной файл имеет вид:
```
Phasellus eget tellus ac risus iaculis feugiat nec in eros. Aenean in luctus ante. In lacinia
lectus tempus, rutrum ipsum quis, gravida nunc. Fusce tempor eleifend libero at pharetra.
Nulla lacinia ante ac felis malesuada auctor. Vestibulum eget congue sapien, ac euismod
elit. Fusce nisl ante, consequat et imperdiet vel, semper et neque.
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse id enim euismod erat
elementum cursus. In hac habitasse platea dictumst. Etiam vitae tortor ipsum. Morbi massa
augue, lacinia sed nisl id, congue eleifend lorem.

require ‘Folder 2/File 2-1.txt’

Praesent feugiat egestas sem, id luctus lectus dignissim ac. Donec elementum rhoncus
quam, vitae viverra massa euismod a. Morbi dictum sapien sed porta tristique. Donec varius
convallis quam in fringilla.
require ‘Folder 1/File 1-1.txt’
require ‘Folder 2/File 2-1.txt’

In pretium dictum lacinia. In rutrum, neque a dignissim maximus, dolor mi pretium ante, nec
volutpat justo dolor non nulla. Vivamus nec suscipit nisl, ornare luctus erat. Aliquam eget est
orci. Proin orci urna, elementum a nunc ac, fermentum varius eros. Mauris id massa elit.

```