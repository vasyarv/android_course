##Работа с потоками в Android. AsyncTask ,  Handler, Synchronized
Для чего нужны потоки?

Выполнять разные процедуры, чтобы они не мешали друг другу - воспроизведение музыки, видео, секундомер.

Все компоненты андроида начинают работу в основном потоке (активити, сервисы, ... ) Поэтому все трудоемкие операции обязательно нужно
переносить в фоновый процесс, чтобы интерфейс не тормозил и вообще работал. Более того, если вы подгрузили активити более чем на 5 секунд,
появится окно принудительного завершения работы приложения и недовольные пользователи поставят вам 1.

###Runnable и Handler
Начнём сразу с примера. Допустим нам нужно написать секундомер, который обновляется раз в секунду. Он будет запускаться и останавливаться
по кнопке.

Последовательность следующая.
  1. Реализуем метод run() интерфейса Runnable, в котором прописывается логика
  2. Для взаимодействия с UI используется Handler и сообщения
  3. Создаётся Thread с Runnable
  
Пример использования [synchronized](https://github.com/vasyarv/android_course/blob/master/synchronized.md)

[Еще один способ работы с Thread](http://developer.alexanderklimov.ru/android/java/thread.php)

###AsyncTask
AsyncTask - простой класс для перемещения трудоемких операций в фоновый поток. 

Работать напрямую с AsyncTask нельзя, необходимо от него наследоваться, определяя 3 параметра (впрочем все 3 можно сделать Void)

Формат AsyncTask:
`AsyncTask<[Input Parameter Type], [Progress Report Type], [Result Type]>`

Каркас AsyncTask:
```java
private class MyAsyncTask extends AsyncTask<String, Integer, Integer> {
    @Override
    protected void onProgressUpdate(Integer... progress) {
        // [... Обновите индикатор хода выполнения, уведомления или другой   
        // элемент пользовательского интерфейса ...]
    }
    
	@Override
    protected void onPostExecute(Integer... result) {
        // [... Сообщите о результате через обновление пользовательского 
        // интерфейса, диалоговое окно или уведомление ...]
    }
   
   @Override
    protected Integer doInBackground(String... parameter) {
        int myProgress = 0;
        // [... Выполните задачу в фоновом режиме, обновите переменную myProgress...]
        publishProgress(myProgress);
        // [... Продолжение выполнения фоновой задачи ...]
        // Верните значение, ранее переданное в метод onPostExecute
        return result;
    }
}
```

[Урок](http://developer.alexanderklimov.ru/android/theory/asynctask.php)

####А в чем разница?
Thread,Runnable, Handler - для выполнения любых операций в параллельных потоках

AsyncTask - для выполнения какой-то КОНКРЕТНОЙ операции в фоновом потоке (загрузка файлов, обратный отсчёт и т.д.)
