package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.json.JSONObject;

/**
 * The helper class to parse the details of the conversation or message update.
 */
public class Details {
  @SerializedName("body")
  @Expose
  private String body;
  @SerializedName("customType")
  @Expose
  private String customType;
  @SerializedName("metaData")
  @Expose
  private Object metadata;
  @SerializedName("searchableTags")
  @Expose
  private List<String> searchableTags;

  /**
   * Gets custom type.
   *
   * @return the custom type
   */
  public String getCustomType() {
    return customType;
  }

  /**
   * Gets metadata.
   *
   * @return the metadata
   */
  public JSONObject getMetadata() {
    try {
      return new JSONObject(new Gson().toJson(metadata));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  /**
   * Gets body.
   *
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets searchable tags.
   *
   * @return the searchable tags
   */
  public List<String> getSearchableTags() {
    return searchableTags;
  }
}
