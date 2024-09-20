package io.isometrik.chat.network

import android.util.Log
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import io.isometrik.chat.Isometrik
import io.isometrik.chat.enums.IMRealtimeEventsVerbosity
import io.isometrik.chat.events.connection.DisconnectEvent
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * The class to handle isometrik connection to receive realtime message and events.
 */
class IsometrikConnection(private var isometrikInstance: Isometrik) {
    private var mqttClient: Mqtt3AsyncClient? = null
    private var messagingTopic: String? = null
    private var presenceEventsTopic: String? = null
    private var scheduler: ScheduledExecutorService? = null
    private var ATTEMP = 0L

    /**
     * Create connection.
     *
     * @param isometrikInstance the isometrik instance
     * @see io.isometrik.chat.Isometrik
     */
    fun createConnection(isometrikInstance: Isometrik) {
        this.isometrikInstance = isometrikInstance
        val imConfiguration = isometrikInstance.configuration
        val baseConnectionPath = isometrikInstance.connectionBaseUrl
        val port = isometrikInstance.port


        val accountId = imConfiguration.accountId
        val projectId = imConfiguration.projectId
//        val keysetId = imConfiguration.keysetId
//        val licenseKey = imConfiguration.licenseKey
        val clientId = imConfiguration.clientId

        val clientIdForTopicSubscribe = clientId.substring(0, 24)


        val username = imConfiguration.userName
        val password = imConfiguration.password

        this.messagingTopic = "/$accountId/$projectId/Message/$clientIdForTopicSubscribe"
        this.presenceEventsTopic =
            "/$accountId/$projectId/Status/$clientIdForTopicSubscribe"

        checkConnectionStatus(baseConnectionPath, port, clientId, username, password)
    }

    /**
     * @param baseConnectionPath path to be used for creating connection
     * @param clientId id of the client making the connection(should be unique)
     * @param username username to be unnecsed for authentication at time of connection
     * @param password password to be used for authentication at time of connection
     */
    private fun checkConnectionStatus(
        baseConnectionPath: String, port: Int, clientId: String, username: String,
        password: String
    ) {
        val uniqueClientId = clientId + "CHAT"
        if (isometrikInstance.configuration.realtimeEventsVerbosity
            == IMRealtimeEventsVerbosity.FULL
        ) {
            Log.d(
                ISOMETRIK_MQTT_TAG,
                " ClientId: $uniqueClientId username: $username password: $password baseConnectionPath: $baseConnectionPath"
            )
        }

        if (mqttClient != null && mqttClient!!.state.isConnected) {

            Log.d(
                ISOMETRIK_MQTT_TAG,
                "  Already Connected ClientId: $uniqueClientId username: $username password: $password baseConnectionPath: $baseConnectionPath"
            )
        } else {
            mqttClient = MqttClient.builder()
                .useMqttVersion3()
                .identifier(uniqueClientId)
                .serverHost(baseConnectionPath.replace("tcp://",""))
                .serverPort(port)
                .addDisconnectedListener { context ->
                    this@IsometrikConnection.onDisconnected(
                        context,username, password) }
                .buildAsync()

            connectToBroker(username, password)

            // Set message listener
            mqttClient!!.publishes(MqttGlobalPublishFilter.ALL, this::onMqttDataReceived)
        }

    }

    private fun connectToBroker(username: String, password: String) {
        mqttClient!!.connectWith()
            .keepAlive(60)
            .cleanSession(true)
            .simpleAuth()
            .username(username)
            .password(password.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    // handle failure
                    Log.d(ISOMETRIK_MQTT_TAG, "Connection failed due to $throwable")
                    scheduleReconnect(username, password)
                } else {
                    // matt connected
                    if (isometrikInstance.configuration.realtimeEventsVerbosity
                        == IMRealtimeEventsVerbosity.FULL
                    ) {
                        Log.d(ISOMETRIK_MQTT_TAG, "Connected")
                    }

                    subscribeToTopic(messagingTopic!!,0)
                    subscribeToTopic(presenceEventsTopic!!,0)
                }
            }
    }

    private fun onMqttDataReceived(message: Mqtt3Publish) {
        val topic = message.topic.toString()

        if (topic == messagingTopic) {
            if (isometrikInstance.configuration.realtimeEventsVerbosity
                == IMRealtimeEventsVerbosity.FULL
            ) {
                Log.d(
                    "Realtime Event-Message",
                    JSONObject(String(message.payloadAsBytes)).toString()
                )
            }
            isometrikInstance.messageEvents
                .handleMessageEvent(
                    JSONObject(String(message.payloadAsBytes)),
                    isometrikInstance
                )
        } else if (topic == presenceEventsTopic) {
            if (isometrikInstance.configuration.realtimeEventsVerbosity
                == IMRealtimeEventsVerbosity.FULL
            ) {
                Log.d(
                    "Realtime Event-Presence",
                    JSONObject(String(message.payloadAsBytes)).toString()
                )
            }

            isometrikInstance.presenceEvents
                .handlePresenceEvent(
                    JSONObject(String(message.payloadAsBytes)),
                    isometrikInstance
                )
        }
    }

    private fun onDisconnected(
        context: MqttClientDisconnectedContext,
        username: String,
        password: String
    ) {
        Log.d(ISOMETRIK_MQTT_TAG, " Disconnected due to ${context.cause.message}")
        isometrikInstance.realtimeEventsListenerManager
            .connectionListenerManager
            .announce(DisconnectEvent(context.cause))

        scheduleReconnect(username, password)
    }

    private fun scheduleReconnect(username: String, password: String) {
        if (mqttClient == null) {
            return
        }
        if (scheduler == null || scheduler?.isShutdown == true) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
        }
        ATTEMP++
        Log.d(ISOMETRIK_MQTT_TAG, " scheduleReconnect")
        scheduler?.schedule({ connectToBroker(username,password) }, ATTEMP*10, TimeUnit.SECONDS)
    }

    fun subscribeToTopic(topic: String, qos: Int) {
        mqttClient?.subscribeWith()
            ?.topicFilter(topic)
            ?.qos(MqttQos.AT_MOST_ONCE)
            ?.callback { publish ->
                Log.e("MQTT_CHAT_SUBSCRIBE", "==> ${publish.topic}")
            }
            ?.send()
//            ?.whenComplete { subAck, throwable ->
//                if (throwable != null) {
//                    // Handle failure to subscribe
//                    Log.e("MQTT_CHAT_SUBSCRIBE", "$topic Error ==> $throwable")
//                } else {
//                    // Handle successful subscription,
//                    Log.e("MQTT_CHAT_SUBSCRIBE", "$topic Success")
//                }
//            }
    }

    private fun convertMessageToJsonObject(message: ByteArray): JSONObject {
        var obj = JSONObject()
        try {
            obj = JSONObject(String(message))
        } catch (ignored: JSONException) {
        }
        return obj
    }



    /**
     * Drop connection to isometrik backend.
     *
     * @param force whether to drop a connection forcefully or allow graceful drop of connection
     */
    fun dropConnection(force: Boolean) {
        if ( mqttClient?.state?.isConnected == true) {
            mqttClient?.disconnect()
        }
    }

    /**
     * Drop connection to isometrik backend.
     */
    fun dropConnection() {
        if ( mqttClient?.state?.isConnected == true) {
            mqttClient?.disconnect()
        }
        mqttClient = null
    }

    /**
     * Re connect to isometrik backend.
     */
    fun reConnect() {
        // reconnect manage automatically
    }

    fun isConnected() : Boolean{
//        Log.e(ISOMETRIK_MQTT_TAG,"isConnected ${mqttClient?.state?.isConnected}")
        return  mqttClient?.state?.isConnected == true
    }

    companion object {
        var ISOMETRIK_MQTT_TAG: String = "ISOMETRIK_MQTT: CHAT"
    }
}
