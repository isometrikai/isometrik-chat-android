package io.isometrik.chat.builder.groupcast

import android.app.usage.UsageEvents.Event
import io.isometrik.chat.response.message.utils.fetchmessages.MentionedUser
import io.isometrik.chat.response.message.utils.schemas.Attachment
import io.isometrik.chat.response.message.utils.schemas.EventForMessage
import org.json.JSONObject

/**
 * The query builder for broadcasting message request.
 */
class GroupCastMessageQuery private constructor(builder: Builder) {
    /**
     * Gets message type.
     *
     * @return the message type
     */
    val deliveryCount: Int?
    val readCount: Int?
    val messageType: Int?

    /**
     * Gets show in conversation.
     *
     * @return the show in conversation
     */
    val showInConversation: Boolean?
    val notifyOnCompletion: Boolean?
    val hideNewConversationsForSender: Boolean?

    val sendPushForNewConversationCreated: Boolean?

    /**
     * Gets body.
     *
     * @return the body
     */
    val body: String?

    /**
     * Gets encrypted.
     *
     * @return the encrypted
     */
    val encrypted: Boolean?

    /**
     * Gets user token.
     *
     * @return the user token
     */
    val userToken: String?

    /**
     * Gets device id.
     *
     * @return the device id
     */
    val deviceId: String?
    val groupcastId: String?

    /**
     * Gets event for message.
     *
     * @return the event for message
     */
    val eventForMessage: EventForMessage?

    /**
     * Gets custom type.
     *
     * @return the custom type
     */
    val customType: String?

    /**
     * Gets user ids.
     *
     * @return the user ids
     */
    val userIds: List<String>?
    val mentionedUsers: List<MentionedUser>?

    /**
     * Gets meta data.
     *
     * @return the meta data
     */
    val metaData: JSONObject?

    /**
     * Gets attachments.
     *
     * @return the attachments
     */
    val attachments: List<Attachment>?

    /**
     * Gets searchable tags.
     *
     * @return the searchable tags
     */
    val searchableTags: List<String>?
    val notificationTitle: String?
    val notificationBody: String?
    val events: EventForMessage?

    init {
        this.readCount = builder.readCount
        this.deliveryCount = builder.deliveryCount
        this.messageType = builder.messageType
        this.showInConversation = builder.showInConversation
        this.notifyOnCompletion = builder.notifyOnCompletion
        this.hideNewConversationsForSender = builder.hideNewConversationsForSender
        this.sendPushForNewConversationCreated = builder.sendPushForNewConversationCreated
        this.body = builder.body
        this.encrypted = builder.encrypted
        this.userToken = builder.userToken
        this.deviceId = builder.deviceId
        this.groupcastId = builder.groupcastId

        this.eventForMessage = builder.eventForMessage
        this.customType = builder.customType
        this.userIds = builder.userIds
        this.metaData = builder.metaData
        this.attachments = builder.attachments
        this.searchableTags = builder.searchableTags
        this.notificationTitle = builder.notificationTitle
        this.notificationBody = builder.notificationBody
        this.mentionedUsers = builder.mentionedUsers
        this.events = builder.events
    }

    /**
     * The builder class for building broadcast message query.
     */
    class Builder
    /**
     * Instantiates a new Builder.
     */
    {
        var messageType: Int? = null
        var readCount: Int? = null
        var deliveryCount: Int? = null
        var showInConversation: Boolean? = null
        var notifyOnCompletion: Boolean? = null
        var hideNewConversationsForSender: Boolean? = null
        var sendPushForNewConversationCreated: Boolean? = null
        var body: String? = null
        var encrypted: Boolean? = null
        var userToken: String? = null
        var deviceId: String? = null
        var groupcastId: String? = null

        var eventForMessage: EventForMessage? = null
        var customType: String? = null
        var userIds: List<String>? = null
        var metaData: JSONObject? = null
        var attachments: List<Attachment>? = null
        var searchableTags: List<String>? = null
        var notificationTitle: String? = null
        var notificationBody: String? = null
        var mentionedUsers: List<MentionedUser>? = null
        var events: EventForMessage? = null

        /**
         * Sets message type.
         *
         * @param messageType the message type
         * @return the message type
         */
        fun setMessageType(messageType: Int?): Builder {
            this.messageType = messageType
            return this
        }

        /**
         * Sets show in conversation.
         *
         * @param showInConversation the show in conversation
         * @return the show in conversation
         */
        fun setShowInConversation(showInConversation: Boolean?): Builder {
            this.showInConversation = showInConversation
            return this
        }

        /**
         * Sets body.
         *
         * @param body the body
         * @return the body
         */
        fun setBody(body: String?): Builder {
            this.body = body
            return this
        }

        /**
         * Sets encrypted.
         *
         * @param encrypted the encrypted
         * @return the encrypted
         */
        fun setEncrypted(encrypted: Boolean?): Builder {
            this.encrypted = encrypted
            return this
        }

        /**
         * Sets event for message.
         *
         * @param eventForMessage the event for message
         * @return the event for message
         */
        fun setEventForMessage(eventForMessage: EventForMessage?): Builder {
            this.eventForMessage = eventForMessage
            return this
        }

        /**
         * Sets custom type.
         *
         * @param customType the custom type
         * @return the custom type
         */
        fun setCustomType(customType: String?): Builder {
            this.customType = customType
            return this
        }

        /**
         * Sets user ids.
         *
         * @param userIds the user ids
         * @return the user ids
         */
        fun setUserIds(userIds: List<String>?): Builder {
            this.userIds = userIds
            return this
        }

        /**
         * Sets meta data.
         *
         * @param metaData the meta data
         * @return the meta data
         */
        fun setMetaData(metaData: JSONObject?): Builder {
            this.metaData = metaData
            return this
        }

        /**
         * Sets attachments.
         *
         * @param attachments the attachments
         * @return the attachments
         */
        fun setAttachments(attachments: List<Attachment>?): Builder {
            this.attachments = attachments
            return this
        }

        /**
         * Sets device id.
         *
         * @param deviceId the device id
         * @return the device id
         */
        fun setDeviceId(deviceId: String?): Builder {
            this.deviceId = deviceId
            return this
        }

        /**
         * Sets searchable tags.
         *
         * @param searchableTags the searchable tags
         * @return the searchable tags
         */
        fun setSearchableTags(searchableTags: List<String>?): Builder {
            this.searchableTags = searchableTags
            return this
        }

        /**
         * Sets user token.
         *
         * @param userToken the user token
         * @return the user token
         */
        fun setUserToken(userToken: String?): Builder {
            this.userToken = userToken
            return this
        }

        /**
         * Build broadcast message query.
         *
         * @return the broadcast message query
         */
        fun build(): GroupCastMessageQuery {
            return GroupCastMessageQuery(this)
        }
    }
}
