#Хранение и передача данных в Android приложении.

##1) Shared Preferences

Набор пар ключ-значение, которые можно получить в любом месте из приложения.

Создаем глобальную переменную
```java  
SharedPreferences sPref;
````
  
Получаем преферны:
```java  
  sPref = getSharedPreferences("filename-for-preferences",Contex.MODE_PRIVATE);
```
Редактирование:
```java  
  Editor ed = sPref.edit();
  //ИЛИ
  SharedPrefernces.Editor ed = sPref.edit();
```
Добавление элемента:
```java  
  ed.putString("key", "value"); //также putInt , putFloat и т.д.
  ed.commit(); //закрываем редактор
 ```
Получение элемента по ключу
  Здесь редактор НЕ нужен
 ```java  
`  String some_value = sPref.getString("key", "default_value");  //если не получилось вернуть значение по ключу или ключ не найден   - возвращается default_value`
  ```
  
  SharedPreferences хранятся в файле: `/data/data/имя_пакета/shared_prefs/имя_файла_настроек.xml.`
  Можно отловить первый запуск приложения.
  
Проверка наличия ключа
```java   
Boolean b = sPref.contains("key2");
```

Ключ - всегда строка
  
Работа с наборами ( <set> )

```java
// Добавление элемента
public void onPutSettings(View v){
	Set<String> names = new HashSet<String>();
	names.add("Василий");
	names.add("Михаил");
	names.add("Руслан");
	Editor ed = sPref.edit();
	ed.putStringSet("strSetKey", names);
	ed.apply();  //почти то же самое что commit, но НЕ возвращает ничего и работает быстрее
}

// Получение списка
public void onShowSettings(View v)
{
 	Set<String> ret = sp.getStringSet("strSetKey", new HashSet<String>());
	for(String r : ret) {
	    Log.i("Share", "Имя: " + r);
	}
} 
```
  
Можно создавать отдельные преференции для каждой активити. Тогда метод `getSharedPreferences` сменяется на `getPreferences`

```java
protected void saveActivityPreferences() {
    // Создайте или извлеките объект настроек активности.
    SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
    // Извлеките редактор, чтобы изменить Общие настройки.
    SharedPreferences.Editor editor = activityPreferences.edit();
    // Извлеките представление.
    TextView myTextView = (TextView)findViewById(R.id.myTextView);
    // Запишите новые значения примитивных типов в объект Общих настроек.
    editor.putString("currentTextValue", myTextView.getText().toString());
    // Сохраните изменения.
    editor.commit();
}
```

###Ссылки:
http://developer.alexanderklimov.ru/android/theory/sharedpreferences.php

http://startandroid.ru/ru/uroki/vse-uroki-spiskom/73-urok-33-hranenie-dannyh-preferences

http://developer.android.com/reference/android/content/SharedPreferences.html

##2) SQLite + SQL

SQLite - реляционная СУБД (Система Управления Баз Данных)
Базой данных (БД) называется организованная в соответствии с определенными правилами и поддерживаемая в памяти компьютера совокупность сведений об объектах, процессах, событиях или явлениях, относящихся к некоторой предметной области, теме или задаче. Она организована таким образом, чтобы обеспечить информационные потребности пользователей, а также удобное хранение этой совокупности данных, как в целом, так и любой ее части.

Реляционная БД состоит из таблиц (как Excel), где строки - отдельные записи, столбцы - признаки (id, имя, возраст, ... )

SQL - язык запросов к реляционной БД. 

Простейший синтаксис:
```sql
select name,age --столбцы
from people --имя таблицы
where age > 18 and name <> 'Василий' --условия
order by name --сортируем по имени
limit 10 --выводим 10 записе
```
Можно объединять таблицы с помощью `join`
В различных СУБД может быть слегка разный синтаксис.

###Ссылки
[Синтаксис](http://developer.alexanderklimov.ru/android/sqlite/azbuka-sqlite.php)

http://developer.alexanderklimov.ru/android/sqlite/simplydatabase.php



##3) Файлы
Все приложения в андроиде живут в отдельных контейнерах и по умолчанию не могут друг с другом взаимодействовать. Как следствие, все файл конкретного приложения хранятся в своем контейнере.
Примеры записи файлов:
```java
final String FILENAME = "file.txt";
  
void writeFile() {
    try {
      // отрываем поток для записи
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          openFileOutput(FILENAME, MODE_PRIVATE)));
      // пишем данные
      bw.write("Содержимое файла");
      // закрываем поток
      bw.close();
      Log.d(LOG_TAG, "Файл записан");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void readFile() {
    try {
      // открываем поток для чтения
      BufferedReader br = new BufferedReader(new InputStreamReader(
          openFileInput(FILENAME)));
      String str = "";
      // читаем содержимое
      while ((str = br.readLine()) != null) {
        Log.d(LOG_TAG, str);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
```
Читать-писать можно посимвольно, построчно и буфером определенного размера

###Ссылки
http://startandroid.ru/ru/uroki/vse-uroki-spiskom/138-urok-75-hranenie-dannyh-rabota-s-fajlami.html

http://developer.alexanderklimov.ru/android/texteditor.php

http://developer.android.com/reference/java/io/BufferedReader.html

http://developer.android.com/reference/java/io/BufferedWriter.html
##4) Assets

##5) ORM (object-related-mapping)

