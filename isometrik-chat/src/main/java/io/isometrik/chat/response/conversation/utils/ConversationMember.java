package io.isometrik.chat.response.conversation.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * The helper class to parse the details of the conversation member.
 */
public class ConversationMember {

    @SerializedName(value = "userProfileImageUrl", alternate = "memberProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName(value = "userName", alternate = "memberName")
    @Expose
    private String userName;
    @SerializedName(value = "userIdentifier", alternate = "memberIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName(value = "userId", alternate = "memberId")
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