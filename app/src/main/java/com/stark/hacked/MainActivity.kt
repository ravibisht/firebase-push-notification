package com.stark.hacked

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.stark.hacked.MyFirebaseMessagingService.Companion.DEFAULT_TEST
import com.stark.hacked.MyFirebaseMessagingService.Companion.INTENT_ORIGIN
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var writeMessage: TextView
    private lateinit var handler: Handler
    private lateinit var constraintLayout: ConstraintLayout
    private val backgroundColors = listOf(R.color.red, R.color.green, R.color.black)
    private val textColors = listOf(R.color.black, R.color.red, R.color.green)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        constraintLayout = findViewById(R.id.container_layout)
        writeMessage = findViewById(R.id.writeMessage)

        handler = Handler()

        if (intent != null && intent.getStringExtra(INTENT_ORIGIN).equals(MyFirebaseMessagingService.INTENT_ORIGIN_FIREBASE_SERVICE)) {
            writeMessage.text = intent.getStringExtra(MyFirebaseMessagingService.GET_DATA_FROM_NOTIFICATION)

            startAnimation()
            if (intent.getStringExtra("song_url")==null) {
                val mediaPlayer = MediaPlayer.create(this, R.raw.hacked_song)


                if (mediaPlayer== null){
                    Log.d("TAG", "onCreate: Falied Media Player")

                }else{
                    mediaPlayer.start()
                    if (!mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                    }
                }

            } else {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(intent.getStringExtra("song_url"))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }

        }


    }


    private fun startAnimation() {
        val runnableCode: Runnable = object : Runnable {
            override fun run() {
                var random = Random().nextInt(3)
                constraintLayout.setBackgroundColor(resources.getColor(backgroundColors[random]))
                writeMessage.setTextColor(resources.getColor(textColors[random]))
                handler.postDelayed(this, 100)
            }
        }
        handler.postDelayed(runnableCode, 100)
    }

    private fun askOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            if (isXiaomi()) {
                askPopupPermission()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                startActivityForResult(intent, 333)
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 333 && resultCode == RESULT_OK) {
            if (!Settings.canDrawOverlays(this)) {
                askOverlayPermission()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPopupPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val className = Class.forName("android.os.SystemProperties")
            val get = className.getMethod("get", String::class.java)
            val miui = get.invoke(className, "ro.miui.ui.version.name") as String
            if (miui.contains("11")) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }

    }


    fun isXiaomi(): Boolean {
        return "xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)
    }

}