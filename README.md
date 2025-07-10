# Isometrik Chat Android

A simple, powerful chat library for Android applications.

<p float="left">
  <img src="docs/1.png" width="200" />
  <img src="docs/2.png" width="200" />
</p>

---

## 🚀 Features
- Plug-and-play chat UI for Android
- One-to-one, group, and public conversations
- Media, file, location, and contact sharing
- Customizable UI and message types
- Easy integration and configuration
- Multi-language support (localization)

---

## ⚡ Quick Start

### 1. Add the Dependency
Add JitPack to your project-level `build.gradle`:
```groovy
allprojects {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
Add the library to your app-level `build.gradle`:
```groovy
dependencies {
    implementation 'com.github.isometrikai:isometrik-chat-android:1.8.3'
}
```
Sync your project.

### 2. Initialize the SDK
In your `Application` class:
```java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IsometrikChatSdk.getInstance().sdkInitialize(this);
        // Configure the SDK (see below)
    }
}
```

### 3. Configure the SDK
Call this once (usually in `onCreate()`):
```java
IsometrikChatSdk.getInstance().createConfiguration(
    getString(R.string.app_secret),
    getString(R.string.user_secret),
    getString(R.string.accountId),
    getString(R.string.projectId),
    getString(R.string.keysetId),
    getString(R.string.license_key),
    BuildConfig.APPLICATION_ID,
    getString(R.string.app_name),
    getString(R.string.google_places_api_key),
    getString(R.string.giphy_api_key)
);
```

Add your Google Maps API key to `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="maps_api_key" />
```

### 4. Connect a User
Before using chat features:
```java
IsometrikChatSdk.getInstance().getIsometrik().createConnection(userClientId, userIsometrikToken);
```

---

## 💬 Basic Usage

### Open the Chat List
```java
Intent intent = new Intent(this, ConversationsActivity.class);
startActivity(intent);
```

### Use as a Fragment
```java
ConversationsListFragment fragment = new ConversationsListFragment();
getSupportFragmentManager()
    .beginTransaction()
    .replace(R.id.fragment_container, fragment)
    .commit();
```

### Start a New Conversation
```java
// Example: Start a new conversation programmatically
isometrik.getRemoteUseCases()
    .getConversationUseCases()
    .createConversation(/* ... */);
```

---

## 🎨 Customization

- **Custom Activities & Views:**
  - Implement your own screens or override click listeners.
- **Custom Message Types:**
  - Register and display your own message types.
- **UI Tweaks:**
  - Change colors, icons, and visibility of options via `ChatConfig` and `AttachmentsConfig`.

> See the [source code](isometrik-chat/src/main/java/io/isometrik/ui/conversations/list/DefaultChatListItemBinder.kt) for more advanced customization examples.

---

## 🌐 Localization / Multi-language Support

The library provides all user-facing strings in English by default. You can easily add support for any language in your app!

1. **See all available string keys:**  
   [View the library’s `strings.xml`](./isometrik-chat/src/main/res/values/strings.xml)
2. **Add/override translations in your app:**  
   - Copy any keys you want to translate from the above file.
   - Add them to your app’s `res/values-xx/strings.xml` (where `xx` is your language code, e.g., `values-fr` for French).
   - Android will automatically use your translations at runtime.
3. **Override any default text:**  
   - Redefine the string in your app’s `res/values/strings.xml`.

#### Example:
```xml
<!-- In your app's res/values-fr/strings.xml -->
<string name="ism_create_user">Créer un utilisateur</string>
<string name="ism_error">Échec de l'opération demandée, vérifiez les logs du SDK Isometrik pour plus de détails</string>
```

---

## ⚙️ Technical Details
- compileSdkVersion: 34
- buildToolsVersion: 34.0.0
- targetSdkVersion: 34
- Android Gradle plugin: 8.4.0
- Java/Kotlin: 17 / 1.9.23
- One Signal: 5.1.21

---

## 📚 Links & Support
- [Full API & Source Code](./isometrik-chat/)
- [strings.xml (for localization)](./isometrik-chat/src/main/res/values/strings.xml)
- For issues or questions, please open an issue on GitHub.


