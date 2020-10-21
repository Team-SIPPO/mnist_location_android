package io.github.team_sippo.service.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            // 端末起動時に送信されるインテント
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                val startService = Intent(context?.applicationContext, LocationService::class.java)

                // Context.startForegroundService()を使ってServiceを起動する
                context?.startForegroundService(startService)
            }

            // ロック解除時のインテント
            Intent.ACTION_USER_PRESENT -> {
                println("ロックを解除しました")
            }
        }
    }
}