package io.tixngo.exampleAndroid.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.tixngo.sdkutils.TixngoManager


class FirebaseMessageService() : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        TixngoManager.instance.processFcmTokenIfNeed(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = message.notification
        val data = message.data
        TixngoManager.instance.processFcmMessageIfNeed(notification?.title, notification?.body, data)
    }

}
