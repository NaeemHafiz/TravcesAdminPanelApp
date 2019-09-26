package com.example.travcesadminpanelapp.data.remote.puhser

import android.util.Log
import com.example.travcesadminpanelapp.MyApplication
import com.example.travcesadminpanelapp.data.remote.puhser.model.PusherEvent
import com.example.travcesadminpanelapp.data.sharedPreferences.AppPreferences
import com.example.travcesadminpanelapp.utils.rxBus.RxBus
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.util.HttpAuthorizer

const val EVENT_NAME_ANNOUNCEMENT = "App\\Events\\Announcements"
const val EVENT_NAME_LOCATION_UPDATE = "App\\Events\\SendCoordinates"

class MyPusherManager private constructor() {

    //TODO : Channel format -> private-users.{userId}.notifications

    var pusher: Pusher
    var myChannel: Channel? = null
    var privateChannelNotification: Channel? = null
    lateinit var privateChannel: PrivateChannel
    var privateChannelRiderLocationUpdates: Channel? = null

    init {
        val authorizer = HttpAuthorizer("http://travces.com/travces/public/api/auth")
        val options = PusherOptions()
        options.setCluster("ap2")
        options.authorizer = authorizer
        pusher = Pusher("8332aaa9ad861498c559", options)
    }

    fun connect() {
        pusher.connect()
        subscribeToNotifications()
    }

    fun disconnectPusher() {

//        unsubscribeToNotifications()
//        pusher.disconnect()
    }

    private fun subscribeToNotifications() {
        privateChannelNotification = pusher.getPrivateChannel(
            "private-user." +
                    "${AppPreferences(MyApplication.instance.applicationContext).getUser().user.id}.announcements"
        )

        if (privateChannelNotification == null) privateChannelNotification =
            pusher.subscribePrivate(
                "private-user." +
                        "${AppPreferences(MyApplication.instance.applicationContext).getUser().user.id}.announcements"
            )

        privateChannelNotification!!.bind(
            EVENT_NAME_ANNOUNCEMENT, object : PrivateChannelEventListener {
                override fun onEvent(channelName: String, eventName: String, data: String) {
                    RxBus.defaultInstance().send(PusherEvent(channelName, eventName, data))
                }

                override fun onAuthenticationFailure(p0: String?, p1: Exception?) {
                    Log.e(TAG, p0)
                }

                override fun onSubscriptionSucceeded(p0: String?) {
                    Log.e(TAG, p0)
                }
            })
    }

    fun subscribeToPrivateLocationUpdateChannelPusher(driverId: String) {
        privateChannelRiderLocationUpdates = pusher.getPrivateChannel(
            "private-user.$driverId.coordinates"
        )
        if (privateChannelRiderLocationUpdates == null) privateChannelRiderLocationUpdates =
            pusher.subscribePrivate(
                "private-user.$driverId.coordinates"
            )

        privateChannelRiderLocationUpdates!!.bind(
            EVENT_NAME_LOCATION_UPDATE, object : PrivateChannelEventListener {
                override fun onEvent(channelName: String, eventName: String, data: String) {
                    RxBus.defaultInstance().send(PusherEvent(channelName, eventName, data))
                }

                override fun onAuthenticationFailure(p0: String?, p1: Exception?) {
                    Log.e(TAG, p0)
                }

                override fun onSubscriptionSucceeded(p0: String?) {
                    Log.e(TAG, p0)
                }
            })
    }


    private fun unsubscribeToNotifications() {
        if (myChannel != null && myChannel!!.isSubscribed) pusher.unsubscribe(myChannel!!.name)
//        if(privateChannelNotification != null && privateChannelNotification!!.isSubscribed) pusher.unsubscribe(privateChannelNotification!!.name)
    }

    fun unsubscribeToPrivateLocationUpdateChannelPusher() {

        if (privateChannelRiderLocationUpdates?.isSubscribed!!) pusher.unsubscribe(
            privateChannelRiderLocationUpdates!!.name
        )
    }

    fun subscribeToPrivateLocationUpdateChannelPusherA(
        driverId: String,
        riderId: String,
        privateChannelEventListener: PrivateChannelEventListener
    ) {
        if (privateChannel.isSubscribed) return

        privateChannel = pusher.subscribePrivate("private-channel-$driverId-$riderId")
        privateChannel.bind("client-driver-location", privateChannelEventListener)
    }

    companion object {
        @JvmStatic
        var TAG: String = MyPusherManager::class.java.simpleName
        private var ourInstance: MyPusherManager? = null

        val instance: MyPusherManager
            get() {

                if (ourInstance == null) ourInstance = MyPusherManager()

                return ourInstance as MyPusherManager
            }
    }
}
