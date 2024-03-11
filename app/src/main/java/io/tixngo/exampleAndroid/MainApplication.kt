package io.tixngo.exampleAndroid

import android.app.Application
import android.graphics.fonts.FontStyle
import com.amazonaws.regions.Regions
import io.tixngo.exampleAndroid.service.AwsCognitoService
import io.tixngo.exampleAndroid.service.FirebaseMessageHelper
import com.xuning.native_font.NativeFontPlugin
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

class MainApplication : Application() {

    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        NativeFontPlugin.setFontDataHandler { familyName, weight, isItalic, fontCallBack ->
            var familyId = R.font.qatar2022_regular

            if (familyName.equals("qatar2022")) {
                if (weight == FontStyle.FONT_WEIGHT_BOLD) {
                    familyId = R.font.qatar2022_bold
                } else if (weight == FontStyle.FONT_WEIGHT_NORMAL) {
                    familyId = R.font.qatar2022_regular
                } else if (weight == FontStyle.FONT_WEIGHT_MEDIUM) {
                    familyId = R.font.qatar2022_medium
                }

                fontCallBack.onFontLoadCompleted(fontResToByteBuffer(familyId))
            }
        }
        // SDK
        // AwsCognitoService(this, Regions.EU_WEST_1, "69g6etb9kiendmj04gombdpnib", "eu-west-1_wNu7WMfPC")

        // FWWC23
        AwsCognitoService(this, Regions.EU_WEST_1,
            "1dblf7p82qg23pt4plusr4muf8",
            "eu-west-1_Uk3AzbQZ0")

//        App ID: com.fifa.tournament
//                Pool Name: pp26-tixngo_spectators-com.fifa.tournament
//        User pool ID: us-east-1_1BABV9GDI
//        Client ID: 29bqrftt94022e1jbrop2j01ec
//        API: https://v7ps26pl55.execute-api.us-east-1.amazonaws.com/v3

        AwsCognitoService(this, Regions.US_EAST_1,
            "29bqrftt94022e1jbrop2j01ec",
            "us-east-1_1BABV9GDI")
        FirebaseMessageHelper(this).initialize()
    }

    private fun fontResToByteBuffer(fontId: Int): ByteBuffer? {
        val inputStream: InputStream = resources.openRawResource(fontId)
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var n = 0
        while (true) {
            try {
                inputStream.read(buffer).also { n = it }
                if (-1 == n) {
                    inputStream.close()
                    output.close()
                    break
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            output.write(buffer, 0, n)
        }
        val byteBuffer = ByteBuffer.allocateDirect(output.toByteArray().size)
        byteBuffer.put(output.toByteArray())
        return byteBuffer
    }
}