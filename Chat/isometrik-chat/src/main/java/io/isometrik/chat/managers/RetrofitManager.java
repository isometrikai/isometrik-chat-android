package io.isometrik.chat.managers;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.enums.IMLogVerbosity;
import io.isometrik.chat.services.ConversationService;
import io.isometrik.chat.services.DeliveryStatusService;
import io.isometrik.chat.services.MembershipControlService;
import io.isometrik.chat.services.MessageService;
import io.isometrik.chat.services.ReactionService;
import io.isometrik.chat.services.UserService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The manager class for Retrofit and OkHttpClient client for Message, User, Conversation,
 * DeliveryStatus, MembershipControl and Reaction remote api calls.
 */
public class RetrofitManager {
  private final Isometrik isometrik;

  private final OkHttpClient transactionClientInstance;

  private final MessageService messageService;
  private final UserService userService;
  private final ConversationService conversationService;
  private final DeliveryStatusService deliveryStatusService;
  private final MembershipControlService membershipControlService;
  private final ReactionService reactionService;

  /**
   * Instantiates a new Retrofit manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.chat.Isometrik
   */
  public RetrofitManager(Isometrik isometrikInstance) {
    this.isometrik = isometrikInstance;
    this.transactionClientInstance =
        createOkHttpClient(this.isometrik.getConfiguration().getRequestTimeout(),
            this.isometrik.getConfiguration().getConnectTimeout());
    Retrofit transactionInstance = createRetrofit(this.transactionClientInstance);

    this.messageService = transactionInstance.create(MessageService.class);
    this.userService = transactionInstance.create(UserService.class);
    this.conversationService = transactionInstance.create(ConversationService.class);
    this.deliveryStatusService = transactionInstance.create(DeliveryStatusService.class);
    this.membershipControlService = transactionInstance.create(MembershipControlService.class);
    this.reactionService = transactionInstance.create(ReactionService.class);
  }

  /**
   * Create OkHttpClient instance.
   *
   * @return OkHttpClient instance
   * @see okhttp3.OkHttpClient
   */
  private OkHttpClient createOkHttpClient(int requestTimeout, int connectTimeOut) {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.retryOnConnectionFailure(false);
    httpClient.readTimeout(requestTimeout, TimeUnit.SECONDS);
    httpClient.connectTimeout(connectTimeOut, TimeUnit.SECONDS);

    if (isometrik.getConfiguration().getLogVerbosity() == IMLogVerbosity.BODY) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      httpClient.addInterceptor(logging);
    }

    return httpClient.build();
  }

  /**
   * Create retrofit instance.
   *
   * @return retrofit instance
   * @see retrofit2.Retrofit
   */
  private Retrofit createRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(isometrik.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  /**
   * Gets message service.
   *
   * @return the message service
   */
  public MessageService getMessageService() {
    return messageService;
  }

  /**
   * Gets user service.
   *
   * @return the user service
   */
  public UserService getUserService() {
    return userService;
  }

  /**
   * Gets conversation service.
   *
   * @return the conversation service
   */
  public ConversationService getConversationService() {
    return conversationService;
  }

  /**
   * Gets delivery status service.
   *
   * @return the delivery status service
   */
  public DeliveryStatusService getDeliveryStatusService() {
    return deliveryStatusService;
  }

  /**
   * Gets membership control service.
   *
   * @return the membership control service
   */
  public MembershipControlService getMembershipControlService() {
    return membershipControlService;
  }

  /**
   * Gets reaction service.
   *
   * @return the reaction service
   */
  public ReactionService getReactionService() {
    return reactionService;
  }

  /**
   * Destroy.
   *
   * @param force whether to destroy forcibly
   */
  public void destroy(boolean force) {
    if (this.transactionClientInstance != null) {
      closeExecutor(this.transactionClientInstance, force);
    }
  }

  /**
   * Closes the OkHttpClient execute
   *
   * @param client OkHttpClient whose requests are to be canceled
   * @param force whether to forcibly shutdown the OkHttpClient and evict all connection pool
   */
  private void closeExecutor(OkHttpClient client, boolean force) {
    try {
      new Thread(() -> {
        client.dispatcher().cancelAll();
        if (force) {

          ExecutorService executorService = client.dispatcher().executorService();
          executorService.shutdown();
          client.connectionPool().evictAll();
        }
      }).start();
    } catch (Exception ignore) {
    }
  }
}

