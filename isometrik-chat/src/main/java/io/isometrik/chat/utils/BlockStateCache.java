package io.isometrik.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import io.isometrik.ui.IsometrikChatSdk;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Persists block direction per opponent when list API returns messagingDisabled=true
 * without metaData.blockedMessage (backend inconsistency).
 */
public final class BlockStateCache {

  private static final String PREFS_NAME = "IsometrikBlockState";
  private static final String KEY_PREFIX_OPPONENT = "opponent:";
  private static final String KEY_PREFIX_CONVERSATION = "conversation:";

  private static final ConcurrentHashMap<String, Boolean> blockedByOpponentByOpponentId =
      new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, Boolean> blockedByOpponentByConversationId =
      new ConcurrentHashMap<>();

  private BlockStateCache() {
  }

  public static void setBlockedByOpponent(String opponentUserId, boolean blockedByOpponent) {
    if (opponentUserId == null || opponentUserId.isEmpty()) {
      return;
    }
    blockedByOpponentByOpponentId.put(opponentUserId, blockedByOpponent);
    persistOpponent(opponentUserId, blockedByOpponent);
    BlockStatusUtil.log("BlockStateCache",
        "set opponentUserId=" + opponentUserId + " blockedByOpponent=" + blockedByOpponent);
  }

  public static void setBlockedByOpponentForConversation(String conversationId,
      String opponentUserId, boolean blockedByOpponent) {
    setBlockedByOpponent(opponentUserId, blockedByOpponent);
    if (conversationId != null && !conversationId.isEmpty()) {
      blockedByOpponentByConversationId.put(conversationId, blockedByOpponent);
      persistConversation(conversationId, blockedByOpponent);
      BlockStatusUtil.log("BlockStateCache",
          "set conversationId=" + conversationId + " blockedByOpponent=" + blockedByOpponent);
    }
  }

  @Nullable
  public static Boolean getBlockedByOpponent(String opponentUserId) {
    if (opponentUserId == null || opponentUserId.isEmpty()) {
      return null;
    }
    Boolean inMemory = blockedByOpponentByOpponentId.get(opponentUserId);
    if (inMemory != null) {
      return inMemory;
    }
    return readPersistedOpponent(opponentUserId);
  }

  @Nullable
  public static Boolean getBlockedByOpponentForConversation(String conversationId) {
    if (conversationId == null || conversationId.isEmpty()) {
      return null;
    }
    Boolean inMemory = blockedByOpponentByConversationId.get(conversationId);
    if (inMemory != null) {
      return inMemory;
    }
    return readPersistedConversation(conversationId);
  }

  public static void clear(String opponentUserId) {
    if (opponentUserId != null && !opponentUserId.isEmpty()) {
      blockedByOpponentByOpponentId.remove(opponentUserId);
      removePersisted(KEY_PREFIX_OPPONENT + opponentUserId);
      BlockStatusUtil.log("BlockStateCache", "cleared opponentUserId=" + opponentUserId);
    }
  }

  public static void clearConversation(String conversationId, String opponentUserId) {
    clear(opponentUserId);
    if (conversationId != null && !conversationId.isEmpty()) {
      blockedByOpponentByConversationId.remove(conversationId);
      removePersisted(KEY_PREFIX_CONVERSATION + conversationId);
      BlockStatusUtil.log("BlockStateCache", "cleared conversationId=" + conversationId);
    }
  }

  private static SharedPreferences prefs() {
    Context context = IsometrikChatSdk.getInstance().getContext();
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
  }

  private static String scopedUserKey(String suffix) {
    String userId = IsometrikChatSdk.getInstance().getUserSession().getUserId();
    return userId + ":" + suffix;
  }

  private static void persistOpponent(String opponentUserId, boolean blockedByOpponent) {
    prefs().edit()
        .putBoolean(scopedUserKey(KEY_PREFIX_OPPONENT + opponentUserId), blockedByOpponent)
        .apply();
  }

  private static void persistConversation(String conversationId, boolean blockedByOpponent) {
    prefs().edit()
        .putBoolean(scopedUserKey(KEY_PREFIX_CONVERSATION + conversationId), blockedByOpponent)
        .apply();
  }

  @Nullable
  private static Boolean readPersistedOpponent(String opponentUserId) {
    String key = scopedUserKey(KEY_PREFIX_OPPONENT + opponentUserId);
    if (!prefs().contains(key)) {
      return null;
    }
    boolean value = prefs().getBoolean(key, false);
    blockedByOpponentByOpponentId.put(opponentUserId, value);
    BlockStatusUtil.log("BlockStateCache",
        "restored from disk opponentUserId=" + opponentUserId + " blockedByOpponent=" + value);
    return value;
  }

  @Nullable
  private static Boolean readPersistedConversation(String conversationId) {
    String key = scopedUserKey(KEY_PREFIX_CONVERSATION + conversationId);
    if (!prefs().contains(key)) {
      return null;
    }
    boolean value = prefs().getBoolean(key, false);
    blockedByOpponentByConversationId.put(conversationId, value);
    BlockStatusUtil.log("BlockStateCache",
        "restored from disk conversationId=" + conversationId + " blockedByOpponent=" + value);
    return value;
  }

  private static void removePersisted(String suffix) {
    prefs().edit().remove(scopedUserKey(suffix)).apply();
  }
}
