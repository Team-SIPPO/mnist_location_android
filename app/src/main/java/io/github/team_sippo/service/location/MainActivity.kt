package io.github.team_sippo.service.location

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = applicationContext

        // Android 6, API 23以上でパーミッシンの確認
        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiPermissions()
        } else {
            startLocationService()
        }
    }

    // 位置情報許可の確認、外部ストレージのPermissionにも対応できるようにしておく
    private fun checkMultiPermissions() {
        // 位置情報の Permission
        val permissionLocation = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        val reqPermissions = ArrayList<String>()

        // 位置情報の Permission が許可されているか確認
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            // 許可済
        } else {
            // 未許可
            reqPermissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // 未許可
        if (!reqPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                reqPermissions.toTypedArray(),
                REQUEST_MULTI_PERMISSIONS
            )
            // 未許可あり
        } else {
            // 許可済
            startLocationService()
        }
    }

    // 結果の受け取り
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_MULTI_PERMISSIONS) {
            if (grantResults.size > 0) {
                for (i in permissions.indices) {
                    // 位置情報
                    if (permissions[i] == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // 許可された
                        } else {
                            // それでも拒否された時の対応
                            toastMake("位置情報の許可がないので計測できません")
                        }
                    }
                }
                startLocationService()
            }
        }
    }

    private fun startLocationService() {

        setContentView(R.layout.activity_main);
        val textView: TextView = findViewById(R.id.log_text);

        val buttonStart: Button = findViewById(R.id.button_start)
        buttonStart.setOnClickListener( {
            val intent = Intent(application, LocationService::class.java)
            // API 26 以降
            startForegroundService(intent)
            // Activityを終了させる
            //finish()
        })

        val buttonLog: Button = findViewById(R.id.button_log)
        buttonLog.setOnClickListener({
            val loc = (application as LocationApplication).location.toString()
            textView.setText(loc)
        })

        val buttonReset: Button = findViewById(R.id.button_reset)
        buttonReset.setOnClickListener({
            // Serviceの停止
            val intent = Intent(application, LocationService::class.java)
            stopService(intent)
            textView.setText("")
        })
    }

    // トーストの生成
    private fun toastMake(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        // 位置調整
        toast.setGravity(Gravity.CENTER, 0, 200)
        toast.show()
    }

    companion object {
        private const val REQUEST_MULTI_PERMISSIONS = 101
    }
}