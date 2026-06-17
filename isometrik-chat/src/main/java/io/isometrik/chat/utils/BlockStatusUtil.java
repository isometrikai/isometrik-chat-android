package io.isometrik.chat.utils;

import android.util.Log;
import androidx.annotation.Nullable;
import org.json.JSONObject;

/**
 * Parses block state from conversation API metadata and logs with a single debug tag.
 */
public final class BlockStatusUtil {

  public static final String TAG = "ISOMETRIK_BLOCK";

  private BlockStatusUtil() {
  }

  public static void log(String message) {
    Log.d(TAG, message);
  }

  public static void log(String source, String message) {
    Log.d(TAG, source + " -> " + message);
  }

  /**
   * @return true if opponent blocked current user, false if current user blocked opponent,
   *         null if block state could not be determined from metadata.
   */
  @Nullable
  public static Boolean parseBlockedByOpponent(@Nullable JSONObject metaData, @Nullable String opponentUserId,
      @Nullable String currentUserId, @Nullable String source) {
    log(source, "parseBlockedByOpponent opponentUserId=" + opponentUserId
        + " currentUserId=" + currentUserId
        + " metaData=" + (metaData != null ? metaData.toString() : "null"));

    if (metaData == null) {
      log(source, "metaData is null, cannot parse block state");
      return null;
    }

    try {
      JSONObject blockedMessage = extractBlockedMessage(metaData);
      if (blockedMessage == null) {
        log(source, "blockedMessage not found in metaData");
        return null;
      }

      log(source, "blockedMessage=" + blockedMessage);

      if (blockedMessage.has("sentByMe")) {
        boolean sentByMe = readBoolean(blockedMessage, "sentByMe");
        boolean blockedByOpponent = !sentByMe;
        log(source, "resolved via sentByMe=" + sentByMe + " blockedByOpponent=" + blockedByOpponent);
        return blockedByOpponent;
      }

      String initiatorId = blockedMessage.optString("initiatorId", null);
      if (initiatorId == null || initiatorId.isEmpty()) {
        initiatorId = blockedMessage.optString("userId", null);
      }

      if (initiatorId != null && !initiatorId.isEmpty()) {
        if (currentUserId != null && initiatorId.equals(currentUserId)) {
          log(source, "resolved via initiatorId=currentUser (I blocked) -> false");
          return false;
        }
        if (opponentUserId != null && initiatorId.equals(opponentUserId)) {
          log(source, "resolved via initiatorId=opponentUserId (blocked by opponent) -> true");
          return true;
        }
        log(source, "initiatorId present but does not match current or opponent user");
      }
    } catch (Exception e) {
      log(source, "parse error: " + e.getMessage());
    }

    return null;
  }

  /**
   * Parse block state from conversation lastMessage when metaData is empty on list API.
   */
  @Nullable
  public static Boolean parseBlockedByOpponentFromLastMessage(@Nullable JSONObject lastMessage,
      @Nullable String opponentUserId, @Nullable String currentUserId, @Nullable String source) {
    if (lastMessage == null) {
      log(source, "lastMessage is null");
      return null;
    }

    log(source, "parseFromLastMessage lastMessage=" + lastMessage);

    try {
      String action = lastMessage.optString("action", null);
      String customType = lastMessage.optString("customType", null);

      if (!"userBlockConversation".equals(action) && !"block".equals(customType)) {
        log(source, "lastMessage is not a block action (action=" + action + " customType=" + customType + ")");
        return null;
      }

      if (lastMessage.has("sentByMe")) {
        boolean sentByMe = readBoolean(lastMessage, "sentByMe");
        boolean blockedByOpponent = !sentByMe;
        log(source, "lastMessage resolved via sentByMe=" + sentByMe + " blockedByOpponent=" + blockedByOpponent);
        return blockedByOpponent;
      }

      String initiatorId = lastMessage.optString("initiatorId", null);
      if (initiatorId == null || initiatorId.isEmpty()) {
        initiatorId = lastMessage.optString("userId", null);
      }
      if (initiatorId != null && !initiatorId.isEmpty()) {
        if (currentUserId != null && initiatorId.equals(currentUserId)) {
          log(source, "lastMessage resolved via initiatorId=currentUser -> false");
          return false;
        }
        if (opponentUserId != null && initiatorId.equals(opponentUserId)) {
          log(source, "lastMessage resolved via initiatorId=opponentUserId -> true");
          return true;
        }
      }

      String initiatorName = lastMessage.optString("initiatorName", null);
      String opponentName = lastMessage.optString("opponentName", null);
      String currentUserName = io.isometrik.ui.IsometrikChatSdk.getInstance().getUserSession().getUserName();
      if (opponentName != null && opponentName.equals(currentUserName)) {
        log(source, "lastMessage resolved via opponentName=currentUser -> true");
        return true;
      }
      if (initiatorName != null && initiatorName.equals(currentUserName)) {
        log(source, "lastMessage resolved via initiatorName=currentUser -> false");
        return false;
      }
    } catch (Exception e) {
      log(source, "lastMessage parse error: " + e.getMessage());
    }

    return null;
  }

  /**
   * Resolve block state using metadata, lastMessage, then session cache (MQTT).
   */
  @Nullable
  public static Boolean resolveBlockedByOpponent(@Nullable JSONObject metaData,
      @Nullable JSONObject lastMessage, @Nullable String opponentUserId,
      @Nullable String currentUserId, boolean messagingDisabled, @Nullable String source) {
    return resolveBlockedByOpponent(metaData, lastMessage, null, opponentUserId, currentUserId,
        messagingDisabled, source);
  }

  @Nullable
  public static Boolean resolveBlockedByOpponent(@Nullable JSONObject metaData,
      @Nullable JSONObject lastMessage, @Nullable String conversationId,
      @Nullable String opponentUserId, @Nullable String currentUserId, boolean messagingDisabled,
      @Nullable String source) {
    Boolean fromMeta = parseBlockedByOpponent(metaData, opponentUserId, currentUserId, source + "#meta");
    if (fromMeta != null) {
      BlockStateCache.setBlockedByOpponentForConversation(conversationId, opponentUserId, fromMeta);
      return fromMeta;
    }

    Boolean fromLastMessage = parseBlockedByOpponentFromLastMessage(lastMessage, opponentUserId,
        currentUserId, source + "#lastMessage");
    if (fromLastMessage != null) {
      BlockStateCache.setBlockedByOpponentForConversation(conversationId, opponentUserId, fromLastMessage);
      return fromLastMessage;
    }

    if (messagingDisabled && opponentUserId != null) {
      Boolean cached = BlockStateCache.getBlockedByOpponent(opponentUserId);
      if (cached == null && conversationId != null) {
        cached = BlockStateCache.getBlockedByOpponentForConversation(conversationId);
      }
      if (cached != null) {
        log(source + "#cache", "resolved blockedByOpponent=" + cached + " opponentUserId=" + opponentUserId
            + " conversationId=" + conversationId);
        return cached;
      }
      log(source + "#cache",
          "messagingDisabled=true but API has no blockedMessage (metaData empty or lastMessage is chat). "
              + "opponentUserId=" + opponentUserId + " conversationId=" + conversationId
              + " — need MQTT or persisted cache; backend should include metaData.customMetaData.blockedMessage");
    }

    return null;
  }

  @Nullable
  private static JSONObject extractBlockedMessage(JSONObject metaData) {
    JSONObject customMetaData = metaData.optJSONObject("customMetaData");
    if (customMetaData != null && customMetaData.has("blockedMessage")) {
      return customMetaData.optJSONObject("blockedMessage");
    }
    if (metaData.has("blockedMessage")) {
      return metaData.optJSONObject("blockedMessage");
    }
    return null;
  }

  private static boolean readBoolean(JSONObject jsonObject, String key) {
    Object value = jsonObject.opt(key);
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    if (value instanceof Number) {
      return ((Number) value).intValue() != 0;
    }
    return Boolean.parseBoolean(jsonObject.optString(key, "false"));
  }
}
