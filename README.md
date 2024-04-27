# PreferenceX

该项目是对安卓Preference的一个简单使用，Preference是官方提供的，有关设置的一种操作设计和依赖组件，并且与系统界面拥有高契合度，风格和体验也与系统的Settings一致。

---
![avatar](demonstration.gif)
---  

### 使用方式
需要将该应用作为系统应用，并push到 /system/app/目录下，在重启之后，该应用将作为Settings的子菜单，显示在主页的列表里面。
<br>

```
adb shell mkdir /system/app/PreferenceX
adb push PreferenceX.apk /system/app/PreferenceX/

```  
### 子菜单配置  
```
	<meta-data
		android:name="com.android.settings.category"
		android:value="com.android.settings.category.ia.homepage" />

	<meta-data
		android:name="com.android.settings.order"
		android:value="10" />

	<meta-data
		android:name="com.android.settings.title"
		android:resource="@string/app_name" />

	<meta-data
		android:name="com.android.settings.summary"
		android:resource="@string/app_summary" />

	<meta-data
		android:name="com.android.settings.icon"
		android:resource="@drawable/ic_icon" />
```
### 系统应用设置
需要在AndroidManifest.xml指定 android:sharedUserId="android.uid.system"  

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:sharedUserId="android.uid.system">
```  
### 生成platform.keystore系统签名

在/build/target/product/security路径下找到签名证书，并使用 [keytool-importkeypair](https://github.com/getfatday/keytool-importkeypair) 生成keystore,
执行如下命令：  

```
./keytool-importkeypair -k platform.keystore -p 123456 -pk8 platform.pk8 -cert platform.x509.pem -alias platform
```

并将以下代码添加到gradle配置中：

```
    signingConfigs {
        platform {
            storeFile file("platform.keystore")
            storePassword '123456'
            keyAlias 'platform'
            keyPassword '123456'
        }
    }

    buildTypes {
        release {
            debuggable false
            minifyEnabled false
            signingConfig signingConfigs.platform
        }

        debug {
            debuggable true
            minifyEnabled false
            signingConfig signingConfigs.platform
        }
    }
```

