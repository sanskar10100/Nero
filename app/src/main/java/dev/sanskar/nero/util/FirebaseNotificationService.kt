package dev.sanskar.nero.util

import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New FCM token: $token")
    }
}