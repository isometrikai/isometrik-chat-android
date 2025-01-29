package io.isometrik.ui

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import io.isometrik.chat.IMConfiguration
import io.isometrik.chat.Isometrik
import io.isometrik.chat.enums.IMLogVerbosity
import io.isometrik.chat.enums.IMRealtimeEventsVerbosity
import io.isometrik.chat.utils.UserSession
import io.isometrik.ui.messages.chat.ChatActionsClickListener
import kotlin.concurrent.Volatile

/**
 * The IsometrikUiSdk singleton to expose sdk functionality to other modules.
 */
class IsometrikChatSdk private constructor() {
    private var isometrik: Isometrik? = null
    private var userSession: UserSession? = null

    /**
     * Gets application id.
     *
     * @return the application id
     */
    var applicationId: String? = null
        private set

    /**
     * Gets application name.
     *
     * @return the application name
     */
    var applicationName: String? = null
        private set
    private var applicationContext: Context? = null
    var chatActionsClickListener: ChatActionsClickListener? = null
        private set

    /**
     * private constructor.
     */
    init {
        //Prevent form the reflection api.

        if (isometrikChatSdk != null) {
            throw RuntimeException(
                "Use getInstance() method to get the single instance of this class."
            )
        }
    }

    /**
     * Sdk initialize.
     *
     * @param applicationContext the application context
     */
    fun sdkInitialize(applicationContext: Context) {
        if (applicationContext == null) {
            throw RuntimeException(
                "Sdk initialization failed as application context cannot be null."
            )
        } else if (applicationContext !is Application) {
            throw RuntimeException(
                "Sdk initialization failed as context passed in parameter is not application context."
            )
        }

        this.applicationContext = applicationContext
    }

    /**
     * Create configuration.
     *
     * @param appSecret the app secret
     * @param userSecret the user secret
     * @param accountId the accountId
     * @param projectId the projectId
     * @param keysetId the keysetId
     * @param userName the userName
     * @param password the password
     * @param licenseKey the license key
     * @param applicationId the application id
     * @param applicationName the application name
     * @param googlePlacesApiKey the google places api key
     * @param giphyApiKey the giphy api key
     */
    @JvmOverloads
    fun createConfiguration(
        appSecret: String,
        userSecret: String,
        accountId: String,
        projectId: String,
        keysetId: String,
        userName: String? = null,
        password: String? = null,
        licenseKey: String,
        applicationId: String,
        applicationName: String,
        googlePlacesApiKey: String,
        giphyApiKey: String
    ) {
        if (applicationContext == null) {
            throw RuntimeException("Initialize the sdk before creating configuration.")
        } else if (appSecret == null || appSecret.isEmpty()) {
            throw RuntimeException("Pass a valid appSecret for isometrik sdk initialization.")
        } else if (userSecret == null || userSecret.isEmpty()) {
            throw RuntimeException("Pass a valid userSecret for isometrik sdk initialization.")
        } else if (accountId == null || accountId.isEmpty()) {
            throw RuntimeException("Pass a valid accountId for isometrik sdk initialization.")
        } else if (projectId == null || projectId.isEmpty()) {
            throw RuntimeException("Pass a valid projectId for isometrik sdk initialization.")
        } else if (keysetId == null || keysetId.isEmpty()) {
            throw RuntimeException("Pass a valid keysetId for isometrik sdk initialization.")
        } else if (licenseKey == null || licenseKey.isEmpty()) {
            throw RuntimeException("Pass a valid licenseKey for isometrik sdk initialization.")
        } else if (googlePlacesApiKey == null || googlePlacesApiKey.isEmpty()) {
            throw RuntimeException(
                "Pass a valid googlePlacesApiKey for isometrik sdk initialization."
            )
        } else if (giphyApiKey == null || giphyApiKey.isEmpty()) {
            throw RuntimeException(
                "Pass a valid giphyApiKey for isometrik sdk initialization."
            )
        } else if (applicationId == null || applicationId.isEmpty()) {
            throw RuntimeException("Pass a valid applicationId for isometrik sdk initialization.")
        } else if (applicationName == null || applicationName.isEmpty()) {
            throw RuntimeException("Pass a valid applicationName for isometrik sdk initialization.")
        }

        val mUserName = if (userName.isNullOrBlank()) {
            "2$accountId$projectId"
        } else userName

        val mPassword = if (password.isNullOrBlank()) {
            licenseKey + keysetId
        } else {
            password
        }
        this.applicationId = applicationId
        this.applicationName = applicationName
        val imConfiguration =
            IMConfiguration(
                applicationContext, licenseKey, appSecret, userSecret,
                accountId, projectId, keysetId, mUserName, mPassword, googlePlacesApiKey, giphyApiKey
            )
        imConfiguration.logVerbosity = IMLogVerbosity.BODY
        imConfiguration.realtimeEventsVerbosity = IMRealtimeEventsVerbosity.FULL
        isometrik = Isometrik(imConfiguration)
        userSession = UserSession(applicationContext)
        //    setGooglePlacesApiKey(googlePlacesApiKey);
    }

    private fun setGooglePlacesApiKey(apiKey: String) {
        try {
            // Get the ApplicationInfo object which contains the meta-data
            val appInfo = applicationContext!!.packageManager.getApplicationInfo(
                applicationContext!!.packageName, PackageManager.GET_META_DATA
            )

            // Update the API key in the meta-data
            if (appInfo.metaData != null) {
                appInfo.metaData.putString("com.google.android.geo.API_KEY", apiKey)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * Gets isometrik.
     *
     * @return the isometrik
     */
    fun getIsometrik(): Isometrik {
        if (isometrik == null) {
            throw RuntimeException("Create configuration before trying to access isometrik object.")
        }

        return isometrik!!
    }

    /**
     * Gets user session.
     *
     * @return the user session
     */
    fun getUserSession(): UserSession {
        if (userSession == null) {
            throw RuntimeException(
                "Create configuration before trying to access user session object."
            )
        }

        return userSession!!
    }

    val context: Context?
        /**
         * Gets context.
         *
         * @return the context
         */
        get() {
            if (isometrik == null) {
                throw RuntimeException("Create configuration before trying to access context object.")
            }
            return applicationContext
        }

    /**
     * On terminate.
     */
    fun onTerminate() {
        if (isometrik == null) {
            throw RuntimeException("Create configuration before trying to access isometrik object.")
        }


        isometrik!!.destroy()
    }

    fun dropConnection() {
        if (isometrik != null) {
            isometrik!!.dropConnection()
        }
    }

    val isConnected: Boolean
        get() {
            if (isometrik != null) {
                return isometrik!!.isConnected
            }
            return false
        }

    /**
     * register conversations Action callbacl
     */
    fun addClickListeners(chatActionsClickListener: ChatActionsClickListener?) {
        this.chatActionsClickListener = chatActionsClickListener
    }

    companion object {
        @Volatile
        private var isometrikChatSdk: IsometrikChatSdk? = null

        @JvmStatic
        val instance: IsometrikChatSdk?
            /**
             * Gets instance.
             *
             * @return the IsometrikUiSdk instance
             */
            get() {
                if (isometrikChatSdk == null) {
                    synchronized(IsometrikChatSdk::class.java) {
                        if (isometrikChatSdk == null) {
                            isometrikChatSdk = IsometrikChatSdk()
                        }
                    }
                }
                return isometrikChatSdk
            }
    }
}
