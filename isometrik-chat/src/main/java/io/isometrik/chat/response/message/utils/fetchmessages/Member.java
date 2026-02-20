package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the details of the add/remove member.
 */
public class Member {
  @SerializedName("memberId")
  @Expose
  private String memberId;
  @SerializedName("memberName")
  @Expose
  private String memberName;
  @SerializedName("memberIdentifier")
  @Expose
  private String memberIdentifier;
  @SerializedName("memberProfileImageUrl")
  @Expose
  private String memberProfileImageUrl;
  @SerializedName("isAdmin")
  @Expose
  private Boolean isAdmin;
  @SerializedName("isPublishing")
  @Expose
  private Boolean isPublishing;

  /**
   * Gets member id.
   *
   * @return the member id
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the member name
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * Gets member identifier.
   *
   * @return the member identifier
   */
  public String getMemberIdentifier() {
    return memberIdentifier;
  }

  /**
   * Gets member profile image url.
   *
   * @return the member profile image url
   */
  public String getMemberProfileImageUrl() {
    return memberProfileImageUrl;
  }

  /**
   * Gets is admin.
   *
   * @return the is admin
   */
  public Boolean getIsAdmin() {
    return isAdmin;
  }

  /**
   * Gets is publishing.
   *
   * @return the is publishing
   */
  public Boolean getIsPublishing() {
    return isPublishing;
  }
}
