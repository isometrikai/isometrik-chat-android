package io.isometrik.chat.response.error;

/**
 * The helper class for building Isometrik errors for local validations.
 */
public final class IsometrikErrorBuilder {

  // Error Codes

  private static final int IMERR_LICENSE_KEY_MISSING = 101;

  /**
   * The constant IMERROBJ_LICENSE_KEY_MISSING.
   */
  public static final IsometrikError IMERROBJ_LICENSE_KEY_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_LICENSE_KEY_MISSING)
          .setErrorMessage("License key not configured.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_APP_SECRET_MISSING = 102;

  /**
   * The constant IMERROBJ_APP_SECRET_MISSING.
   */
  public static final IsometrikError IMERROBJ_APP_SECRET_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_APP_SECRET_MISSING)
          .setErrorMessage("App secret not configured.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_SECRET_MISSING = 103;

  /**
   * The constant IMERROBJ_USER_SECRET_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_SECRET_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_SECRET_MISSING)
          .setErrorMessage("ReadByUser secret not configured.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CLIENT_ID_MISSING = 104;

  /**
   * The constant IMERROBJ_CLIENT_ID_MISSING.
   */
  public static final IsometrikError IMERROBJ_CLIENT_ID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CLIENT_ID_MISSING)
          .setErrorMessage("Client id not configured.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATIONID_MISSING = 105;

  /**
   * The constant IMERROBJ_CONVERSATIONID_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONVERSATIONID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATIONID_MISSING)
          .setErrorMessage("conversationId is missing.")
          .setRemoteError(false)
          .build();
  private static final int IMERR_MESSAGEID_MISSING = 106;

  /**
   * The constant IMERROBJ_MESSAGEID_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGEID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGEID_MISSING)
          .setErrorMessage("messageId is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_REACTIONTYPE_MISSING = 107;

  /**
   * The constant IMERROBJ_REACTIONTYPE_MISSING.
   */
  public static final IsometrikError IMERROBJ_REACTIONTYPE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_REACTIONTYPE_MISSING)
          .setErrorMessage("reactionType is missing.")
          .setRemoteError(false)
          .build();
  private static final int IMERR_MESSAGEIDS_MISSING = 108;

  /**
   * The constant IMERROBJ_MESSAGEIDS_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGEIDS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGEIDS_MISSING)
          .setErrorMessage("messageIds are missing.")
          .setRemoteError(false)
          .build();
  private static final int IMERR_MEMBERID_MISSING = 109;

  /**
   * The constant IMERROBJ_MEMBERID_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEMBERID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBERID_MISSING)
          .setErrorMessage("memberId is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEMBERIDS_MISSING = 110;

  /**
   * The constant IMERROBJ_MEMBERIDS_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEMBERIDS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBERIDS_MISSING)
          .setErrorMessage("memberIds are missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_IMAGE_URL_MISSING = 111;

  /**
   * The constant IMERROBJ_CONVERSATION_IMAGE_URL_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_IMAGE_URL_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_IMAGE_URL_MISSING)
          .setErrorMessage("conversationImageUrl is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_TITLE_MISSING = 112;

  /**
   * The constant IMERROBJ_CONVERSATION_TITLE_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_TITLE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_TITLE_MISSING)
          .setErrorMessage("conversationTitle is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_SETTINGS_MISSING = 113;

  /**
   * The constant IMERROBJ_CONVERSATION_SETTINGS_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_SETTINGS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_SETTINGS_MISSING)
          .setErrorMessage(
              "Atleast one of pushNotifications, readEvents and typingEvents should be specified.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_TYPE_MISSING = 114;

  /**
   * The constant IMERROBJ_CONVERSATION_TYPE_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_TYPE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_TYPE_MISSING)
          .setErrorMessage("conversationType is missing.")
          .setRemoteError(false)
          .build();
  private static final int IMERR_IS_GROUP_MISSING = 115;

  /**
   * The constant IMERROBJ_IS_GROUP_MISSING.
   */
  public static final IsometrikError IMERROBJ_IS_GROUP_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_IS_GROUP_MISSING)
          .setErrorMessage("isGroup is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEMBERS_MISSING = 116;

  /**
   * The constant IMERROBJ_MEMBERS_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEMBERS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBERS_MISSING)
          .setErrorMessage("Members list is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_OPPONENTID_MISSING = 117;

  /**
   * The constant IMERROBJ_OPPONENTID_MISSING.
   */
  public static final IsometrikError IMERROBJ_OPPONENTID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_OPPONENTID_MISSING)
          .setErrorMessage("opponentId is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_TIMESTAMP_MISSING = 118;

  /**
   * The constant IMERROBJ_TIMESTAMP_MISSING.
   */
  public static final IsometrikError IMERROBJ_TIMESTAMP_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_TIMESTAMP_MISSING)
          .setErrorMessage("timestamp is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_IDENTIFIER_MISSING = 119;

  /**
   * The constant IMERROBJ_USER_IDENTIFIER_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_IDENTIFIER_MISSING = new IsometrikError.Builder()
      .setIsometrikErrorCode(IMERR_USER_IDENTIFIER_MISSING)
      .setErrorMessage("userIdentifier is missing.")
      .setRemoteError(false)
      .build();

  private static final int IMERR_USER_PASSWORD_MISSING = 120;

  /**
   * The constant IMERROBJ_USER_PASSWORD_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_PASSWORD_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PASSWORD_MISSING)
          .setErrorMessage("userPassword is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_UPDATE_USER_DETAILS_MISSING = 121;

  /**
   * The constant IMERROBJ_UPDATE_USER_DETAILS_MISSING.
   */
  public static final IsometrikError IMERROBJ_UPDATE_USER_DETAILS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UPDATE_USER_DETAILS_MISSING)
          .setErrorMessage(
              "Atleast one of userIdentifier, userProfileImageUrl, userName, notification and metadata should be specified.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_PROFILE_IMAGEURL_MISSING = 122;

  /**
   * The constant IMERROBJ_USER_PROFILE_IMAGEURL_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_PROFILE_IMAGEURL_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PROFILE_IMAGEURL_MISSING)
          .setErrorMessage("userProfileImageUrl is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_NAME_MISSING = 123;

  /**
   * The constant IMERROBJ_USER_NAME_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_NAME_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_NAME_MISSING)
          .setErrorMessage("userName is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MESSAGE_TYPE_MISSING = 124;

  /**
   * The constant IMERROBJ_MESSAGE_TYPE_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGE_TYPE_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_TYPE_MISSING)
          .setErrorMessage("messageType is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_SHOW_IN_CONVERSATION_MISSING = 125;

  /**
   * The constant IMERROBJ_SHOW_IN_CONVERSATION_MISSING.
   */
  public static final IsometrikError IMERROBJ_SHOW_IN_CONVERSATION_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SHOW_IN_CONVERSATION_MISSING)
          .setErrorMessage("showInConversation is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MESSAGE_BODY_MISSING = 126;

  /**
   * The constant IMERROBJ_MESSAGE_BODY_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGE_BODY_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_BODY_MISSING)
          .setErrorMessage("Message body is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MESSAGE_ENCRYPTED_MISSING = 127;

  /**
   * The constant IMERROBJ_MESSAGE_ENCRYPTED_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGE_ENCRYPTED_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_ENCRYPTED_MISSING)
          .setErrorMessage("Message encrypted is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_IDS_MISSING = 128;

  /**
   * The constant IMERROBJ_USER_IDS_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_IDS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IDS_MISSING)
          .setErrorMessage("userIds are missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_EVENT_FOR_MESSAGE_INVALID_VALUE = 129;

  /**
   * The constant IMERROBJ_EVENT_FOR_MESSAGE_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_EVENT_FOR_MESSAGE_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_EVENT_FOR_MESSAGE_INVALID_VALUE)
          .setErrorMessage("Message events has invalid value.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_IDS_CONVERSATION_IDS_MISSING = 130;

  /**
   * The constant IMERROBJ_USER_IDS_CONVERSATION_IDS_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_IDS_CONVERSATION_IDS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IDS_CONVERSATION_IDS_MISSING)
          .setErrorMessage("Atleast one of userId or conversationIds should be specified.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_ATTACHMENTS_INVALID_VALUE = 131;

  /**
   * The constant IMERROBJ_ATTACHMENTS_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_ATTACHMENTS_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_ATTACHMENTS_INVALID_VALUE)
          .setErrorMessage("Attachments has invalid value.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MENTIONED_USERS_INVALID_VALUE = 132;

  /**
   * The constant IMERROBJ_MENTIONED_USERS_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_MENTIONED_USERS_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MENTIONED_USERS_INVALID_VALUE)
          .setErrorMessage("Mentioned users has invalid value.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONNECTION_STRING_MISSING = 144;

  /**
   * The constant IMERROBJ_CONNECTION_STRING_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONNECTION_STRING_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONNECTION_STRING_MISSING)
          .setErrorMessage("accountId or keysetId or projectId  not configured.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONNECTION_STRING_INVALID_VALUE = 145;

  /**
   * The constant IMERROBJ_CONNECTION_STRING_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_CONNECTION_STRING_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONNECTION_STRING_INVALID_VALUE)
          .setErrorMessage("Connection string has invalid value.").setRemoteError(false).build();

  private static final int IMERR_USER_TOKEN_MISSING = 146;

  /**
   * The constant IMERROBJ_USER_TOKEN_MISSING.
   */
  public static final IsometrikError IMERROBJ_USER_TOKEN_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_TOKEN_MISSING)
          .setErrorMessage("userToken is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_READ_EVENTS_MISSING = 147;

  /**
   * The constant IMERROBJ_READ_EVENTS_MISSING.
   */
  public static final IsometrikError IMERROBJ_READ_EVENTS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_READ_EVENTS_MISSING)
          .setErrorMessage("readEvents is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_TYPING_EVENTS_MISSING = 148;

  /**
   * The constant IMERROBJ_TYPING_EVENTS_MISSING.
   */
  public static final IsometrikError IMERROBJ_TYPING_EVENTS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_TYPING_EVENTS_MISSING)
          .setErrorMessage("typingEvents is missing.")
          .setRemoteError(false).build();

  private static final int IMERR_PUSH_NOTIFICATIONS_MISSING = 149;

  /**
   * The constant IMERROBJ_PUSH_NOTIFICATIONS_MISSING.
   */
  public static final IsometrikError IMERROBJ_PUSH_NOTIFICATIONS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PUSH_NOTIFICATIONS_MISSING)
          .setErrorMessage("pushNotifications is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIA_NOT_FOUND = 150;

  /**
   * The constant IMERROBJ_MEDIA_NOT_FOUND.
   */
  public static final IsometrikError IMERROBJ_MEDIA_NOT_FOUND =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_NOT_FOUND)
          .setErrorMessage("Media not found at given url or invalid url.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIA_DOWNLOAD_CANCELED = 151;

  /**
   * The constant IMERROBJ_MEDIA_DOWNLOAD_CANCELED.
   */
  public static final IsometrikError IMERROBJ_MEDIA_DOWNLOAD_CANCELED = new IsometrikError.Builder()
      .setIsometrikErrorCode(IMERR_MEDIA_DOWNLOAD_CANCELED)
      .setErrorMessage("Media download has been canceled.")
      .setRemoteError(false)
      .build();

  private static final int IMERR_MEDIA_URL_MISSING = 152;

  /**
   * The constant IMERROBJ_MEDIA_URL_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEDIA_URL_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_URL_MISSING)
          .setErrorMessage("mediaUrl is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CANCEL_DOWNLOAD_REQUEST_NOT_FOUND = 153;

  /**
   * The constant IMERROBJ_CANCEL_DOWNLOAD_REQUEST_NOT_FOUND.
   */
  public static final IsometrikError IMERROBJ_CANCEL_DOWNLOAD_REQUEST_NOT_FOUND =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CANCEL_DOWNLOAD_REQUEST_NOT_FOUND)
          .setErrorMessage("mediaDownload request to be canceled not found.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_DOWNLOAD_MEDIA_FAILED= 154;

  /**
   * The constant IMERROBJ_DOWNLOAD_MEDIA_FAILED.
   */
  public static final IsometrikError IMERROBJ_DOWNLOAD_MEDIA_FAILED =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_DOWNLOAD_MEDIA_FAILED)
          .setErrorMessage("media download failed.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_PRESIGNED_URL_REQUESTS_INVALID_VALUE = 155;

  /**
   * The constant IMERROBJ_PRESIGNED_URL_REQUESTS_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_PRESIGNED_URL_REQUESTS_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRESIGNED_URL_REQUESTS_INVALID_VALUE)
          .setErrorMessage("Presigned urls has invalid value.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIA_PATH_MISSING = 156;

  /**
   * The constant IMERROBJ_MEDIA_PATH_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEDIA_PATH_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_PATH_MISSING)
          .setErrorMessage("media path is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_UPLOAD_MEDIA_FAILED = 157;

  /**
   * The constant IMERROBJ_UPLOAD_MEDIA_FAILED.
   */
  public static final IsometrikError IMERROBJ_UPLOAD_MEDIA_FAILED =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UPLOAD_MEDIA_FAILED)
          .setErrorMessage("media upload failed.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CANCEL_UPLOAD_REQUEST_NOT_FOUND = 158;

  /**
   * The constant IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND.
   */
  public static final IsometrikError IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CANCEL_UPLOAD_REQUEST_NOT_FOUND)
          .setErrorMessage("mediaUpload request to be canceled not found.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIAID_MISSING = 159;

  /**
   * The constant IMERROBJ_MEDIAID_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEDIAID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIAID_MISSING)
          .setErrorMessage("mediaId is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIA_UPLOAD_CANCELED = 160;

  /**
   * The constant IMERROBJ_MEDIA_UPLOAD_CANCELED.
   */
  public static final IsometrikError IMERROBJ_MEDIA_UPLOAD_CANCELED =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_UPLOAD_CANCELED)
          .setErrorMessage("Media upload has been canceled.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_PRESIGNED_URL_MISSING = 161;
  /**
   * The constant IMERROBJ_PRESIGNED_URL_MISSING.
   */
  public static final IsometrikError IMERROBJ_PRESIGNED_URL_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRESIGNED_URL_MISSING)
          .setErrorMessage("presignedUrl is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIA_NOT_SUPPORTED = 162;

  /**
   * The constant IMERROBJ_MEDIA_NOT_SUPPORTED.
   */
  public static final IsometrikError IMERROBJ_MEDIA_NOT_SUPPORTED =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_NOT_SUPPORTED)
          .setErrorMessage("Media type not supported.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_DEVICEID_MISSING = 163;

  /**
   * The constant IMERROBJ_DEVICEID_MISSING.
   */
  public static final IsometrikError IMERROBJ_DEVICEID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_DEVICEID_MISSING)
          .setErrorMessage("deviceId is missing.")
          .setRemoteError(false)
          .build();
  private static final int IMERR_REQUESTID_MISSING = 164;

  /**
   * The constant IMERROBJ_REQUESTID_MISSING.
   */
  public static final IsometrikError IMERROBJ_REQUESTID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_REQUESTID_MISSING)
          .setErrorMessage("requestId is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MEDIA_EXTENSION_MISSING = 165;

  /**
   * The constant IMERROBJ_MEDIA_EXTENSION_MISSING.
   */
  public static final IsometrikError IMERROBJ_MEDIA_EXTENSION_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_EXTENSION_MISSING)
          .setErrorMessage("mediaExtension is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_NEW_CONVERSATION_MISSING = 166;

  /**
   * The constant IMERROBJ_NEW_CONVERSATION_MISSING.
   */
  public static final IsometrikError IMERROBJ_NEW_CONVERSATION_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_NEW_CONVERSATION_MISSING)
          .setErrorMessage("newConversation is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_APPLICATION_NAME_MISSING = 167;

  /**
   * The constant IMERROBJ_APPLICATION_NAME_MISSING.
   */
  public static final IsometrikError IMERROBJ_APPLICATION_NAME_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_APPLICATION_NAME_MISSING)
          .setErrorMessage("applicationName is missing.")
          .setRemoteError(false)
          .build();


  private static final int IMERR_DOWNLOAD_MEDIA_TYPE_MISSING = 168;

  /**
   * The constant IMERROBJ_DOWNLOAD_MEDIA_TYPE_MISSING.
   */
  public static final IsometrikError IMERROBJ_DOWNLOAD_MEDIA_TYPE_MISSING=
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_DOWNLOAD_MEDIA_TYPE_MISSING)
          .setErrorMessage("downloadMediaType is missing.")
          .setRemoteError(false)
          .build();
  private static final int IMERR_MEDIA_DOWNLOAD_CANCELED_OR_NETWORK_ERROR = 169;

  /**
   * The constant IMERROBJ_MEDIA_DOWNLOAD_CANCELED_OR_NETWORK_ERROR.
   */
  public static final IsometrikError IMERROBJ_MEDIA_DOWNLOAD_CANCELED_OR_NETWORK_ERROR = new IsometrikError.Builder()
      .setIsometrikErrorCode(IMERR_MEDIA_DOWNLOAD_CANCELED_OR_NETWORK_ERROR)
      .setErrorMessage("Network error or media download has been canceled.")
      .setRemoteError(false).build();

  private static final int IMERR_LOCAL_MESSAGE_ID_MISSING = 170;

  /**
   * The constant IMERROBJ_LOCAL_MESSAGE_ID_MISSING.
   */
  public static final IsometrikError IMERROBJ_LOCAL_MESSAGE_ID_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_LOCAL_MESSAGE_ID_MISSING)
          .setErrorMessage("local messageId is missing.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_DETAILS_MISSING = 171;

  /**
   * The constant IMERROBJ_CONVERSATION_DETAILS_MISSING.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_DETAILS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_DETAILS_MISSING)
          .setErrorMessage(
              "Atleast one of metadata, customType and searchableTags should be specified.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_IMAGE_UPLOAD_CANCELED = 172;

  /**
   * The constant IMERROBJ_CONVERSATION_IMAGE_UPLOAD_CANCELED.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_IMAGE_UPLOAD_CANCELED =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_IMAGE_UPLOAD_CANCELED)
          .setErrorMessage("Conversation image upload has been canceled.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_USER_IMAGE_UPLOAD_CANCELED = 173;

  /**
   * The constant IMERROBJ_USER_IMAGE_UPLOAD_CANCELED.
   */
  public static final IsometrikError IMERROBJ_USER_IMAGE_UPLOAD_CANCELED =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IMAGE_UPLOAD_CANCELED)
          .setErrorMessage("User image upload has been canceled.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_MESSAGE_DETAILS_MISSING = 174;

  /**
   * The constant IMERROBJ_MESSAGE_DETAILS_MISSING.
   */
  public static final IsometrikError IMERROBJ_MESSAGE_DETAILS_MISSING =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_DETAILS_MISSING)
          .setErrorMessage(
              "Atleast one of body, metadata, customType and searchableTags should be specified.")
          .setRemoteError(false)
          .build();

  private static final int IMERR_CONVERSATION_TYPE_INVALID_VALUE = 175;

  /**
   * The constant IMERROBJ_CONVERSATION_TYPE_INVALID_VALUE.
   */
  public static final IsometrikError IMERROBJ_CONVERSATION_TYPE_INVALID_VALUE =
      new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONVERSATION_TYPE_INVALID_VALUE)
          .setErrorMessage("conversationType has invalid value.")
          .setRemoteError(false)
          .build();
  /**
   * Not Found
   */
  private static final int IMERR_NOT_FOUND = 133;

  /**
   * Forbidden
   */
  private static final int IMERR_FORBIDDEN = 134;

  /**
   * Service unavailable
   */
  private static final int IMERR_SERVICE_UNAVAILABLE = 135;

  /**
   * Conflict
   */
  private static final int IMERR_CONFLICT = 136;

  /**
   * Unprocessable entity
   */
  private static final int IMERR_UNPROCESSABLE_ENTITY = 137;

  /**
   * Bad gateway
   */
  private static final int IMERR_BAD_GATEWAY = 138;

  /**
   * Internal server error
   */

  private static final int IMERR_INTERNAL_SERVER_ERROR = 139;

  /**
   * Parsing error
   */

  private static final int IMERR_PARSING_ERROR = 140;

  /**
   * Network error
   */

  private static final int IMERR_NETWORK_ERROR = 141;

  /**
   * Bad Request error
   */

  private static final int IMERR_BAD_REQUEST_ERROR = 142;

  /**
   * Unauthorized error
   */

  private static final int IMERR_UNAUTHORIZED_ERROR = 143;

  /**
   * Gets imerr not found.
   *
   * @return the imerr not found
   */
  static int getImerrNotFound() {
    return IMERR_NOT_FOUND;
  }

  /**
   * Gets imerr forbidden.
   *
   * @return the imerr forbidden
   */
  static int getImerrForbidden() {
    return IMERR_FORBIDDEN;
  }

  /**
   * Gets imerr service unavailable.
   *
   * @return the imerr service unavailable
   */
  static int getImerrServiceUnavailable() {
    return IMERR_SERVICE_UNAVAILABLE;
  }

  /**
   * Gets imerr conflict.
   *
   * @return the imerr conflict
   */
  static int getImerrConflict() {
    return IMERR_CONFLICT;
  }

  /**
   * Gets imerr unprocessable entity.
   *
   * @return the imerr unprocessable entity
   */
  static int getImerrUnprocessableEntity() {
    return IMERR_UNPROCESSABLE_ENTITY;
  }

  /**
   * Gets imerr bad gateway.
   *
   * @return the imerr bad gateway
   */
  static int getImerrBadGateway() {
    return IMERR_BAD_GATEWAY;
  }

  /**
   * Gets imerr internal server error.
   *
   * @return the imerr internal server error
   */
  static int getImerrInternalServerError() {
    return IMERR_INTERNAL_SERVER_ERROR;
  }

  /**
   * Gets imerr parsing error.
   *
   * @return the imerr parsing error
   */
  static int getImerrParsingError() {
    return IMERR_PARSING_ERROR;
  }

  /**
   * Gets imerr network error.
   *
   * @return the imerr network error
   */
  static int getImerrNetworkError() {
    return IMERR_NETWORK_ERROR;
  }

  /**
   * Gets imerr bad request error.
   *
   * @return the imerr bad request error
   */
  static int getImerrBadRequestError() {
    return IMERR_BAD_REQUEST_ERROR;
  }

  /**
   * Gets imerr unauthorized error.
   *
   * @return the imerr unauthorized error
   */
  public static int getImerrUnauthorizedError() {
    return IMERR_UNAUTHORIZED_ERROR;
  }
}
