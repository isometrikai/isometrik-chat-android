package io.isometrik.chat.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import java.util.ArrayList;

/**
 * The type Fetch messages result.
 */
/**
 * The helper class to parse the response of the fetch messages request.
 */
public class FetchMessagesResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("messages")
  @Expose
  private ArrayList<Message> messages;

  //
  //public static class UserBlockMessage extends Message {
  //
  //}
  //
  //public static class UserUnblockMessage extends Message {
  //
  //}
  //
  //public static class AddAdminMessage extends Message {
  //
  //}
  //
  //public static class RemoveAdminMessage extends Message {
  //
  //}
  //
  //public static class DeleteConversationLocallyMessage extends Message {
  //
  //}
  //
  //public static class ClearConversationMessage extends Message {
  //
  //}
  //
  //public static class MemberJoinMessage extends Message {
  //
  //}
  //
  //public static class MemberLeaveMessage extends Message {
  //
  //}
  //
  //public static class MembersAddMessage extends Message {
  //
  //}
  //
  //public static class MembersRemoveMessage extends Message {
  //
  //}
  //
  //public static class ConversationSettingsUpdatedMessage extends Message {
  //
  //}
  //
  //public static class ConversationImageUpdatedMessage extends Message {
  //
  //}
  //
  //public static class ConversationTitleUpdatedMessage extends Message {
  //
  //}
  //
  //public static class MessagesDeleteLocalMessage extends Message {
  //
  //}
  //
  //public static class MessagesDeleteForAllMessage extends Message {
  //
  //}
  //
  //public static class ReactionAddMessage extends Message {
  //
  //}
  //public static class ReactionRemoveMessage extends Message {
  //
  //}
  //
  //public static class CreateConversationMessage extends Message {
  //
  //}

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets messages.
   *
   * @return the messages
   */
  public ArrayList<Message> getMessages() {
    return messages;
  }
}
