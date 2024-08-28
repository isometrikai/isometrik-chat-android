package io.isometrik.chat.response.membershipcontrol;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse the response of the fetch observers request.
 */
public class FetchObserversResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("observersCount")
  @Expose
  private int observersCount;

  @SerializedName("conversationObservers")
  @Expose
  private ArrayList<ConversationObserver> conversationObservers;

  /**
   * The type Conversation observer.
   */
  public static class ConversationObserver {

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
    @SerializedName("isAdmin")
    @Expose
    private boolean isAdmin;
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
     * Is admin boolean.
     *
     * @return the boolean
     */
    public boolean isAdmin() {
      return isAdmin;
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

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversation observers.
   *
   * @return the conversation observers
   */
  public ArrayList<ConversationObserver> getConversationObservers() {
    return conversationObservers;
  }

  /**
   * Gets observers count.
   *
   * @return the observers count
   */
  public int getObserversCount() {
    return observersCount;
  }
}
