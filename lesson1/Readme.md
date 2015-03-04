##Вступление
Android - бесплатная операционная система, основанная на Linux с интерфейсом программирования Java.
Android поддерживает 2D и 3D-графику, используя библиотеки OpenGL, а также хранение данных в базе данных SQLite.
Каждое Android-приложение запускается в своем собственном процессе. Поэтому приложение изолировано от других запущенных приложений, и неправильно работающее приложение не может беспрепятственно навредить другим приложениям.

Для работы нам потребуется установить Java Development Kit (JDK) и Android Studio последних версий.

JDK: http://www.oracle.com/technetwork/java/javase/downloads/index.html

Android Studio: http://developer.android.com/sdk/index.html

Важно понимать, что в данном случае мы пишем на Java и Android Studio - всего лишь среда разработки. Раньше большинство разработчиков использовало Eclipse для этих целей, но сейчас Google целиком перешел на поддержку Android Studio. 
Мой курс будет вестись относительно Android Studio.

Устанавливаем сначала JDK, потом Android Studio. Если при запуске Android Studio выдаёт ошибку, то необходимо задать переменную окружения `JAVA_HOME` равной пути к JDK, например `C:\Program Files\Java\jdk1.8.0_25`

##Запуск

Тестирование приложение можно делать следующими способами:
  * Физическое устройство на андроид ( [Док](http://developer.android.com/tools/device.html) ,в некоторых случаях нужны драйвера)
  * Стандартные эмуляторы (Если [ругается на Intel Hax Kernel Model](http://stackoverflow.com/questions/26355645/error-in-launching-avd) , [путь к SDK](http://stackoverflow.com/questions/16581752/android-studio-how-to-change-android-sdk-path) )
  * Прочие эмуляторы (например [GenyMotion](https://www.genymotion.com) )

##Полезные сайты:
 * [Доки разработчика](http://developer.android.com)
 * [Климов](http://developer.alexanderklimov.ru/android/) - простые и наглядные уроки
 * [StartAndroid](http://startandroid.ru/ru/) - большое количество уроков + YouTube канал

##Gradle
 Android Studio по умолчанию устанавливается с автоматической системой сборки Gradle. [Официальная страница](https://gradle.org/). В случае неправильной настройки, Gradle приводит к невозможности собрать проект. [Типичная ошибка](http://stackoverflow.com/questions/27016385/error26-0-gradle-dsl-method-not-found-runproguard). 
 
 Пример подключения внешних библиотек
 ```Java
    compile 'com.android.support:appcompat-v7:21.0.3'
    compile 'com.parse.bolts:bolts-android:1.+'
    compile 'com.google.android.gms:play-services:6.+'
```
 

##Java
 * [Интерактивные уроки](http://javarush.ru/)
 * [LearnJavaOnline](http://www.learnjavaonline.org/) - не компилится
 * Java/J2EE Job Interview Companion By K.Arulkumaran
 * [Климов](http://developer.alexanderklimov.ru/android/java/java.php)
