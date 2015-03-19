##Gradle, Manifest. Activity, View

[Скринкаст занятия](https://www.youtube.com/watch?v=O7kQ3pNC7JE)

###Gradle
Система автоматической сборки приложений. Не является особенностью Android, была возможность установить и на Java, но в Android
Studio стала обязательной. 

Как правило существует 2 файла build.gradle - для проекта и для модуля. Нас интересует для модуля. 

Добавление библиотеки происходит следующим образом

```python
dependencies {
    compile 'com.squareup.picasso:picasso:2.3.2'
}
```
Так же тут прописываются некоторые параметры приложения - такие как номер версии и версии андроида. Так же Gradle мы будем 
использовать для релиза приложения. Когда в градле что-то меняется необходимо синхронизовать проект.

###XML
Язык разметки. Удобен для программ и человека. Объявление XML объявляет версию языка, на которой написан документ. Поскольку интерпретация содержимого документа, вообще говоря, зависит от версии языка, то Спецификация предписывает начинать документ с объявления XML. В первой (1.0) версии языка использование объявления не было обязательным, в последующих версиях оно обязательно. Таким образом, версия языка определяется из объявления, и если объявление отсутствует, то принимается версия 1.0.
Кроме версии XML, объявление может также содержать информацию о кодировке документа и «оставаться ли документу со своим собственным DTD, или с подключённым».

В андроиде мы указываем
```xml
<?xml version="1.0" encoding="utf-8"?>
```
Пример xml файла
```xml
<contacts>
    <contact
        first_name="Иван"
        last_name="Иваненко"
        phone="090507"/>
    <contact
        first_name="Василий"
        last_name="Пупкин"
        phone="123456"/>
    <contact
        first_name="Денис"
        last_name="Денисенко"
        phone="654879"/>
    <contact
        first_name="Данил"
        last_name="Даниленко"
        phone="234587"/>
</contacts>
```


###Manifest
Манифест нужен для задания некоторых настроек и параметров приложения. Например:
  1. объявляет имя Java-пакета приложения, который служит уникальным идентификатором;
  2. описывает компоненты приложения — деятельности, службы, приемники широковещательных намерений и контент-провайдеры, что позволяет вызывать классы, которые реализуют каждый из компонентов, и объявляет их намерения
  3. ссодержит список необходимых разрешений для обращения к защищенным частям API и взаимодействия с другими приложениями;
  4. объявляет разрешения, которые сторонние приложения обязаны иметь для взаимодействия с компонентами данного приложения;

Манифест является xml файлом.
Общая структура манифеста:

```xml
<?xml version="1.0" encoding="utf-8"?>

<manifest>

    <uses-permission />
    <permission />
    <permission-tree />
    <permission-group />
    <instrumentation />
    <uses-sdk />
    <uses-configuration />  
    <uses-feature />  
    <supports-screens />  
    <compatible-screens />  
    <supports-gl-texture />  

    <application>

        <activity>
            <intent-filter>
                <action />
                <category />
                <data />
            </intent-filter>
            <meta-data />
        </activity>

        <activity-alias>
            <intent-filter> . . . </intent-filter>
            <meta-data />
        </activity-alias>

        <service>
            <intent-filter> . . . </intent-filter>
            <meta-data/>
        </service>

        <receiver>
            <intent-filter> . . . </intent-filter>
            <meta-data />
        </receiver>

        <provider>
            <grant-uri-permission />
            <meta-data />
            <path-permission />
        </provider>

        <uses-library />

    </application>

</manifest>
```

Почти все поля являются необязательными. Работа с манифестом будет проходить по мере необходимости - когда нужно добавить какое-то разрешение или активити. 

[Статья про манифест](http://developer.alexanderklimov.ru/android/theory/AndroidManifestXML.php)

###Activity
Основной единицей приложения является активити. Это по сути отдельный экран. 
Для каждого экрана создается свой класс.

Процесс создания активити можно сделать в 2 клика, но сначала сделаем разок вручную. 
    1. Создаем файлы разметки
Создаем в папке меню файл menu_test.xml

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context="ru.ryazanoff.faceaday.TestActivity">
    <item android:id="@+id/action_settings" android:title="@string/action_settings"
        android:orderInCategory="100" app:showAsAction="never" />
</menu>
```

В папке layout activity_test.xml
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" android:layout_width="match_parent"
    android:layout_height="match_parent" android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin"
    tools:context="ru.ryazanoff.faceaday.TestActivity">

    <TextView android:text="@string/hello_world" android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</RelativeLayout>
```
    2. Создаем класс TestActivity
    3. В описании класса наследуемся от Activity (лучше ActionBarActivity) и добавляем методы:
```java
    public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    //создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
```
    4. Изменим файл манифеста
```xml
        <activity
            android:name=".TestActivity"
            android:label="@string/title_activity_test"
            android:screenOrientation="portrait" >
        </activity>
```

Все это можно сделать в пару кликов - просто Create New Activity

Жизненный цикл активити:
http://developer.android.com/reference/android/app/Activity.html

Изучение поведения активити
http://habrahabr.ru/post/201214/

Можно ввести запрет на наличие более одного экземпляра одной активити.

###View
Класс реализующий базовые элементы интерфейса. Представлен в виде прямоугольной области. Отрисовывает элемент и обрабатывает нажатия. Является классом родителем для всех UI - элементов.
Обязательно нужно указывать `layout_width` и `layout_height`

Дети: **ViewGroup**, TextView, EditText, Button, List ...

Layout & Margin:
http://stackoverflow.com/questions/4619899/difference-between-a-views-padding-and-margin

###sp dp px
Размеры View элементов можно указывать по-разному. px - в пикселях, sp,dp - относительные размеры.
http://developer.android.com/guide/practices/screens_support.html
http://stackoverflow.com/questions/2025282/difference-between-px-dp-dip-and-sp-in-android
http://startandroid.ru/ru/materialy/pamjatka/40-edinitsy-izmerenija-chem-otlichaetsja-dp-dip-ot-px-screen-density
Самое важное, что следует вынести из этого раздела - **всегда** размеры нужно указывать в sp или dp и только в случае крайней необходимости  - px.

####Получение элемента
setContentView(R.layout.activity_main);

```java
Button b = (Button) findViewById(R.id.button);
```
Вызывать это нужно ПОСЛЕ
```java
setContentView(R.layout.activity_main);
```

####R.java
Существует специальный файл, в котором лежат id всех элементов приложения.

####Создание обработчика нажатий
1.Через XML

В xml описание нашей кнопки добавляем свойство
    
```xml
    android:onClick="onClick"
```

    В Activity создаем метод onClick.
    
```java
    public void onClick(View view){
        //some staff
    }
```

2.Программно 
    
```java
Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AskActivity.class);
                startActivity(i);
            }
        });
```

3.Через интерфейс
Имплементим интерфейс `public class MainActivity extends ActionBarActivity implements View.OnClickListener`
Он содержит единственный метод onClick, который и нужно реализовать.
    
```java
 public void onClick(View v) {
        switch(v.getId()){
            case R.id.button:
                //some code
                break;
            case R.id.button2:
                ///
                break;
            case R.id.button3:
                ///
                break;
        }
    }
```

Добавляем обработчик событий `b.setOnClickListener(this);`

[Создание кастомного списка](http://habrahabr.ru/post/133575/)

###Задание
Сделайте Activity и layout своих приложений
