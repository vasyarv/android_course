##Хранение и передача данных в Android приложении.

###Shared Preferences

Набор пар ключ-значение, которые можно получить в любой Activity приложения (а точнее в любом Context)

Создаем глобальную переменную
```java  
SharedPreferences sPref;
````
  
Получаем префернcы:
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
  //либо
  ed.apply(); //закрываем редактор  
 ```
Разница между commit и apply следующая - apply() возвращает void, а commit() возвращает boolean. apply был добавлен, т.к. было замечено, что почти никто не смотрит на значение возвращаемой величины, и apply работает быстрее.

Получение элемента по ключу
  Здесь создавать editor НЕ нужно
 ```java  
`  String some_value = sPref.getString("key", "default_value");  //если не получилось вернуть значение по ключу или ключ не найден   - возвращается default_value`
  ```
  
SharedPreferences хранятся в файле: `/data/data/имя_пакета/shared_prefs/имя_файла_настроек.xml.`
Можно отловить первый запуск приложения.
  
Проверка наличия ключа
```java   
Boolean b = sPref.contains("key2");
```

Удаление по ключу: `SharedPreferences.Editor.remove()`

Удаление всего: `SharedPreferences.Editor.clear()`

И не забываем после этого делать commit или apply.
  
Работа с наборами ( `<Set>` )

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

После удаления приложения SharedPreferences тоже удаляются, но можно воспользоваться [BackupAgentHelper](http://developer.android.com/reference/android/app/backup/BackupAgentHelper.html)

####Ссылки:
http://developer.alexanderklimov.ru/android/theory/sharedpreferences.php

http://startandroid.ru/ru/uroki/vse-uroki-spiskom/73-urok-33-hranenie-dannyh-preferences

http://developer.android.com/reference/android/content/SharedPreferences.html

###SQLite + SQL

SQLite - реляционная БД 
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

####Ссылки

[Синтаксис](http://developer.alexanderklimov.ru/android/sqlite/azbuka-sqlite.php)

[Работа с базой SQL](http://developer.alexanderklimov.ru/android/sqlite/simplydatabase.php)

[Работа с курсором](http://developer.alexanderklimov.ru/android/theory/cursor.php)

[Еще про курсор](http://stackoverflow.com/questions/19807916/android-sqlite-custom-query)

[Join с помощью курсора](http://developer.android.com/reference/android/database/CursorJoiner.html)

### Файлы
Все приложения в андроиде живут в отдельных контейнерах и по умолчанию не могут друг с другом взаимодействовать. Как следствие, все файлы конкретного приложения хранятся в своем контейнере.
Примеры записи файлов:
```java
final String FILENAME = "file.txt";
  
void writeFile() {
    try {
      // отрываем поток для записи
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
          openFileOutput(FILENAME, MODE_PRIVATE)));
      //MODE_PRIVATE - режим создания файла. openFileOutput - метод Context, открывает файл на запись
      //OutputStreamWriter - класс для преобразования потока символов в поток байтов. 
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

####Ссылки


http://startandroid.ru/ru/uroki/vse-uroki-spiskom/138-urok-75-hranenie-dannyh-rabota-s-fajlami.html

http://developer.alexanderklimov.ru/android/texteditor.php

http://developer.android.com/reference/java/io/BufferedReader.html

http://developer.android.com/reference/java/io/BufferedWriter.html

###Assets(Активы)
Папка для хранения ресурсов, находится на том же уровне, что и *res*.
Предназначена, чтобы хранить файлы, которые нельзя изменять:
	1. Шрифты
	2. Звуки
	3. Простые текстовые файлы
Пример чтение текстового файла из активов:
```java
	AssetManager am = activity.getAssets();
	InputStream is = am.open("test.txt");
	String s = convertStreamToString(is);
	is.close();
	return s;
```
Пример использования шрифта (assets/fonts):
```java
Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/TimesNewRoman.ttf");
textView.setTypeface(tf);
```
Пример использования звуков:
```java
public void playBeep() {
    try {
        if (m.isPlaying()) {
            m.stop();
            m.release();
            m = new MediaPlayer();
        }

        AssetFileDescriptor descriptor = getAssets().openFd("beepbeep.mp3");
        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();

        m.prepare();
        m.setVolume(1f, 1f);
        m.setLooping(true);
        m.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

###5) ORM (object-related-mapping)
Расскажу на примере ORMlite, хотя это не единственная библиотека. [Остальные библиотеки](http://habrahabr.ru/company/yotadevices/blog/242559/)
Установка: `compile 'com.j256.ormlite:ormlite-android:4.46'`

[Примеры](http://ormlite.com/android/examples/)


###6) ContentProvider
Способ расширить данные вашего приложения до других приложений. 
http://developer.alexanderklimov.ru/android/theory/contentprovider.php

http://startandroid.ru/ru/uroki/vse-uroki-spiskom/166-urok-101-sozdaem-svoj-contentprovider
