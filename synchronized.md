###Пример работы с synchronized в Java

[Оригинал статьи](http://sandro-omsk.livejournal.com/6622.html)

[Статья по synchronized в Java (en)](http://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)

Всем бодрого духа! Хочу написать несколько слов о исключительно полезном ключевом слове synchronized в языке Java. Многие начинающие разработчики сталкиваются с проблемой понимания функциональности этого ключевого слова и не могут правильно его использовать и интерпретировать уже написанный код с использованием synchronized, да и как обычно все усугубляет скудное количество русскоязычной документации.

Итак, случается, что ваша программа не может обойтись без параллельного выполнения каких либо задач. Например самый очевидный случай, когда вы пишите сетевое приложение и подразумеваются, что им будут пользоваться несколько пользователей одновременно. На первый взгляд никаких трудностей это не вызывает, но на практике есть несколько очень неприятных моментов.

Начнем с примера. Допустим, вы пишите программу для банкомата. У вас есть 3 пользователя, у которых есть доступ к одному и тому же счету. Конечно, хорошо, если они заходят снять деньги в разное время, но мы должны подумать о том, что они могут захотеть снять деньги одновременно. Тут как раз и начинается самое интересное.

Будем считать, что для каждого из пользователей в нашей системе создается отдельный поток выполнения, т.е абстрактно это выглядит так:

1. Пользователь подошел к банкомату и вставил карту - Мы создали поток
2. Вводится PIN код - делаем нужные проверки, и если все ОК, продолжаем
3. Выбирается сумма и нажимается кнопка “Получить деньги”
4. Вот здесь должно происходит списание денег со счета и их выдача, но есть НО...

... . Дело в том, что если больше одного пользователя одновременно придут в пункт 4, то может случится, что проверка доступных средств на счете для всех выполнится одновременно, а не последовательно. Т.е система проверит что на счете например 10 рублей, а пользователь хочет снять 5 рублей, а таких пользователей у нас 3. В итоге каждый снимет по 5 рублей, и на счете будет минус 5. Как-то не круто.

Тут то к нам на помощь и приходит синхронизация доступа к данным. Как вы видите из примера, нам нужно как то заставить всех пользователей выполнять их транзакции последовательно. Т.е мы должны проводить каждую следующую операцию со счетом, только когда у нас завершена предыдущая.

В абстрактной схеме это будет выглядеть так:

1. Пользователь подошел к банкомату и встали карту - Мы создали поток
2. Вводится PIN код - делаем нужные проверки, и если все ОК, продолжаем
3. Выбирается сумма и нажимается кнопка “Получить деньги”
4. Проверяем, есть ли кто-нибудь, кто сейчас работает с нашим счетом
5. Если нет, тогда говорим что теперь мы заняли счет и проводим свою операцию
6. Если счет кем-то уже занят, мы просто встаем в очередь ожидающих доступа к счет и ждем
7. После получения доступа к счету, делаем все что нам нужно
6. После завершения своих операций со счетом, мы говорим, что мы освободили счет для других

Благодаря тому, что в Java все механизмы синхронизации уже сделаны за нас, нам нужно только указать в каком месте нам требуется синхронный доступ к данным.


Вот небольшая программка, в которой подробно прокомментированы все действия.
PS: Програмка действительно не большая, но обильное кол-во комментариев к ней, делает ее код довольно растянутым

```java
import static java.lang.Thread.sleep;

/**
 * Тестовый класс "Денежный счет" для объяснения функциональности ключевого слова "synchronized"
 *

 * Описание: Есть 3 человека, у которых есть доступ к 1 банковскому счету. На счете лежит 10 рублей.
 * Каждый из участников делает попытку снять 5 рублей со счета.
 *

 * Ожидаемый результат: 2 участника могут снять деньги, а 3ий получает сообщение о том, что денег недостаточно
 * Реальные возможные результаты:
 * а) Все 3ое смогут снять деньги.
 * 6) Смогут снять только 2ое
 *
 * Этот пример содержит 2 вспомогательных класса:
 *
 *
 * 1. CachePoint - класс для демонстарции корректно работающего банкомата (с исп. ключ. слова synchronized)
 * 2. BrokenCachePoint - класс для демонстрации неисправного банкомата
 *
 * В методе main проиллюстрировано 2 примера. Первый использует исправные банкоматы, второй - нет
 *
 * @author sandro
 * @version 1.0
 * @date 7/6/11
 */
public final class MoneyAccount {

    /**
     * Так как это денежный счет, он будет 1 для всех наших пользователей (смотреть "Singleton Pattern")
     * Переменная instance хранит ссылку на единственный экземпляр этого класса
     */
    private static MoneyAccount instance;

    /**
     * Количество денег на счете
     */
    private Integer account;

    /**
     * Инициализации счета. Указываем сколько у нас денег в самом начале
     *

     * Замечание: Конструктор имеет приватную область видимости, так как мы должны гарантировать
     * что объект MoneyAccount в нашей системе будет существовать в единственном экземпляре
     */
    private MoneyAccount() {
        account = 10;
    }

    /**
     * Возвращает ссылку на наш денежный счет. При первом обращении создает единственный экземпляр счета.
     *
     * @return Ссылка на MoneyAccount
     */
    public static MoneyAccount getInstance() {
        if (instance == null) {
            instance = new MoneyAccount();
        }
        return instance;
    }

    /**
     * Снимает указанное количество денег со счета.
     *
     * @param amount Сколько денег снимаем
     */
    public void cache(final int amount) {
        account -= amount;
    }

    /**
     * Показывает сколкьо сейчас денег на счете
     *
     * @return Кол-во денег
     */
    public Integer getAccount() {
        return account;
    }


    /**
     * =======================================================
     * Ниже объявим класс, ктороый будет выполнять функции банкомата
     * =======================================================
     */

    /**
     * Это собственно банкомат. Его функциональность заключается в том, что
     * он должен будет списывать деньги со счета и выдывать их пользователю
     *

     * Замечание: Банкомат у нашем случае реализаует интерфейс Runnable, что позволит нам
     * вынести его работу в отдельный поток
     */
    static final class CachePoint extends Thread {

        /**
         * Ссылка на наш счет. Мы будем использовать ее для синхронного предоставления дотсупа к счету
         */
        private static final MoneyAccount moneyAccount = MoneyAccount.getInstance();

        /**
         * Номер банкомата
         */
        private Integer id;

        /**
         * Имя владельца банковской карты
         */
        private String userName;

        /**
         * Кол-во денег которое мы хотим снять
         */
        private Integer cacheAmount;

        /**
         * Устанавливает кол-во денег для снятия
         *
         * @param cacheAmount Кол-во денег
         */
        public void setCacheAmount(final Integer cacheAmount) {
            this.cacheAmount = cacheAmount;
        }

        /**
         * Устанавливает имя владельца карты
         *
         * @param name Имя
         */
        public void setUserName(final String name) {
            this.userName = name;
        }

        /**
         * Устанавливает номер банкомата
         *
         * @param id Номер
         */
        public void setId(final Integer id) {
            this.id = id;
        }


        /**
         * {inherited_doc}
         */
        @Override
        public void run() {
            synchronized (moneyAccount) {
                //сохраняем текущее значение счета
                final int previousAccountValue = MoneyAccount.getInstance().getAccount();

                //если на счете денег меньше, чем запрашивают, говорим что денег мало и не даем ничего
                if (previousAccountValue < cacheAmount) {
                    System.out.println(userName + " не смог снять деньги. Недостаточно средств");
                    return;
                }
                //списываем указанную сумму
                MoneyAccount.getInstance().cache(cacheAmount);


                //формируем сообщение об успешной транзакции
                final StringBuilder message = new StringBuilder();
                message.append("Произведено снятие средств пользоваталем ");
                message.append(userName);
                message.append(" с банкомата ");
                message.append(id);
                message.append(" на сумму ");
                message.append(cacheAmount);
                message.append(". Остаток на счете до снятия:  ");
                message.append(previousAccountValue);
                message.append(", после снятия: ");
                message.append(MoneyAccount.getInstance().getAccount());

                //спасаем мир от всесущего зла ^^
                System.out.println(message);
            }
        }
    }

    /**
     * А это сломаный банкомат. Он может отдавать деньги пользователям, даже если на их счету ноль.
     * В этом банкомате не используется синхронизация, что приведет к полной порче и потере данных.
     */
    static final class BrokenCachePoint extends Thread {

        /**
         * Номер банкомата
         */
        private Integer id;

        /**
         * Имя владельца банковской карты
         */
        private String userName;

        /**
         * Кол-во денег которое мы хотим снять
         */
        private Integer cacheAmount;

        /**
         * Устанавливает кол-во денег для снятия
         *
         * @param cacheAmount Кол-во денег
         */
        public void setCacheAmount(final Integer cacheAmount) {
            this.cacheAmount = cacheAmount;
        }

        /**
         * Устанавливает имя владельца карты
         *
         * @param name Имя
         */
        public void setUserName(final String name) {
            this.userName = name;
        }

        /**
         * Устанавливает номер банкомата
         *
         * @param id Номер
         */
        public void setId(final Integer id) {
            this.id = id;
        }


        /**
         * {inherited_doc}
         */
        @Override
        public void run() {
            //сохраняем текущее значение счета
            final int previousAccountValue = MoneyAccount.getInstance().getAccount();

            //дадим потокам время запустится. Можно убрать этот кусок, но тогда будет сложнее воспроизвести
            // ситуацию с рассинхронизацией доступа к данным.
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //если на счете денег меньше, чем запрашивают, говорим что денег мало и не даем ничего
            //
            // Замечание: Эту проверку все 3 потока уже прошли успешно, так как их доступ к этому месту
            // не синхронизирован
            if (previousAccountValue < cacheAmount) {
                System.out.println(userName + " не смог снять деньги. Недостаточно средств");
                return;
            }
            //списываем указанную сумму
            //
            //Замечание: А в этом месте все 3 пототка с чистой совестью отнимают от существующего счета
            //требуемую сумму
            MoneyAccount.getInstance().cache(cacheAmount);

            //формируем сообщение об успешной транзакции
            final StringBuilder message = new StringBuilder();
            message.append("Произведено снятие средств пользоваталем ");
            message.append(userName);
            message.append(" с банкомата ");
            message.append(id);
            message.append(" на сумму ");
            message.append(cacheAmount);
            message.append(". Остаток на счете до снятия:  ");
            message.append(previousAccountValue);
            message.append(", после снятия: ");
            message.append(MoneyAccount.getInstance().getAccount());

            //А тут у нас отрицательный баланс :(
            System.out.println(message);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * Пример с исправными банкоматами
         */
        System.out.println("Ситуация №1. Мы используем исправные банкоматы");
        CachePoint point1 = new CachePoint();
        point1.setId(1);
        point1.setCacheAmount(5);
        point1.setUserName("Вася");

        CachePoint point2 = new CachePoint();
        point2.setId(2);
        point2.setCacheAmount(5);
        point2.setUserName("Петя");

        CachePoint point3 = new CachePoint();
        point3.setId(3);
        point3.setCacheAmount(5);
        point3.setUserName("Джо");

        point1.start();
        point2.start();
        point3.start();

        //подождем 100 мс. пока все потоки сделают своё дело
        sleep(100);

        // установим значение счета обратно в 10
        MoneyAccount.getInstance().account = 10;
        System.out.println();

        /**
         * Пример с неисправными банкоматами
         */

        System.out.println("Ситуация №2. Мы используем неисправные банкоматы");
        BrokenCachePoint bpoint1 = new BrokenCachePoint();
        bpoint1.setId(1);
        bpoint1.setCacheAmount(5);
        bpoint1.setUserName("ЗлойВася");

        BrokenCachePoint bpoint2 = new BrokenCachePoint();
        bpoint2.setId(2);
        bpoint2.setCacheAmount(5);
        bpoint2.setUserName("НедовольныйПетя");

        BrokenCachePoint bpoint3 = new BrokenCachePoint();
        bpoint3.setId(3);
        bpoint3.setCacheAmount(5);
        bpoint3.setUserName("ДжоВБешенстве");

        bpoint1.start();
        bpoint2.start();
        bpoint3.start();

    }

}
```
