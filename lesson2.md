#Хранение и передача данных в Android приложении.

##1) Shared Preferences

Набор пар ключ-значение, которые можно получить в любом месте из приложения.

Создаем глобальную переменную
'  SharedPreferences sPref;'
  
Получаем преферны:
  sPref = getPreferences(MODE_PRIVATE);

Редактирование:
  Editor ed = sPref.edit();
  ИЛИ
  SharedPrefernces.Editor ed = sPref.edit();
  
Добавление элемента:
  ed.putString("key", "value"); //также putInt , putFloat и т.д.
  ed.commit(); //закрываем редактор
  
Получение элемента по ключу
  Здесь редактор НЕ нужен
  String some_value = sPref.getString("key", "default_value");  //если не получилось вернуть значение по ключу или ключ не найден   - возвращается default_value
  
  SharedPreferences хранятся в файле.
  Можно отловить первый запуск приложения.
  
Проверка наличия ключа
  Boolean b = sPref.contains("key2");

Ключ - всегда строка
  
Работа с наборами ( <set> )
SharedPreferences sp;
String catnames;

// записываем имена котов в файл настроек
public void onPutSettings(View v){
	Set<String> catnames = new HashSet<String>();
	catnames.add("Мурзик");
	catnames.add("Барсик");
	catnames.add("Рыжик");
	Editor e = sp.edit();
	e.putStringSet("strSetKey", catnames);
	e.apply();
}

// считываем имена котов обратно
public void onShowSettings(View v)
{
 	Set<String> ret = sp.getStringSet("strSetKey", new HashSet<String>());
	for(String r : ret) {
	    Log.i("Share", "Имя кота: " + r);
	}
}
  

##2) SQLite + SQL

##3) Файлы

##4) Assets

##5) ORM (object-related-mapping)

