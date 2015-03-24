###Пример работы с synchronized в Java

[Оригинал статьи](http://sandro-omsk.livejournal.com/6622.html)

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
