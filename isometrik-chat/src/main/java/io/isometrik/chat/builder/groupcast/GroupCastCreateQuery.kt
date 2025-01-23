package io.isometrik.chat.builder.groupcast

import org.json.JSONObject

/**
 * The query builder for broadcasting message request.
 */
class GroupCastCreateQuery private constructor(builder: Builder) {
    var groupcastTitle: String
    var groupcastImageUrl: String

    /**
     * Gets user token.
     *
     * @return the user token
     */
    val userToken: String


    /**
     * Gets custom type.
     *
     * @return the custom type
     */
    val customType: String?
    val members: List<Map<String, Any>>?

    /**
     * Gets meta data.
     *
     * @return the meta data
     */
    val metaData: JSONObject?

    /**
     * Gets searchable tags.
     *
     * @return the searchable tags
     */
    val searchableTags: List<String>?

    init {
        this.userToken = builder.userToken
        this.customType = builder.customType
        this.members = builder.members
        this.metaData = builder.metaData
        this.searchableTags = builder.searchableTags
        this.groupcastTitle = builder.groupcastTitle
        this.groupcastImageUrl = builder.groupcastImageUrl
    }

    /**
     * The builder class for building broadcast message query.
     */

    /**
     * Instantiates a new Builder.
     */
    class Builder(
        var userToken: String,
        var groupcastTitle: String,
        var groupcastImageUrl: String
    ) {

        var customType: String? = null
        var members: List<Map<String, Any>>? = null
        var metaData: JSONObject? = null
        var searchableTags: List<String>? = null


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
         * Sets searchable tags.
         *
         * @param searchableTags the searchable tags
         * @return the searchable tags
         */
        fun setSearchableTags(searchableTags: List<String>?): Builder {
            this.searchableTags = searchableTags
            return this
        }

        fun setMemberData(members : List<Map<String, Any>>) : Builder {
            this.members = members
            return this
        }


        /**
         * Build broadcast message query.
         *
         * @return the broadcast message query
         */
        fun build(): GroupCastCreateQuery {
            return GroupCastCreateQuery(this)
        }
    }
}
