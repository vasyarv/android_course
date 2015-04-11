##Fragment
[Ман от гугла](http://developer.android.com/guide/components/fragments.html)
[Уроки от гугла](http://developer.android.com/training/basics/fragments/index.html)

Fragment — модульная часть activity, у которой свой жизненный цикл и свои обработчики различных событий. Android добавил фрагменты с API 11, для того, чтобы разработчики могли разрабатывать более гибкие пользовательские интерфейсы на больших экранах, таких как экраны планшетов. Через некоторое время была написана библиотека, которая добавляет поддержку фрагментов в более старые версии. 

Основные классы

Есть три основных класса:

`android.app.Fragment` — от него, собственно говоря. и будут наследоваться наши фрагменты

`android.app.FragmentManager` — с помощью экземпляра этого класса происходит все взаимодействие между фрагментами

`android.app.FragmentTransaction` — ну и этот класс, как понятно по названию, нужен для совершения транзакций.

В настоящее время появляются разновидности класса Fragment, для решения определенных задач — ListFragment, PreferenceFragment и др.


###Простой пример с фрагментом

Создаем 2 layout для фрагментов.

fragment1.xml
```xml
  <?xml version="1.0" encoding="utf-8"?>
  <LinearLayout
   xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   android:background="#77ff0000"
   android:orientation="vertical">
  <TextView
   android:id="@+id/textView1"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:text="Fragment 1">
  </TextView>
  </LinearLayout>
```

fragment2.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
 xmlns:android="http://schemas.android.com/apk/res/android"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:background="#7700ff00"
 android:orientation="vertical">
<TextView
 android:id="@+id/textView2"
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 android:text="Fragment 2">
</TextView>
</LinearLayout>
```

У каждого фрагмента обязательно должен быть свой класс

```java
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment1 extends Fragment {

  final String LOG_TAG = "myLogs";

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Log.d(LOG_TAG, "Fragment1 onAttach");
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(LOG_TAG, "Fragment1 onCreate");
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Log.d(LOG_TAG, "Fragment1 onCreateView");
    return inflater.inflate(R.layout.fragment1, null);
  }

  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d(LOG_TAG, "Fragment1 onActivityCreated");
  }

  public void onStart() {
    super.onStart();
    Log.d(LOG_TAG, "Fragment1 onStart");
  }

  public void onResume() {
    super.onResume();
    Log.d(LOG_TAG, "Fragment1 onResume");
  }

  public void onPause() {
    super.onPause();
    Log.d(LOG_TAG, "Fragment1 onPause");
  }

  public void onStop() {
    super.onStop();
    Log.d(LOG_TAG, "Fragment1 onStop");
  }

  public void onDestroyView() {
    super.onDestroyView();
    Log.d(LOG_TAG, "Fragment1 onDestroyView");
  }

  public void onDestroy() {
    super.onDestroy();
    Log.d(LOG_TAG, "Fragment1 onDestroy");
  }

  public void onDetach() {
    super.onDetach();
    Log.d(LOG_TAG, "Fragment1 onDetach");
  }
}
```

```java
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends Fragment {

  final String LOG_TAG = "myLogs";
  
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    Log.d(LOG_TAG, "Fragment2 onAttach");
  }
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(LOG_TAG, "Fragment2 onCreate");
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Log.d(LOG_TAG, "Fragment2 onCreateView");
    return inflater.inflate(R.layout.fragment2, null) ;
  }
  
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.d(LOG_TAG, "Fragment2 onActivityCreated");
  }
  
  public void onStart() {
    super.onStart();
    Log.d(LOG_TAG, "Fragment2 onStart");
  }
  
  public void onResume() {
    super.onResume();
    Log.d(LOG_TAG, "Fragment2 onResume");
  }
  
  public void onPause() {
    super.onPause();
    Log.d(LOG_TAG, "Fragment2 onPause");
  }
  
  public void onStop() {
    super.onStop();
    Log.d(LOG_TAG, "Fragment2 onStop");
  }
  
  public void onDestroyView() {
    super.onDestroyView();
    Log.d(LOG_TAG, "Fragment2 onDestroyView");
  }
  
  public void onDestroy() {
    super.onDestroy();
    Log.d(LOG_TAG, "Fragment2 onDestroy");
  }
  
  public void onDetach() {
    super.onDetach();
    Log.d(LOG_TAG, "Fragment2 onDetach");
  }
}
```

activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/LinearLayout1"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal">
    <fragment
        android:name="com.example.lenovo.fragmenttest.Fragment1"  <!--Ваш пакет.Класс фрагмента -->
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_weight="1"
        tools:layout="@layout/fragment1">
    </fragment>
    <fragment
        android:name="com.example.lenovo.fragmenttest.Fragment2"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_weight="1"
        tools:layout="@layout/fragment2">
    </fragment>
</LinearLayout>
```


###Fragment - второй пример

Сохраним наши классы фрагментов и разметку для них. Воспользуемся [уроком](http://developer.android.com/training/basics/fragments/fragment-ui.html)
Если возникла ошибка *Cannot resolve method add*. Изменим название метода getSupporFragmentManger на getFragmentManager , т.к. иначе мы мешаем классы из разных библиотек. За этим нужно 
следить, т.к. фрагмент может быть унаследован от разных классов.

##### Взаимодействия фрагмента и активити
При работе с фрагментами возникает задача передачи данных из активити в фрагмент и обратно. В кратце как это делать:
  
  1. Вызов метода фрагмента из активити - через экзмепаляр фрагмента, который мы получили, либо через findFragmentById
  2. Вызов метода активити из фрагмента. Получаем активити, к которому прикреплен фрагмент методом getActivity.

Более подробно про передачу данных между фрагментами и активити можно почитать в [этом уроке](http://startandroid.ru/ru/uroki/vse-uroki-spiskom/176-urok-106-android-3-fragments-vzaimodejstvie-s-activity.html)

### 3 пример - слайдинг экранов
Очень удобный и модный интерфейс - слайдинг экранов пальцем влево-вправо. Сделать это очень просто, пример есть в следующем уроке.
[Реализация viewPager](http://developer.android.com/training/animation/screen-slide.html)



