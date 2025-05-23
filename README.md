# Isometrik Chat Android

A simple chat library for Android applications.

<p float="left">
  <img src="docs/1.png" width="200" />
  <img src="docs/2.png" width="200" />
</p>

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
    implementation 'com.github.isometrikai:isometrik-chat-android:1.8.3'
}
```
### Step 3: Sync Your Project

Once you've made the changes, sync your project with the Gradle files by clicking the "Sync Now" button in Android Studio.


# SDK Integration Guide

Follow the steps below to integrate and configure the Isometrik UI SDK in your Android project.

## Step 1: SDK Initialization

Initialize the SDK in your project's `Application` class, specifically within the `onCreate()` method.

```java
IsometrikChatSdk.getInstance().sdkInitialize(this);
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

Provide these details using the parameters in the method below: userName & password are OPTIONAL DON'T PASS UNTIL REQUIRED.

```java
IsometrikChatSdk.getInstance()
        .createConfiguration(
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

        IsometrikChatSdk.getInstance()
                            .getUserSession()
                            .switchUser(isoMetricUserId, isoMetricToken, userName, userIdentifier,
                                    userProfilePic, false, new JSONObject(),true,0);

```

add `maps_api_key` in your base module `AndroidManifest.xml` file

```java

 <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="maps_api_key" />

```
To handle SDK termination, call the following method, usually in the onTerminate() method of the Application class:

```java
IsometrikChatSdk.getInstance().onTerminate();
```

## Step 3: Create a Connection

Establish a connection on the base screen of your app (e.g., `MainActivity` or `LandingActivity`) before accessing the chat functionality. You will need the following details:

- `userClientId` (isometrikUserId)
- `userIsometrikToken`

Use the following method to create a connection:

```java
IsometrikChatSdk.getInstance().getIsometrik().createConnection(userClientId, userIsometrikToken);
```

### Step 4: Start a Conversation
You are now ready to start a conversation. You can launch ConversationsListActivity or load ConversationsListFragment from any click action in your app:

```java
 Intent intent = new Intent(this, ConversationsActivity.class);
 startActivity(intent);
```
  for fragment
```java
ConversationsListFragment fragment = new ConversationsListFragment();

getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .commit();
```

### Create a Conversation
start new conversation from any action in your project

```java
  isometrik.getRemoteUseCases()
                .getConversationUseCases()
                .createConversation(new CreateConversationQuery.Builder().setUserToken(userToken)
                        .setGroup(false)
                        .setConversationType(0)
                        .setMembers(Collections.singletonList(selectedUserId))
                        .setReadEvents(enableMessageDeliveryReadEvents)
                        .setTypingEvents(enableMessageTypingEvents)
                        .setPushNotifications(enablePushNotifications)
                        .setSearchableTags(searchableTags)
                        .build(), (var1, var2) -> {
                    if (var1 != null) {
                        String conversationId = var1.getConversationId();
                        Intent intent =
                                      new Intent(YourActivity.this, ConversationMessagesActivity.class);
                                intent.putExtra("messageDeliveryReadEventsEnabled",true);
                                intent.putExtra("typingEventsEnabled",true);
                                intent.putExtra("newConversation", true);
                                intent.putExtra("conversationId", conversationId);
                                intent.putExtra("isPrivateOneToOne", true);
                                intent.putExtra("userName", your.userName);
                                intent.putExtra("userImageUrl", your.profilePicUrl);
                                intent.putExtra("isOnline", false);
                                intent.putExtra("lastSeenAt", "");
                                intent.putExtra("userId", your.userId);
                                startActivity(intent);
                    }
                });

```

open custom activity from `ConversationMessagesActivity`

```kotlin

  val i = Intent(this,ChatListActivity::class.java)
  ConversationMessagesActivity.startActivity(i)

```


# Customization

## Handle a Custom Activity from Your Module

To open a new screen for app module used below click listeners.

```kotlin

  IsometrikChatSdk.getInstance().addClickListeners(object : ChatActionsClickListener {

              override fun onCreateChatIconClicked(isGroup: Boolean) {
              // here default screen mentioned from SDK. We can respected screen from here.
                  if (isGroup) {
                        val i = Intent(
                          this,
                          io.isometrik.ui.conversations.newconversation.group.NewGroupConversationActivity::class.java
                      )
                      i.putExtra("conversationType", ConversationType.PrivateConversation.value)
                      startActivity(i)
                  } else {
                      val i = Intent(
                          this,
                          io.isometrik.ui.conversations.newconversation.onetoone.NewOneToOneConversationActivity::class.java
                      )
                      startActivity(i)
                  }

              }

              override fun onBlockStatusUpdate(isBlocked: Boolean, userId: String) {
              }

              override fun onCallClicked(
                  isAudio: Boolean,
                  userId: String,
                  meetingDescription: String,
                  opponentName: String,
                  opponentImageUrl: String
              ) {

              }

              override fun onSharedPostClick(postId: String) {

              }

              override fun onViewSocialProfileClick(userId: String) {

              }
          })

```

## Add custom view in ChatList Screen

Check default view [here](isometrik-chat/src/main/java/io/isometrik/ui/conversations/list/DefaultChatListItemBinder.kt)

```kotlin

     val customBinder = object : ChatListItemBinder<ConversationsModel, ChatItemBinding> {
            override fun createBinding(parent: ViewGroup): ChatItemBinding {
                return ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }

            override fun bindData(context: Context, binding: ChatItemBinding, data: ConversationsModel) {
                binding.chatName.text = data.conversationTitle
                binding.chatLastMessage.text = data.lastMessageSenderName

            }
        }

        val chatFragment = ConversationsListFragment.newInstance(customBinder)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, chatFragment)
            .commit()

```

## Add custom view for any type of Message in Chat Screen
Check total message type of UI [here](isometrik-chat/src/main/java/io/isometrik/chat/enums/MessageTypeUi.kt)

```kotlin

               MessageBinderRegistry.registerBinder(
                   MessageTypeUi.TEXT_MESSAGE_SENT,
                   CustomTextSentBinder()
               )

              MessageBinderRegistry.registerBinder(
                  MessageTypeUi.TEXT_MESSAGE_RECEIVED,
                  CustomTextSentBinder()
              )

```

## Add custom top view in Chat Screen

```kotlin

        class MyCustomTopViewHandler : ChatTopViewHandler {

                   private var binding: CustomTopViewBinding? = null

                   override fun createTopView(parent: ViewGroup): View {
                       val inflater = LayoutInflater.from(parent.context)
                       binding = CustomTopViewBinding.inflate(inflater, parent, false)
                       return binding!!.root
                   }

                   override fun updateTopView(view: View, message: MessagesModel) {
                       binding?.apply {
                           rootView.visibility = View.VISIBLE
                           tvTitle.text = message.textMessage
                       }
                   }
               }


               ChatConfig.topViewHandler = MyCustomTopViewHandler()

```

### Change base colors, default text, drawables and visibility of options

Modify below file to update base color and other initial customisation.

```kotlin

ChatConfig.baseColor = R.color.your_base_color
ChatConfig.chatBackGroundColor = R.color.your_bg_color

ChatConfig.noConversationsStringResId = R.string.your_text
ChatConfig.noConversationsImageResId = R.drawable.your_image
ChatConfig.noConversationsImageResId = R.drawable.your_ima
ChatConfig.hideCreateChatOption = true
ChatConfig.hideAudioCallOption = true
ChatConfig.hideVideoCallOption = true
ChatConfig.hideCaptureCameraOption = true
ChatConfig.hideRecordAudioOption = true

// if conversation initiated but not started then don't show in UI
ChatConfig.hideNotStartedConversationInChatList = true

```
Realtime update view

```kotlin

 ChatConfig.updateBottomTypingView(false)
```

### Manage attachments options visibility by below file

```kotlin

AttachmentsConfig.hideCameraOption = true
AttachmentsConfig.hideRecordVideoOption = true
AttachmentsConfig.hidePhotosOption  = true
AttachmentsConfig.hideVideosOption = true
AttachmentsConfig.hideFilesOption = true
AttachmentsConfig.hideLocationOption = true
AttachmentsConfig.hideContactOption = true
AttachmentsConfig.hideStickerOption = true
AttachmentsConfig.hideGIFOption = true

```

### Prevent Toast to show
Provide list of exact message that should not show in app

```kotlin

ChatConfig.dontShowToastList = arrayListOf("conversation not found")

```

## Custom Message Types

The SDK allows you to register and handle custom message types for your chat application. This feature enables you to extend the chat functionality with your own message types while maintaining compatibility with the existing system.

### Registering Custom Message Types

To register a custom message type, use the `registerCustomType` method from `CustomMessageTypes`:

```kotlin
// Register a custom message type
CustomMessageTypes.registerCustomType(
    typeName = "POLL",  // The name of your custom message type
    value = "poll_message"  // The value string for the custom message type
)
```

### Registering Custom Views for Custom Message Types

You can register custom views for your custom message types using the `registerCustomBinder` method:

```kotlin
// Create your custom binders
class PollMessageSentBinder : MessageItemBinder<MessagesModel, PollMessageBinding> {
    override fun createBinding(parent: ViewGroup): PollMessageBinding {
        return PollMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(context: Context, binding: PollMessageBinding, data: MessagesModel) {
        // Bind your custom view data here
        binding.pollQuestion.text = data.textMessage
        // ... bind other data
    }
}

class PollMessageReceivedBinder : MessageItemBinder<MessagesModel, PollMessageBinding> {
    // Similar implementation for received messages
}

// Register the binders for your custom type
CustomMessageTypes.registerCustomBinder(
    value = "poll_message",
    sentBinder = PollMessageSentBinder(),
    receivedBinder = PollMessageReceivedBinder()
)
```

### Example Usage

Here's a complete example of how to use custom message types with custom views:

```kotlin
// Register the custom type
CustomMessageTypes.registerCustomType(
    typeName = "POLL",
    value = "poll_message"
)

// Register custom binders
CustomMessageTypes.registerCustomBinder(
    value = "poll_message",
    sentBinder = PollMessageSentBinder(),
    receivedBinder = PollMessageReceivedBinder()
)

```


## Update existing message with new data in ChatScreen

```kotlin
    ChatConfig.replaceMessageModelInChatScreen(newMessagesModel)

```

### Best Practices

1. Choose descriptive and unique `typeName` values that clearly indicate the purpose of your custom message type
2. Use consistent `value` strings across your application for the same type of custom message
3. Include relevant metadata in the message to support your custom message type's functionality
4. Create separate binders for sent and received messages to handle different UI requirements
5. Make sure your custom views handle all possible states of the message (loading, error, etc.)

# Technical details

* compileSdkVersion  = 34
* buildToolsVersion = '34.0.0'
* targetSdkVersion 34
* Android gradle plugin 8.4.0
* sourceCompatibility 17
* targetCompatibility 17
* JDK version 17
* Kotlin version 1.9.23
* One Signal version 5.1.21


