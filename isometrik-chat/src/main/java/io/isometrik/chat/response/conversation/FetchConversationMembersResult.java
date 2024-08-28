package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch conversation members request.
 */
public class FetchConversationMembersResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("conversationMembers")
  @Expose
  private ArrayList<ConversationMember> conversationMembers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversation members.
   *
   * @return the conversation members
   */
  public ArrayList<ConversationMember> getConversationMembers() {
    return conversationMembers;
  }

  /**
   * Gets members count.
   *
   * @return the members count
   */
  public int getMembersCount() {
    return membersCount;
  }
}
