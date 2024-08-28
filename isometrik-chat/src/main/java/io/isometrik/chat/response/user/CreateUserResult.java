package io.isometrik.chat.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the create user request.
 */
public class CreateUserResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("userToken")
  @Expose
  private String userToken;

  @SerializedName("userId")
  @Expose
  private String userId;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }
}
