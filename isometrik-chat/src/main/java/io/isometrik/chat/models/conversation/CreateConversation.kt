package io.isometrik.chat.models.conversation

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import io.isometrik.chat.IMConfiguration
import io.isometrik.chat.builder.conversation.CreateConversationQuery
import io.isometrik.chat.managers.RetrofitManager
import io.isometrik.chat.response.CompletionHandler
import io.isometrik.chat.response.conversation.CreateConversationResult
import io.isometrik.chat.response.error.BaseResponse
import io.isometrik.chat.response.error.ErrorResponse
import io.isometrik.chat.response.error.IsometrikError
import io.isometrik.chat.response.error.IsometrikErrorBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * The helper class to validate request params and make the create conversation request and expose the parsed successful or error response.
 */
class CreateConversation {
    /**
     * Validate params.
     *
     * @param createConversationQuery the create conversation query
     * @param completionHandler the completion handler
     * @param retrofitManager the retrofit manager
     * @param imConfiguration the im configuration
     * @param baseResponse the base response
     * @param gson the gson
     */
    fun validateParams(
        createConversationQuery: CreateConversationQuery,
        completionHandler: CompletionHandler<CreateConversationResult?>,
        retrofitManager: RetrofitManager, imConfiguration: IMConfiguration,
        baseResponse: BaseResponse, gson: Gson
    ) {
        val conversationTitle = createConversationQuery.conversationTitle
        val conversationImageUrl = createConversationQuery.conversationImageUrl
        val customType = createConversationQuery.customType
        val isGroup = createConversationQuery.group
        val members = createConversationQuery.members
        val conversationType = createConversationQuery.conversationType
        val metaData = createConversationQuery.metaData
        val userToken = createConversationQuery.userToken
        val readEvents = createConversationQuery.readEvents
        val typingEvents = createConversationQuery.typingEvents
        val pushNotifications = createConversationQuery.pushNotifications
        val searchableTags = createConversationQuery.searchableTags

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING)
        } else if (conversationType == null) {
            completionHandler.onComplete(
                null,
                IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TYPE_MISSING
            )
        } else if (isGroup == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_IS_GROUP_MISSING)
        } else if (readEvents == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_READ_EVENTS_MISSING)
        } else if (typingEvents == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_TYPING_EVENTS_MISSING)
        } else if (pushNotifications == null) {
            completionHandler.onComplete(
                null,
                IsometrikErrorBuilder.IMERROBJ_PUSH_NOTIFICATIONS_MISSING
            )
        } else if (members == null || members.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEMBERS_MISSING)
        } else if ((conversationType != 0 || isGroup) && (conversationTitle == null || conversationTitle
                .isEmpty())
        ) {
            completionHandler.onComplete(
                null,
                IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TITLE_MISSING
            )
        } else if ((conversationType != 0 || isGroup) && (conversationImageUrl == null
                    || conversationImageUrl.isEmpty())
        ) {
            completionHandler.onComplete(
                null,
                IsometrikErrorBuilder.IMERROBJ_CONVERSATION_IMAGE_URL_MISSING
            )
        } else {
            val headers: MutableMap<String, String> = HashMap()
            headers["licenseKey"] = imConfiguration.licenseKey
            headers["appSecret"] = imConfiguration.appSecret
            headers["userToken"] = userToken

            val bodyParams: MutableMap<String, Any?> = HashMap()

            if (metaData != null) bodyParams["metaData"] =
                JsonParser.parseString(metaData.toString())
            if (customType != null) bodyParams["customType"] = customType
            if (searchableTags != null) bodyParams["searchableTags"] = searchableTags

            bodyParams["members"] = members
            bodyParams["conversationType"] = conversationType
            bodyParams["isGroup"] = isGroup

            bodyParams["readEvents"] = readEvents
            bodyParams["typingEvents"] = typingEvents
            bodyParams["pushNotifications"] = pushNotifications

            if (conversationType != 0 || isGroup) {
                bodyParams["conversationTitle"] = conversationTitle
                bodyParams["conversationImageUrl"] = conversationImageUrl
            }

            val call =
                retrofitManager.conversationService.createConversation(headers, bodyParams)

            call.enqueue(object : Callback<CreateConversationResult?> {
                override fun onResponse(
                    call: Call<CreateConversationResult?>,
                    response: Response<CreateConversationResult?>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 201) {
                            completionHandler.onComplete(response.body(), null)
                        }
                    } else {
                        var errorResponse = try {
                            if (response.errorBody() != null) {
                                gson.fromJson(
                                    response.errorBody()!!.string(),
                                    ErrorResponse::class.java
                                )
                            } else {
                                ErrorResponse()
                            }
                        } catch (e: IOException) {
                            // handle failure to read error
                            ErrorResponse()
                        } catch (e: IllegalStateException) {
                            ErrorResponse()
                        } catch (e: JsonSyntaxException) {
                            ErrorResponse()
                        }

                        var isometrikErrorBuilder =
                            IsometrikError.Builder().setHttpResponseCode(response.code())
                                .setRemoteError(true)

                        isometrikErrorBuilder = when (response.code()) {
                            403 -> baseResponse.handle403responseCode(
                                isometrikErrorBuilder,
                                errorResponse
                            )

                            404 -> baseResponse.handle404responseCode(
                                isometrikErrorBuilder,
                                errorResponse
                            )

                            409 -> baseResponse.handle409responseCode(
                                isometrikErrorBuilder,
                                errorResponse,
                                false
                            )

                            422 -> baseResponse.handle422responseCode(
                                isometrikErrorBuilder,
                                errorResponse
                            )

                            502 -> baseResponse.handle502responseCode(isometrikErrorBuilder)
                            503 -> baseResponse.handle503responseCode(
                                isometrikErrorBuilder,
                                errorResponse
                            )

                            else ->                 //500 response code
                                baseResponse.handle500responseCode(isometrikErrorBuilder)
                        }

                        completionHandler.onComplete(null, isometrikErrorBuilder.build())
                    }
                }

                override fun onFailure(call: Call<CreateConversationResult?>, t: Throwable) {
                    if (t is IOException) {
                        // Network failure
                        completionHandler.onComplete(null, baseResponse.handleNetworkError(t))
                    } else {
                        // Parsing error
                        completionHandler.onComplete(null, baseResponse.handleParsingError(t))
                    }
                }
            })
        }
    }
}
