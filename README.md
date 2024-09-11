
# Isometrik Chat Android

A simple chat library for Android applications.

## Adding it to your project

To use this library in your Android project, follow the steps below to add the required dependency.

### Step 1: Configure Project Repositories

Ensure your project's build.gradle (Project Level) file contains the following repositories:

```groovy
allprojects {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Add the Dependency

Open your build.gradle file (app-level) and add the following line in the dependencies section:

```groovy
dependencies {
    implementation 'com.github.isometrikai:isometrik-chat-android:1.1.0'
}
```
### Step 3: Sync Your Project

Once you've made the changes, sync your project with the Gradle files by clicking the "Sync Now" button in Android Studio.

## Usage

### Step 1: SDK initialization

Initialize SDK in project Application class's onCreate() method.

```groovy
IsometrikUiSdk.getInstance().sdkInitialize(this);
```

### Step 2: SDK Configuration
Configure SDK at most first calling method in app. mostly it would be Application class's onCreate() method. you will require below details for configuration.

1) app_secret
2) user_secret
3) license_key
4) google_places_api_key
5) giphy_api_key
6) accountId
7) projectId
8) keysetId

you can provides this details in below method param.

```groovy
IsometrikUiSdk.getInstance()
        .createConfiguration(getString(R.string.app_secret), getString(R.string.user_secret),
            getString(R.string.accountId),getString(R.string.projectId),getString(R.string.keysetId),
                userName,password ,getString(R.string.license_key),
            BuildConfig.APPLICATION_ID,getString(R.string.app_name), getString(R.string.google_places_api_key),
            getString(R.string.giphy_api_key));
```
and call below method on app terminal. mostly it would be onTerminate() method of Application class.

```groovy
IsometrikUiSdk.getInstance().onTerminate();
```

### Step 3: Create Connection

make this connection on your app base screen which execute first then you go for chat. (ex MainActivity or LandingActivity)
for connection you require below details

1) userClientId  (isometrikUserId)
2) userIsometrikToken

```groovy
 IsometrikUiSdk.getInstance().getIsometrik().createConnection(userClientId,  userIsometrikToken);
```

### Step 4: That's it
Now, you can call ConversationsActivity from any click action and you good to go for chat.

```groovy
 Intent intent = new Intent(this, ConversationsActivity.class);
 startActivity(intent);
```



# Technical details

* compileSdkVersion  = 34
* buildToolsVersion = '34.0.0'
* targetSdkVersion 34
* Android gradle plugin 8.4.0
* sourceCompatibility 17
* targetCompatibility 17
* JDK version 17
* Kotlin version 1.9.23

