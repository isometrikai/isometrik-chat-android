package io.isometrik.ui.conversations.list;

import android.util.Log;

import androidx.annotation.Nullable;

import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.events.conversation.CreateConversationEvent;
import io.isometrik.chat.response.conversation.utils.Conversation;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.reaction.util.ReactionPlaceHolderIconHelper;
import io.isometrik.chat.utils.TimeUtil;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class for inflating conversation items for open/public and all
 * conversations, search
 * conversation results and conversation created event.
 */
public class ConversationsModel {

    private boolean privateOneToOneConversation;
    private String conversationId, opponentId, opponentIdentifier;
    private boolean remoteUserTyping;
    private boolean online, lastMessageWasReactionMessage;
    private boolean messageDeliveryReadEventsEnabled, typingEventsEnabled;
    private String conversationTitle, conversationImageUrl;
    private int unreadMessagesCount, conversationMembersCount;
    private String conversationTypeText, lastMessageTime, lastMessageText,
            lastMessageSendersProfileImageUrl, membersCountText, lastMessageSenderName,
            remoteUserTypingMessage;
    private Integer lastMessagePlaceHolderImage;
    private long lastSeenAt;
    private final boolean canJoin;
    private boolean messagingDisabled;
    private JSONObject metaData;
    private boolean lastMessageReadByAll, lastMessageDeliveredToAll;
    private String lastMessageCustomType;

    /**
     * Instantiates a new Conversations model.
     *
     * @param createConversationEvent the create conversation event
     */
    public ConversationsModel(CreateConversationEvent createConversationEvent) {
        canJoin = false;
        messagingDisabled = false;
        remoteUserTyping = false;
        int conversationType = createConversationEvent.getConversationDetails().getConversationType();

        if (conversationType == ConversationType.PrivateConversation.getValue()) {
            if (createConversationEvent.getConversationDetails().isGroup()) {
                conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_private);
            } else {
                conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_1_1);

            }
        } else if (conversationType == ConversationType.PublicConversation.getValue()) {
            conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_public);
        } else if (conversationType == ConversationType.OpenConversation.getValue()) {
            conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_open);
        }

        lastMessageTime = TimeUtil.formatTimestampToOnlyDate(
                createConversationEvent.getConversationDetails().getCreatedAt());

        messageDeliveryReadEventsEnabled = createConversationEvent.getConversationDetails().getConfig().isReadEvents();
        typingEventsEnabled = createConversationEvent.getConversationDetails().getConfig().isTypingEvents();
        privateOneToOneConversation = createConversationEvent.getConversationDetails().isPrivateOneToOne();
        unreadMessagesCount = (IsometrikChatSdk.getInstance()
                .getUserSession()
                .getUserId()
                .equals(createConversationEvent.getConversationDetails().getCreatedBy())) ? 0 : 1;
        conversationId = createConversationEvent.getConversationId();
        lastMessageWasReactionMessage = false;
        lastMessagePlaceHolderImage = null;
        lastMessageSenderName = createConversationEvent.getConversationDetails().getCreatedByUserName();
        lastMessageSendersProfileImageUrl = createConversationEvent.getConversationDetails().getCreatedByUserImageUrl();
        lastMessageText = IsometrikChatSdk.getInstance()
                .getContext()
                .getString(R.string.ism_created_conversation,
                        createConversationEvent.getConversationDetails().getCreatedByUserName());

        if (privateOneToOneConversation) {
            if (createConversationEvent.getConversationDetails()
                    .getOpponentDetails()
                    .getUserId()
                    .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                List<ConversationMember> members = createConversationEvent.getConversationDetails()
                        .getConversationMembers();

                for (int i = 0; i < members.size(); i++) {

                    if (!members.get(i)
                            .getUserId()
                            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                        ConversationMember conversationMember = members.get(i);
                        opponentId = conversationMember.getUserId();
                        opponentIdentifier = conversationMember.getUserIdentifier();
                        conversationImageUrl = conversationMember.getUserProfileImageUrl();
                        conversationTitle = conversationMember.getUserName();
                        online = true;
                        lastSeenAt = System.currentTimeMillis();

                        break;
                    }
                }
            } else {

                opponentId = createConversationEvent.getConversationDetails().getOpponentDetails().getUserId();
                opponentIdentifier = createConversationEvent.getConversationDetails().getOpponentDetails()
                        .getUserIdentifier();
                conversationImageUrl = createConversationEvent.getConversationDetails()
                        .getOpponentDetails()
                        .getUserProfileImageUrl();
                conversationTitle = createConversationEvent.getConversationDetails().getOpponentDetails().getUserName();
                online = createConversationEvent.getConversationDetails().getOpponentDetails().isOnline();

                lastSeenAt = createConversationEvent.getConversationDetails().getOpponentDetails().getLastSeen();
            }
        } else {

            conversationImageUrl = createConversationEvent.getConversationDetails().getConversationImageUrl();
            conversationTitle = createConversationEvent.getConversationDetails().getConversationTitle();
        }

        conversationMembersCount = createConversationEvent.getConversationDetails().getMembersCount();
        lastMessageCustomType = createConversationEvent.getConversationDetails().getCustomType();

    }

    /**
     * Instantiates a new Conversations model.
     *
     * @param conversation the conversation
     * @param canJoin      the can join
     * @param canObserve   the can observe
     */
    public ConversationsModel(Conversation conversation, boolean canJoin, boolean canObserve) {
        Log.e("ConversationsModel", "==> 11" + lastMessageText);
        this.canJoin = canJoin;
        remoteUserTyping = false;
        messagingDisabled = conversation.isMessagingDisabled();
        parseConversationMessage(conversation);
        if (canJoin || canObserve) {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_member;
            lastMessageSendersProfileImageUrl = null;
            lastMessageText = IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_members_count, conversation.getMembersCount());
            lastMessageTime = TimeUtil.formatTimestampToOnlyDate(conversation.getCreatedAt());
        }
        metaData = conversation.getMetaData();
        JSONObject lastMessageDetails = conversation.getLastMessageDetails();
        try {
            if (lastMessageDetails.has("customType")) {
                lastMessageCustomType = lastMessageDetails.getString("customType");
            }
            if (!conversation.isGroup()) {
                if (lastMessageDetails.has("deliveredTo")
                        && lastMessageDetails.getJSONArray("deliveredTo").length() == 1
                        && !lastMessageDetails.getJSONArray("deliveredTo").getJSONObject(0).getString("userId")
                                .isBlank()
                        && lastMessageDetails.has("readBy") && lastMessageDetails.getJSONArray("readBy").length() == 1
                        && !lastMessageDetails.getJSONArray("readBy").getJSONObject(0).getString("userId").isBlank()) {
                    lastMessageDeliveredToAll = true;
                    lastMessageReadByAll = true;
                } else if (lastMessageDetails.has("deliveredTo")
                        && lastMessageDetails.getJSONArray("deliveredTo").length() == 1
                        && !lastMessageDetails.getJSONArray("deliveredTo").getJSONObject(0).getString("userId")
                                .isBlank()
                        && lastMessageDetails.has("readBy")
                        && lastMessageDetails.getJSONArray("readBy").length() == 0) {
                    lastMessageDeliveredToAll = true;
                    lastMessageReadByAll = false;
                } else {
                    lastMessageReadByAll = false;
                    lastMessageDeliveredToAll = false;
                }
            } else if (conversation.isGroup() && conversation.getMembersCount() <= 2) {
                lastMessageDeliveredToAll = true;
                if (lastMessageDetails.has("deliveredTo")
                        && lastMessageDetails.getJSONArray("deliveredTo").length() == 1
                        && !lastMessageDetails.getJSONArray("deliveredTo").getJSONObject(0).getString("userId")
                                .isBlank()
                        && lastMessageDetails.has("readBy") && lastMessageDetails.getJSONArray("readBy").length() == 1
                        && !lastMessageDetails.getJSONArray("readBy").getJSONObject(0).getString("userId").isBlank()) {
                    lastMessageReadByAll = true;
                }
            }
        } catch (JSONException ignore) {
        }

        Log.e("ConversationsModel", "==> 22" + lastMessageText);

    }

    /**
     * Instantiates a new Conversations model.
     *
     * @param conversation the conversation
     */
    // For search results
    public ConversationsModel(Conversation conversation) {
        canJoin = false;
        messagingDisabled = conversation.isMessagingDisabled();
        remoteUserTyping = false;
        parseConversationMessage(conversation);
        membersCountText = IsometrikChatSdk.getInstance()
                .getContext()
                .getString(R.string.ism_members_count, conversation.getMembersCount());
        lastMessageCustomType = conversation.getCustomType();
    }

    private void parseConversationMessage(Conversation conversation) {
        int conversationType = conversation.getConversationType();

        if (conversationType == ConversationType.PrivateConversation.getValue()) {
            if (conversation.isGroup()) {
                conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_private);
            } else {
                conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_1_1);
            }
        } else if (conversationType == ConversationType.PublicConversation.getValue()) {
            conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_public);
        } else if (conversationType == ConversationType.OpenConversation.getValue()) {
            conversationTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_open);
        }

        messageDeliveryReadEventsEnabled = conversation.getConfig().isReadEvents();
        typingEventsEnabled = conversation.getConfig().isTypingEvents();
        privateOneToOneConversation = conversation.isPrivateOneToOne();
        unreadMessagesCount = conversation.getUnreadMessagesCount();
        conversationId = conversation.getConversationId();
        JSONObject lastMessage = conversation.getLastMessageDetails();
        lastMessageWasReactionMessage = false;
        lastMessageText = ""; // Initialize to empty string to prevent null
        try {
            lastMessageTime = TimeUtil.formatTimestampToOnlyDate(lastMessage.getLong("sentAt"));

            boolean conversationStatusMessage = lastMessage.has("conversationStatusMessage");

            if (conversationStatusMessage) {

                switch (lastMessage.getString("action")) {

                    case "observerJoin": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_observer_joined, lastMessage.getString("userName"));
                        break;
                    }

                    case "observerLeave": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_observer_left, lastMessage.getString("userName"));
                        break;
                    }
                    case "userBlockConversation": {
                        lastMessagePlaceHolderImage = null;
                        String initiatorName = lastMessage.getString("initiatorName");
                        String opponentName = lastMessage.getString("opponentName");
                        String currentUserName = IsometrikChatSdk.getInstance().getUserSession().getUserName();

                        lastMessageSenderName = initiatorName;
                        lastMessageSendersProfileImageUrl = lastMessage.getString("initiatorProfileImageUrl");

                        if (opponentName.equals(currentUserName)) {
                            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                                    R.string.ism_blocked_user_text, initiatorName, "You");
                        } else if (initiatorName.equals(currentUserName)) {
                            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                                    R.string.ism_blocked_user_text, "You", opponentName);
                        } else {
                            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                                    R.string.ism_blocked_user, initiatorName, opponentName);
                        }
                        break;
                    }

                    case "userUnblockConversation": {
                        lastMessagePlaceHolderImage = null;
                        String initiatorName = lastMessage.getString("initiatorName");
                        String opponentName = lastMessage.getString("opponentName");
                        String currentUserName = IsometrikChatSdk.getInstance().getUserSession().getUserName();

                        lastMessageSenderName = initiatorName;
                        lastMessageSendersProfileImageUrl = lastMessage.getString("initiatorProfileImageUrl");

                        if (opponentName.equals(currentUserName)) {
                            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                                    R.string.ism_unblocked_user_text, initiatorName, "You");
                        } else if (initiatorName.equals(currentUserName)) {
                            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                                    R.string.ism_unblocked_user_text, "You", opponentName);
                        } else {
                            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                                    R.string.ism_unblocked_user, initiatorName, opponentName);
                        }
                        break;
                    }

                    case "conversationCreated": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_created_conversation, lastMessage.getString("userName"));
                        break;
                    }

                    case "addAdmin": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("initiatorName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("initiatorProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_made_admin, lastMessage.getString("memberName"),
                                        lastMessage.getString("initiatorName"));
                        break;
                    }

                    case "removeAdmin": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("initiatorName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("initiatorProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_removed_admin, lastMessage.getString("memberName"),
                                        lastMessage.getString("initiatorName"));
                        break;
                    }

                    case "clearConversation": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSendersProfileImageUrl = null;
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_cleared_conversation);
                        break;
                    }

                    case "memberJoin": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_joined_public, lastMessage.getString("userName"));
                        break;
                    }

                    case "memberLeave": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_member_left, lastMessage.getString("userName"));
                        break;
                    }

                    case "membersAdd": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        StringBuilder membersAdded = new StringBuilder();
                        JSONArray members = lastMessage.getJSONArray("members");
                        int size = members.length();
                        for (int i = 0; i < size; i++) {
                            membersAdded.append(", ").append(members.getJSONObject(i).getString("memberName"));
                        }
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_members_added, lastMessage.getString("userName"),
                                        membersAdded.substring(2));
                        break;
                    }
                    case "membersRemove": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        StringBuilder membersRemoved = new StringBuilder();
                        JSONArray members = lastMessage.getJSONArray("members");
                        int size = members.length();
                        for (int i = 0; i < size; i++) {
                            membersRemoved.append(", ").append(members.getJSONObject(i).getString("memberName"));
                        }

                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_members_removed, lastMessage.getString("userName"),
                                        membersRemoved.substring(2));
                        break;
                    }

                    case "conversationImageUpdated": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_updated_conversation_image,
                                        lastMessage.getString("userName"));
                        break;
                    }

                    case "conversationTitleUpdated": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_updated_conversation_title,
                                        lastMessage.getString("userName"), lastMessage.getString("conversationTitle"));
                        break;
                    }

                    case "reactionAdd": {
                        lastMessageWasReactionMessage = true;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessagePlaceHolderImage = ReactionPlaceHolderIconHelper.fetchLastMessagePlaceHolderIcon(
                                lastMessage.getString("reactionType"));
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_reaction_added, lastMessage.getString("userName"));
                        break;
                    }

                    case "reactionRemove": {
                        lastMessageWasReactionMessage = true;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessagePlaceHolderImage = ReactionPlaceHolderIconHelper.fetchLastMessagePlaceHolderIcon(
                                lastMessage.getString("reactionType"));
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_reaction_removed, lastMessage.getString("userName"));
                        break;
                    }
                    case "messagesDeleteLocal": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_message_deleted_locally, lastMessage.getString("userName"));
                        break;
                    }

                    case "messagesDeleteForAll": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");
                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_message_deleted_for_all, lastMessage.getString("userName"));
                        break;
                    }

                    case "conversationSettingsUpdated": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");

                        JSONObject config = lastMessage.getJSONObject("config");

                        String settingsUpdated = "";
                        if (config.has("config.typingEvents")) {
                            settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_settings_typing);
                        }
                        if (config.has("config.readEvents")) {
                            settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_settings_read_delivery_events);
                        }
                        if (config.has("config.pushNotifications")) {
                            settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_settings_notifications);
                        }

                        lastMessageText = IsometrikChatSdk.getInstance()
                                .getContext()
                                .getString(R.string.ism_updated_settings, lastMessage.getString("userName"),
                                        settingsUpdated.substring(2));
                        break;
                    }

                    case "conversationDetailsUpdated": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");

                        JSONObject details = lastMessage.getJSONObject("details");

                        String detailsUpdated = "";
                        if (details.has("customType")) {
                            detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_details_custom_type);
                        }
                        if (details.has("metadata")) {
                            detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_details_metadata);
                        }
                        if (details.has("searchableTags")) {
                            detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_details_searchable_tags);
                        }

                        if (detailsUpdated.length() > 2) {
                            lastMessageText = IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_updated_conversation_details,
                                            lastMessage.getString("userName"), detailsUpdated.substring(2));
                        } else {
                            lastMessageText = "Conversation updated";
                        }
                        break;
                    }

                    case "messageDetailsUpdated": {
                        lastMessagePlaceHolderImage = null;
                        lastMessageSenderName = lastMessage.getString("userName");
                        lastMessageSendersProfileImageUrl = lastMessage.getString("userProfileImageUrl");

                        JSONObject details = lastMessage.getJSONObject("details");

                        String detailsUpdated = "";
                        if (details.has("customType")) {
                            detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_details_custom_type);
                        }
                        if (details.has("metadata")) {
                            detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_details_metadata);
                        }
                        if (details.has("searchableTags")) {
                            detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_details_searchable_tags);
                        }
                        // if (details.has("body")) {
                        // detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                        // .getContext()
                        // .getString(R.string.ism_details_body);
                        // }
                        if (details.has("body")) {
                            detailsUpdated = details.getString("body");
                            lastMessageText = IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_updated_message_details, lastMessage.getString("userName"),
                                            detailsUpdated);
                        } else {

                            lastMessageText = IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_updated_message_details, lastMessage.getString("userName"),
                                            detailsUpdated.substring(2));
                        }
                        break;
                    }
                }
            } else {
                lastMessageSenderName = lastMessage.getString("senderName");
                lastMessageSendersProfileImageUrl = lastMessage.getString("senderProfileImageUrl");
                switch (lastMessage.getString("customType")) {
                    case "AttachmentMessage:Text": {
                        lastMessagePlaceHolderImage = null;
                        if (lastMessage.isNull("parentMessageId")) {

                            if (!lastMessage.isNull("action")) {
                                // action not received for normal messages

                                if ("forward".equals(lastMessage.getString("action"))) {
                                    lastMessagePlaceHolderImage = R.drawable.ism_ic_forward;
                                }
                            }
                        } else {
                            lastMessagePlaceHolderImage = R.drawable.ism_ic_quote;
                        }

                        lastMessageText = lastMessage.getString("body");
                        break;
                    }
                    case "AttachmentMessage:Image": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_picture;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo);
                        break;
                    }
                    case "AttachmentMessage:Payment Request": {
                        lastMessagePlaceHolderImage = null;
                        JSONObject details = lastMessage.getJSONObject("metaData");
                        long currentTimeSeconds = System.currentTimeMillis() / 1000;
                        long sentTimeSeconds = lastMessage.getLong("sentAt") / 1000;
                        long requestAPaymentExpiryTimeInMinutes = details.getLong("requestAPaymentExpiryTime");
                        long expirationTimeSeconds = sentTimeSeconds + (requestAPaymentExpiryTimeInMinutes * 60);
                        boolean isExpired = currentTimeSeconds > expirationTimeSeconds;
                        JSONArray members = details.getJSONArray("paymentRequestedMembers");
                        for (int i = 0; i < members.length(); i++) {
                            JSONObject member = members.getJSONObject(i);
                            String userId = member.getString("userId");
                            if (isExpired) {
                                lastMessageText = "This payment request has expired.";
                            } else {
                                lastMessageText = userId
                                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())
                                                ? "You sent a payment request."
                                                : lastMessageSenderName + " sent you a payment request.";
                            }
                        }
                        break;
                    }
                    case "AttachmentMessage:Video": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_video;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video);
                        break;
                    }
                    case "AttachmentMessage:Audio": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_mic;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext()
                                .getString(R.string.ism_audio_recording);
                        break;
                    }
                    case "AttachmentMessage:File": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_file;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file);
                        break;
                    }
                    case "AttachmentMessage:Sticker": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_sticker;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_sticker);
                        break;
                    }
                    case "AttachmentMessage:Gif": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_gif;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_gif);
                        break;
                    }
                    case "AttachmentMessage:Whiteboard": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_whiteboard;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext()
                                .getString(R.string.ism_whiteboard);
                        break;
                    }
                    case "AttachmentMessage:Location": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_location;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_location);
                        break;
                    }
                    case "AttachmentMessage:Contact": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_contact;
                        lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_contact);
                        break;
                    }
                    case "AttachmentMessage:Reply": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_quote;
                        lastMessageText = lastMessage.getString("body");
                        break;
                    }
                    case "AudioCall": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_audio_call;
                        lastMessageText = "Voice Call";
                        break;
                    }
                    case "VideoCall": {
                        lastMessagePlaceHolderImage = R.drawable.ism_ic_video_call;
                        lastMessageText = "Video Call";
                        break;
                    }
                    default: {
                        // Handle any unhandled custom types - set to empty string to avoid null
                        if (lastMessageText == null) {
                            lastMessageText = "";
                        }
                        break;
                    }
                }
            }
        } catch (JSONException ignore) {
        }

        if (privateOneToOneConversation) {
            conversationImageUrl = conversation.getOpponentDetails().getUserProfileImageUrl();
            conversationTitle = conversation.getOpponentDetails().getUserName();
            online = conversation.getOpponentDetails().isOnline();
            opponentId = conversation.getOpponentDetails().getUserId();
            opponentIdentifier = conversation.getOpponentDetails().getUserIdentifier();

            lastSeenAt = conversation.getOpponentDetails().getLastSeen();
            if (opponentId == null) {
                conversationTitle = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
                messagingDisabled = true;
            }
        } else {

            conversationImageUrl = conversation.getConversationImageUrl();
            conversationTitle = conversation.getConversationTitle();
        }

        conversationMembersCount = conversation.getMembersCount();
        lastMessageCustomType = conversation.getCustomType();
    }

    /**
     * Is private one to one conversation boolean.
     *
     * @return the boolean
     */
    public boolean isPrivateOneToOneConversation() {
        return privateOneToOneConversation;
    }

    /**
     * Is online boolean.
     *
     * @return the boolean
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Gets conversation title.
     *
     * @return the conversation title
     */
    public String getConversationTitle() {
        return conversationTitle;
    }

    /**
     * Gets unread messages count.
     *
     * @return the unread messages count
     */
    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    /**
     * Gets last message time.
     *
     * @return the last message time
     */
    public String getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * Gets last message text.
     *
     * @return the last message text
     */
    public String getLastMessageText() {
        return lastMessageText;
    }

    /**
     * Gets last message place holder image.
     *
     * @return the last message place holder image
     */
    public Integer getLastMessagePlaceHolderImage() {
        return lastMessagePlaceHolderImage;
    }

    /**
     * Gets conversation image url.
     *
     * @return the conversation image url
     */
    public String getConversationImageUrl() {
        return conversationImageUrl;
    }

    /**
     * Gets last message senders profile image url.
     *
     * @return the last message senders profile image url
     */
    public String getLastMessageSendersProfileImageUrl() {
        return lastMessageSendersProfileImageUrl;
    }

    /**
     * Gets conversation id.
     *
     * @return the conversation id
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Gets last seen at.
     *
     * @return the last seen at
     */
    public long getLastSeenAt() {
        return lastSeenAt;
    }

    /**
     * Gets conversation members count.
     *
     * @return the conversation members count
     */
    public int getConversationMembersCount() {
        return conversationMembersCount;
    }

    /**
     * Is last message was reaction message boolean.
     *
     * @return the boolean
     */
    public boolean isLastMessageWasReactionMessage() {
        return lastMessageWasReactionMessage;
    }

    /**
     * Is message delivery read events enabled boolean.
     *
     * @return the boolean
     */
    public boolean isMessageDeliveryReadEventsEnabled() {
        return messageDeliveryReadEventsEnabled;
    }

    /**
     * Is typing events enabled boolean.
     *
     * @return the boolean
     */
    public boolean isTypingEventsEnabled() {
        return typingEventsEnabled;
    }

    /**
     * Sets last message was reaction message.
     *
     * @param lastMessageWasReactionMessage the last message was reaction message
     */
    public void setLastMessageWasReactionMessage(boolean lastMessageWasReactionMessage) {
        this.lastMessageWasReactionMessage = lastMessageWasReactionMessage;
    }

    /**
     * Sets last message time.
     *
     * @param lastMessageTime the last message time
     */
    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * Sets last message text.
     *
     * @param lastMessageText the last message text
     */
    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    /**
     * Sets last message place holder image.
     *
     * @param lastMessagePlaceHolderImage the last message place holder image
     */
    public void setLastMessagePlaceHolderImage(Integer lastMessagePlaceHolderImage) {
        this.lastMessagePlaceHolderImage = lastMessagePlaceHolderImage;
    }

    /**
     * Sets last message senders profile image url.
     *
     * @param lastMessageSendersProfileImageUrl the last message senders profile
     *                                          image url
     */
    public void setLastMessageSendersProfileImageUrl(String lastMessageSendersProfileImageUrl) {
        this.lastMessageSendersProfileImageUrl = lastMessageSendersProfileImageUrl;
    }

    /**
     * Gets last message sender name.
     *
     * @return the last message sender name
     */
    public String getLastMessageSenderName() {
        return lastMessageSenderName;
    }

    /**
     * Sets last message sender name.
     *
     * @param lastMessageSenderName the last message sender name
     */
    public void setLastMessageSenderName(String lastMessageSenderName) {
        this.lastMessageSenderName = lastMessageSenderName;
    }

    /**
     * Sets message delivery read events enabled.
     *
     * @param messageDeliveryReadEventsEnabled the message delivery read events
     *                                         enabled
     */
    public void setMessageDeliveryReadEventsEnabled(boolean messageDeliveryReadEventsEnabled) {
        this.messageDeliveryReadEventsEnabled = messageDeliveryReadEventsEnabled;
    }

    /**
     * Sets typing events enabled.
     *
     * @param typingEventsEnabled the typing events enabled
     */
    public void setTypingEventsEnabled(boolean typingEventsEnabled) {
        this.typingEventsEnabled = typingEventsEnabled;
    }

    /**
     * Sets conversation title.
     *
     * @param conversationTitle the conversation title
     */
    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    /**
     * Sets conversation image url.
     *
     * @param conversationImageUrl the conversation image url
     */
    public void setConversationImageUrl(String conversationImageUrl) {
        this.conversationImageUrl = conversationImageUrl;
    }

    /**
     * Sets unread messages count.
     *
     * @param unreadMessagesCount the unread messages count
     */
    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    /**
     * Sets conversation members count.
     *
     * @param conversationMembersCount the conversation members count
     */
    public void setConversationMembersCount(int conversationMembersCount) {
        this.conversationMembersCount = conversationMembersCount;
    }

    /**
     * Gets conversation type text.
     *
     * @return the conversation type text
     */
    public String getConversationTypeText() {
        return conversationTypeText;
    }

    /**
     * Is can join boolean.
     *
     * @return the boolean
     */
    public boolean isCanJoin() {
        return canJoin;
    }

    /**
     * Gets members count text.
     *
     * @return the members count text
     */
    public String getMembersCountText() {
        return membersCountText;
    }

    /**
     * Gets opponent id.
     *
     * @return the opponent id
     */
    public String getOpponentId() {
        return opponentId;
    }

    /**
     * Is messaging disabled boolean.
     *
     * @return the boolean
     */
    public boolean isMessagingDisabled() {
        return messagingDisabled;
    }

    /**
     * Is remote user typing boolean.
     *
     * @return the boolean
     */
    public boolean isRemoteUserTyping() {
        return remoteUserTyping;
    }

    /**
     * Sets remote user typing.
     *
     * @param remoteUserTyping the remote user typing
     */
    public void setRemoteUserTyping(boolean remoteUserTyping) {
        this.remoteUserTyping = remoteUserTyping;
    }

    /**
     * Gets remote user typing message.
     *
     * @return the remote user typing message
     */
    public String getRemoteUserTypingMessage() {
        return remoteUserTypingMessage;
    }

    /**
     * Sets remote user typing message.
     *
     * @param remoteUserTypingMessage the remote user typing message
     */
    public void setRemoteUserTypingMessage(String remoteUserTypingMessage) {
        this.remoteUserTypingMessage = remoteUserTypingMessage;
    }

    /**
     * Sets messaging disabled.
     *
     * @param messagingDisabled the messaging disabled
     */
    public void setMessagingDisabled(boolean messagingDisabled) {
        this.messagingDisabled = messagingDisabled;
    }

    /**
     * Gets remote user identifier
     */
    public String getOpponentIdentifier() {
        return opponentIdentifier;
    }

    public JSONObject getMetaData() {
        return metaData;
    }

    public void setMetaData(JSONObject metaData) {
        this.metaData = metaData;
    }

    public boolean isLastMessageReadByAll() {
        return lastMessageReadByAll;
    }

    public boolean isLastMessageDeliveredToAll() {
        return lastMessageDeliveredToAll;
    }

    @Nullable
    public String getLastMessageCustomType() {
        return lastMessageCustomType;
    }

    public void setLastMessageCustomType(String lastMessageCustomType) {
        this.lastMessageCustomType = lastMessageCustomType;
    }
}
