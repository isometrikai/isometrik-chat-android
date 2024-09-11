
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
    implementation 'com.github.isometrikai:isometrik-chat-android:1.1.1'
}
```
### Step 3: Sync Your Project

Once you've made the changes, sync your project with the Gradle files by clicking the "Sync Now" button in Android Studio.


# SDK Integration Guide

Follow the steps below to integrate and configure the Isometrik UI SDK in your Android project.

## Step 1: SDK Initialization

Initialize the SDK in your project's `Application` class, specifically within the `onCreate()` method.

```java
IsometrikUiSdk.getInstance().sdkInitialize(this);
```

## Step 2: SDK Configuration

Configure the SDK in the first method called in your app, typically within the `Application` class's `onCreate()` method. You will need the following details for configuration:

- `app_secret`
- `user_secret`
- `license_key`
- `google_places_api_key`
- `giphy_api_key`
- `accountId`
- `projectId`
- `keysetId`

Provide these details using the parameters in the method below:

```java
IsometrikUiSdk.getInstance()
        .createConfiguration(
            getString(R.string.app_secret),
            getString(R.string.user_secret),
            getString(R.string.accountId),
            getString(R.string.projectId),
            getString(R.string.keysetId),
            userName,
            password,
            getString(R.string.license_key),
            BuildConfig.APPLICATION_ID,
            getString(R.string.app_name),
            getString(R.string.google_places_api_key),
            getString(R.string.giphy_api_key)
        );
```
To handle SDK termination, call the following method, usually in the onTerminate() method of the Application class:

```java
IsometrikUiSdk.getInstance().onTerminate();
```

## Step 3: Create a Connection

Establish a connection on the base screen of your app (e.g., `MainActivity` or `LandingActivity`) before accessing the chat functionality. You will need the following details:

- `userClientId` (isometrikUserId)
- `userIsometrikToken`

Use the following method to create a connection:

```java
IsometrikUiSdk.getInstance().getIsometrik().createConnection(userClientId, userIsometrikToken);
```

### Step 4: Start a Conversation
You are now ready to start a conversation. You can launch ConversationsActivity from any click action in your app:

```java
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

