##Релиз apk из Anroid studio.

1) Получаем ключ через keytool Кладем его в папку app (название модуля) 

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
