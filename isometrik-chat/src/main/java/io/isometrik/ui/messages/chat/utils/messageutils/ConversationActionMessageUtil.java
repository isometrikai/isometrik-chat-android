package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.chat.response.message.utils.fetchmessages.AddRemoveMember;
import io.isometrik.chat.response.message.utils.fetchmessages.Config;
import io.isometrik.chat.response.message.utils.fetchmessages.Details;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import java.util.ArrayList;

/**
 * The helper class to parse conversation action message.
 */
public class ConversationActionMessageUtil {

  /**
   * Parse conversation action message string.
   *
   * @param message the message
   * @return the string
   */
  public static String parseConversationActionMessage(Message message) {
    String conversationActionMessage = null;
    switch (message.getAction()) {

      case "observerJoin":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_observer_joined, message.getUserName());
        break;

      case "observerLeave":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_observer_left, message.getUserName());
        break;

      case "userBlockConversation":
        if (message.getOpponentName().equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
          conversationActionMessage = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_blocked_user_text, message.getInitiatorName(),"You");
        } else if (message.getInitiatorName().equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
          conversationActionMessage = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_blocked_user_text, "You",
                          message.getOpponentName());
        } else {
          conversationActionMessage = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_blocked_user, message.getInitiatorName(),
                          message.getOpponentName());
        }
        break;

      case "userUnblockConversation":
        if (message.getOpponentName().equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
          conversationActionMessage = IsometrikChatSdk.getInstance().getContext()
                  .getString(R.string.ism_unblocked_user_text, message.getInitiatorName(),"You");
        } else if (message.getInitiatorName().equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
          conversationActionMessage = IsometrikChatSdk.getInstance().getContext()
                  .getString(R.string.ism_unblocked_user_text, "You",message.getOpponentName());
        } else {
          conversationActionMessage = IsometrikChatSdk.getInstance().getContext()
                  .getString(R.string.ism_unblocked_user, message.getInitiatorName(),
                          message.getOpponentName());
        }
        break;

      case "conversationCreated":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_created_conversation, message.getUserName());
        break;

      case "addAdmin":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_made_admin, message.getMemberName(),
                message.getInitiatorName());
        break;

      case "removeAdmin":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_removed_admin, message.getMemberName(),
                message.getInitiatorName());
        break;

      case "clearConversation":
        conversationActionMessage =
            IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_cleared_conversation);
        break;

      case "memberJoin":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_joined_public, message.getUserName());
        break;

      case "memberLeave":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_left, message.getUserName());
        break;

      case "membersAdd": {
        StringBuilder membersAdded = new StringBuilder();
        ArrayList<AddRemoveMember> members = message.getAddRemoveMembers();

        for (int j = 0; j < members.size(); j++) {
          membersAdded.append(", ").append(members.get(j).getMemberName());
        }
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_members_added, message.getUserName(),
                membersAdded.substring(2));
        break;
      }
      case "membersRemove": {
        StringBuilder membersRemoved = new StringBuilder();
        ArrayList<AddRemoveMember> members = message.getAddRemoveMembers();

        for (int j = 0; j < members.size(); j++) {
          membersRemoved.append(", ").append(members.get(j).getMemberName());
        }

        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_members_removed, message.getUserName(),
                membersRemoved.substring(2));
        break;
      }

      case "conversationImageUpdated":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_updated_conversation_image, message.getUserName());
        break;

      case "conversationTitleUpdated":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_updated_conversation_title, message.getUserName(),message.getConversationTitle());
        break;

      //case "reactionAdd":
      //  conversationActionMessage = IsometrikChatSdk.getInstance()
      //      .getContext()
      //      .getString(R.string.ism_reaction_added, message.getUserName());
      //  break;
      //
      //case "reactionRemove": {
      //  conversationActionMessage = IsometrikChatSdk.getInstance()
      //      .getContext()
      //      .getString(R.string.ism_reaction_removed, message.getUserName());
      //  break;
      //}
      case "messagesDeleteLocal":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_message_deleted_locally, message.getUserName());
        break;

      case "messagesDeleteForAll":
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_message_deleted_for_all, message.getUserName());
        break;

      case "conversationSettingsUpdated": {
        Config config = message.getConfig();

        String settingsUpdated = "";
        if (config.getTypingEvents() != null) {
          settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_settings_typing);
        }
        if (config.getReadEvents() != null) {
          settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_settings_read_delivery_events);
        }
        if (config.getPushNotifications() != null) {
          settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_settings_notifications);
        }

        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_updated_settings, message.getUserName(),
                settingsUpdated.substring(2));
        break;
      }

      case "conversationDetailsUpdated": {
        Details details = message.getDetails();

        String detailsUpdated = "";
        if (details.getCustomType() != null) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_custom_type);
        }
        if (details.getMetadata() != null && details.getMetadata().length() > 0) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_metadata);
        }
        if (details.getSearchableTags() != null) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_searchable_tags);
        }

        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_updated_conversation_details, message.getUserName(),
                detailsUpdated.substring(2));
        break;
      }

      case "messageDetailsUpdated": {
        Details details = message.getDetails();

        String detailsUpdated = "";
        if (details.getCustomType() != null) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_custom_type);
        }
        if (details.getMetadata() != null && details.getMetadata().length() > 0) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_metadata);
        }
        if (details.getSearchableTags() != null) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_searchable_tags);
        }
        if (details.getBody() != null) {
          detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_details_body);
        }
        conversationActionMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_updated_message_details, message.getUserName(),
                detailsUpdated.substring(2));
        break;
      }
    }

    return conversationActionMessage;
  }
}
