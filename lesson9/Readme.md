##Релиз apk из Anroid studio.

1) Получаем ключ через keytool Кладем его в папку app (название модуля) 

Раньше тут всё было сложно, теперь всё делается из Android Studio

[http://developer.android.com/tools/publishing/app-signing.html](http://developer.android.com/tools/publishing/app-signing.html)

2) Нажимаем вкладку Gradle, выбираем assembleRelease Меняем файл build gradle Во вкладку андроид добавляем 

```gradle
    android { 
        lintOptions { 
            checkReleaseBuilds false // Or, if you prefer, you can continue to check for errors in release builds, 
            // but continue the build even when errors are found: abortOnError false 
        } 
    }
```

Меняем вкладку релиз

```gradle
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        signingConfig signingConfigs.release
    }
```

Добавляем

```gradle
signingConfigs {
        release {
            storeFile file("my-release-key.keystore")
            storePassword "***"
            keyAlias "***"
            keyPassword "***"
        }
    }
```


Пример gradle файла


```gradle
apply plugin: 'com.android.application'

android {
    compileSdkVersion 21
    buildToolsVersion "21.1.2"

    defaultConfig {
        applicationId "ru.ryazanoff.landaurus"
        minSdkVersion 16
        targetSdkVersion 21
        versionCode 1
        versionName "1.0"
    }

    signingConfigs {
        release {
            storeFile file("my-release-key.keystore")
            storePassword "***"
            keyAlias "alias_name"
            keyPassword "***"
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }

    android {
        lintOptions {
            checkReleaseBuilds false
            // Or, if you prefer, you can continue to check for errors in release builds,
            // but continue the build even when errors are found:
            abortOnError false
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:21.0.3'
    compile 'com.google.android.gms:play-services:6.+'

}
```
##Публикация и продвижение в Google Play

Основные принципы:

    1. Как можно шире локализация -> перевести на все языки
    2. Постараться вывести в топ сразу
    
Как строится ранжирование в топе?

    1. В первую очередь - количество загрузок и их динамика!
    2. Возможно рейтинг
    
##Аналитика

AppAnnie - расширенная статистика по приложениям и ключевым словам.

MixPanel - аналитика поведения юзеров

GoogleAnalytics - то же самое что и MixPanel

##Монетизация (т.е. получаем деньги =) )

Основные способы:

    1. Реклама
    2. Платное приложение
    3. Freemium (приложение бесплатное, но есть внутриигровые покупки)
    
Зачем нам выявлять пользователей, которые ничего не покупают?

Популярные рекламные сети:

    1. AdMob
    2. WapStart (российская)
    3. Еще 100500 сетей
