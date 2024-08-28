
# Isometrik Chat Android

A simple chat library for Android applications.

## Getting Started

To use this library in your Android project, follow the steps below to add the required dependency.

### Step 1: Configure Repositories

Open your `settings.gradle` file (located at the root of your project) and add the following:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2: Configure Project Repositories

Ensure your project's build.gradle (Project Level) file contains the following repositories:

```groovy
allprojects {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 3: Add the Dependency

Open your build.gradle file (app-level) and add the following line in the dependencies section:

```groovy
dependencies {
    implementation 'com.github.isometrikai:isometrik-chat-android:1.0.1'
}
```
### Step 4: Sync Your Project

Once you've made the changes, sync your project with the Gradle files by clicking the "Sync Now" button in Android Studio.



# Technical details

* compileSdkVersion  = 34
* buildToolsVersion = '34.0.0'
* targetSdkVersion 34
* Android gradle plugin 8.4.0
* sourceCompatibility 17
* targetCompatibility 17
* JDK version 17
* Kotlin version 1.9.23

