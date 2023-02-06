package io.tixngo.exampleAndroid.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
//import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory

class FirebaseMessageHelper(val context: Context) {
    companion object {
        private lateinit var singleton: FirebaseMessageHelper
        val instance
            get() = singleton
    }

    init {
        singleton = this
    }

    fun initialize() {
        requestPermissionIfNeed();
    }

    fun getFcmToken(completion: (String?) -> Unit) {
        if (getPermission()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    completion(it.result)
                } else {
                    completion(null)
                }
            }
        } else {
            completion(null)
        }
    }

    private fun requestPermissionIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(context)
                .withPermissions(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        println("onPermissionsChecked: ${p0!!.areAllPermissionsGranted()}")
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        println("onPermissionRationaleShouldBeShown")
                    }
                })
                .check()
        }
    }

    private fun getPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}