package io.tixngo.exampleAndroid.ui.fragment

import DisplayType
import TixngoButtonStyle
import TixngoButtons
import TixngoColor
import TixngoConfiguration
import TixngoFont
import TixngoSDKConfig
import TixngoTheme
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.tixngo.exampleAndroid.service.AwsCognitoService
import io.tixngo.exampleAndroid.databinding.FragmentMainBinding
import io.tixngo.exampleAndroid.service.FirebaseMessageHelper
import io.tixngo.exampleAndroid.ui.activity.LoginActivity
import io.tixngo.sdkutils.TixngoManager

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var currentFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setLoading(isShow: Boolean) {
//        binding.splashscreen.visibility = if (isShow) View.VISIBLE else  View.GONE
    }

    private fun initialize() {
        val activity = requireActivity()
        setLoading(true)
        TixngoManager.instance.initialize(
            context = activity,
            config = TixngoConfiguration(
                sskLicenseKey = "MEYCIQDO4RS/aRJmaKnRZOaq9FOYNehpX9s4FqTdiNf6flbkcAIhANK7ToiL/EANI1vCIRchcny5SHI8cYbzz3KiyfeZf6SX",
                isEnableDebug = true,
                defaultEnv = TixngoEnv.VAL,
                isEnableWallet = false,
                isCheckAppStatus = false,
                supportLanguages = arrayOf(TixngoLanguages.EN),
                defaultLanguage = TixngoLanguages.EN,
                theme = TixngoTheme(
                    font = "qatar2022",
                    colors = TixngoColor(primary = "0xff00968D", secondary = "0xff2B5B6C"),
                    buttons = TixngoButtons(primary = TixngoButtonStyle(background = "0xff00968D", textFont = TixngoFont("qatar2022", size = 16.0)))
                ),
                appId = "io.tixngo.app.sdk",
                config = TixngoSDKConfig(
                    displayType = DisplayType.Present,
                    isHaveMenu = false,
                    isHaveSignOut = false,
                ),
            ),
            onInitializedHandler =
            {
                println("Hltest => onInitializedHandler => $it")
                if (!it) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                }
                setLoading(false)
            },
            onGetJwtTokenHandler =
            {
                println("Hltest => onGetJwtTokenHandler")
                AwsCognitoService.instance.getToken { _, jwtToken ->
                    it.invoke(jwtToken)
                }
            },
            onGetDeviceTokenHandler = {
                println("Hltest => onGetDeviceTokenHandler")
                FirebaseMessageHelper.instance.getFcmToken { fcmToken ->
                    it.invoke(fcmToken)
                }
            },
            onTokenExpiredHandler =
            {
                println("Hltest => onTokenExpiredHandler")
                val shouldRetryOnTokenExpired = false
                it.invoke(shouldRetryOnTokenExpired)
            },
            onCloseHandler =
            {
                println("Hltest => onCloseHandler")
            }
        )
        TixngoManager.instance.setEnv(TixngoEnv.VAL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("HLtest => onViewCreated")
        initialize()
//        val activity = requireActivity()
//
//        binding.home.setOnClickListener {
//            TixngoManager.instance.openHomePage(activity)
//        }
//        binding.myTicket.setOnClickListener {
//            TixngoManager.instance.openEventPage(activity)
//        }
//
//        binding.transferredTicket.setOnClickListener {
//            TixngoManager.instance.openTransferredTicketPage(activity)
//        }
//
//        binding.pendingTicket.setOnClickListener {
//            TixngoManager.instance.openPendingTicketPage(activity)
//        }
//
//        binding.logout.setOnClickListener {
//            TixngoManager.instance.signOut()
//            val intent = Intent(activity, LoginActivity::class.java)
//            activity.startActivity(intent)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}