package io.isometrik.chat.remote;

import com.google.gson.Gson;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.Isometrik;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.error.BaseResponse;

/**
 * The remote use case class containing methods for accessing use cases containing api calls for
 * operations-
 * ConversationUseCases, DeliveryStatusUseCases, DownloadUseCases, MembershipControlUseCases,
 * MessageUseCases, ReactionUseCases, UploadUseCases and UserUseCases.
 */
public class RemoteUseCases {

  private final ConversationUseCases conversationUseCases;
  private final DeliveryStatusUseCases deliveryStatusUseCases;
  private final DownloadUseCases downloadUseCases;
  private final MembershipControlUseCases membershipControlUseCases;
  private final MessageUseCases messageUseCases;
  private final ReactionUseCases reactionUseCases;
  private final UploadUseCases uploadUseCases;
  private final UserUseCases userUseCases;

  /**
   * Instantiates a new Remote use cases.
   *
   * @param imConfiguration the im configuration
   * @param retrofitManager the retrofit manager
   * @param baseResponse the base response
   * @param gson the gson
   * @param isometrik the isometrik
   * @param mediaTransferManager the media transfer manager
   */
  public RemoteUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson, Isometrik isometrik,
      MediaTransferManager mediaTransferManager) {
    conversationUseCases =
        new ConversationUseCases(imConfiguration, retrofitManager, baseResponse, gson);

    deliveryStatusUseCases =
        new DeliveryStatusUseCases(imConfiguration, retrofitManager, baseResponse, gson);

    downloadUseCases = new DownloadUseCases(imConfiguration, mediaTransferManager, baseResponse);

    membershipControlUseCases =
        new MembershipControlUseCases(imConfiguration, retrofitManager, baseResponse, gson);

    messageUseCases =
        new MessageUseCases(imConfiguration, retrofitManager, baseResponse, gson, isometrik);

    reactionUseCases = new ReactionUseCases(imConfiguration, retrofitManager, baseResponse, gson);

    uploadUseCases = new UploadUseCases(mediaTransferManager, baseResponse);

    userUseCases = new UserUseCases(imConfiguration, retrofitManager, baseResponse, gson);
  }

  /**
   * Gets conversation use cases.
   *
   * @return the conversation use cases
   */
  public ConversationUseCases getConversationUseCases() {
    return conversationUseCases;
  }

  /**
   * Gets delivery status use cases.
   *
   * @return the delivery status use cases
   */
  public DeliveryStatusUseCases getDeliveryStatusUseCases() {
    return deliveryStatusUseCases;
  }

  /**
   * Gets download use cases.
   *
   * @return the download use cases
   */
  public DownloadUseCases getDownloadUseCases() {
    return downloadUseCases;
  }

  /**
   * Gets membership control use cases.
   *
   * @return the membership control use cases
   */
  public MembershipControlUseCases getMembershipControlUseCases() {
    return membershipControlUseCases;
  }

  /**
   * Gets message use cases.
   *
   * @return the message use cases
   */
  public MessageUseCases getMessageUseCases() {
    return messageUseCases;
  }

  /**
   * Gets reaction use cases.
   *
   * @return the reaction use cases
   */
  public ReactionUseCases getReactionUseCases() {
    return reactionUseCases;
  }

  /**
   * Gets upload use cases.
   *
   * @return the upload use cases
   */
  public UploadUseCases getUploadUseCases() {
    return uploadUseCases;
  }

  /**
   * Gets user use cases.
   *
   * @return the user use cases
   */
  public UserUseCases getUserUseCases() {
    return userUseCases;
  }
}
