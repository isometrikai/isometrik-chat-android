package io.isometrik.chat.response.membershipcontrol;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse the response of the fetch members to add to conversation request.
 */
public class FetchMembersToAddToConversationResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("conversationEligibleMembers")
  @Expose
  private ArrayList<MemberToAddToConversation> membersToAddToConversation;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets members to add to conversation.
   *
   * @return the members to add to conversation
   */
  public ArrayList<MemberToAddToConversation> getMembersToAddToConversation() {
    return membersToAddToConversation;
  }

  /**
   * The type Member to add to conversation.
   */
  public static class MemberToAddToConversation {
    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("online")
    @Expose
    private boolean online;
    @SerializedName("lastSeen")
    @Expose
    private long lastSeen;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    /**
     * Gets user profile image url.
     *
     * @return the user profile image url
     */
    public String getUserProfileImageUrl() {
      return userProfileImageUrl;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Gets user identifier.
     *
     * @return the user identifier
     */
    public String getUserIdentifier() {
      return userIdentifier;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
      return userId;
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
     * Gets last seen.
     *
     * @return the last seen
     */
    public long getLastSeen() {
      return lastSeen;
    }

    /**
     * Gets meta data.
     *
     * @return the meta data
     */
    public JSONObject getMetaData() {

      try {
        return new JSONObject(new Gson().toJson(metaData));
      } catch (Exception ignore) {
        return new JSONObject();
      }
    }
  }
}
