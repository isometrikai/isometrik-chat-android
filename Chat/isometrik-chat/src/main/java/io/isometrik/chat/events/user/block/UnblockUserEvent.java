package io.isometrik.chat.events.user.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Unblock user event.
 */
public class UnblockUserEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("opponentId")
  @Expose
  private String opponentId;

  @SerializedName("opponentName")
  @Expose
  private String opponentName;

  @SerializedName("opponentIdentifier")
  @Expose
  private String opponentIdentifier;

  @SerializedName("opponentProfileImageUrl")
  @Expose
  private String opponentProfileImageUrl;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  @SerializedName("initiatorIdentifier")
  @Expose
  private String initiatorIdentifier;

  @SerializedName("initiatorProfileImageUrl")
  @Expose
  private String initiatorProfileImageUrl;

  @SerializedName("sentAt")
  @Expose
  private long sentAt;

  /**
   * Gets action.
   *
   * @return the action
   */
  public String getAction() {
    return action;
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
   * Gets opponent name.
   *
   * @return the opponent name
   */
  public String getOpponentName() {
    return opponentName;
  }

  /**
   * Gets opponent identifier.
   *
   * @return the opponent identifier
   */
  public String getOpponentIdentifier() {
    return opponentIdentifier;
  }

  /**
   * Gets opponent profile image url.
   *
   * @return the opponent profile image url
   */
  public String getOpponentProfileImageUrl() {
    return opponentProfileImageUrl;
  }

  /**
   * Gets initiator id.
   *
   * @return the initiator id
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets initiator name.
   *
   * @return the initiator name
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets initiator identifier.
   *
   * @return the initiator identifier
   */
  public String getInitiatorIdentifier() {
    return initiatorIdentifier;
  }

  /**
   * Gets initiator profile image url.
   *
   * @return the initiator profile image url
   */
  public String getInitiatorProfileImageUrl() {
    return initiatorProfileImageUrl;
  }

  /**
   * Gets sent at.
   *
   * @return the sent at
   */
  public long getSentAt() {
    return sentAt;
  }
}
