package com.stark.hacked

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private  val TAG = "MyFirebaseMessagingService"

    companion object{
        val GET_DATA_FROM_NOTIFICATION = "GET_DATA_FROM_NOTIFICATION";
        val GET_DATA_FROM_NOTIFICATION_VALUE = "Haan Bhai Baja de"
        val GET_DATA_FROM_NOTIFICATION_REMOTE_NOTIFICATION_KEY= "signal"
        val INTENT_ORIGIN="INTENT_ORIGIN"
        val DEFAULT_TEST = "DEFAULT_TEST"
        val INTENT_ORIGIN_FIREBASE_SERVICE = "INTENT_ORIGIN_FIREBASE_SERVICE"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived:  r : " + remoteMessage.data.get("signal"))
        Log.d(TAG, "onMessageReceived: " + remoteMessage.notification?.title)
        Log.d(TAG, "onMessageReceived: " + remoteMessage.notification?.body)

        val data =  remoteMessage.data.get("signal") ?: "NO data"
        val songUrl = remoteMessage.data.get("song_url") ?: null

        val intent = Intent(applicationContext, MainActivity::class.java);
        intent.putExtra(GET_DATA_FROM_NOTIFICATION, data)
        intent.putExtra(INTENT_ORIGIN, INTENT_ORIGIN_FIREBASE_SERVICE)
        intent.putExtra("song_url",songUrl)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d(TAG, "onNewToken: " + p0)
    }
}