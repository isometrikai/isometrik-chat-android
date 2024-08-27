package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the details of the add/remove member.
 */
public class AddRemoveMember {
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
}
