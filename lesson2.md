#Хранение и передача данных в Android приложении.

##1) Shared Preferences

Набор пар ключ-значение, которые можно получить в любом месте из приложения.

Создаем глобальную переменную
```java  
SharedPreferences sPref;
````
  
Получаем преферны:
```java  
  sPref = getPreferences(MODE_PRIVATE);
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
  
Можно создавать отдельные преференции для каждой активити

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

##2) SQLite + SQL

##3) Файлы

##4) Assets

##5) ORM (object-related-mapping)

