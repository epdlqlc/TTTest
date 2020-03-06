package com.example.davidpark.myapplication

import android.app.Notification
import android.app.Service
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.Toast

class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {
    internal var mManager: ClipboardManager? = null

    override fun onCreate() {
        super.onCreate()
        mManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 리스너 등록
        mManager!!.addPrimaryClipChangedListener(this)

        val builder = NotificationCompat.Builder(this, "1234") // channel ID
                .setContentText("Linker")
        //.setContentIntent(pendingIntent)
        startForeground(1, builder.build())

    }


    override fun onDestroy() {
        super.onDestroy()
        // 리스너 해제
        mManager!!.removePrimaryClipChangedListener(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onPrimaryClipChanged() {
        if (mManager != null && mManager!!.primaryClip != null) {
            check_url()
            Log.v("히", "1")
        } else {
            Log.v("히", "2")
        }
    }

    fun recognize_Url(str:String) {
        val myToast = Toast.makeText(this.applicationContext, "인식된 링크 : "+ str, Toast.LENGTH_LONG)
        myToast.show()
    }

    fun check_url() {

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var pasteData = ""

// 클립보드에 데이터가 없거나 텍스트 타입이 아닌 경우
        if (!clipboard.hasPrimaryClip()) {
        } else if (!clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN)) {
        } else {
            val item = clipboard.getPrimaryClip().getItemAt(0)
            pasteData = item.getText().toString()

            Log.v("히히","텍스트가 있긴해요 : " + pasteData)
        }
        if(Patterns.WEB_URL.matcher(pasteData).matches()) {
            Log.v("히히","Url이 있긴해요")
            recognize_Url(pasteData)
        }
    }
}
