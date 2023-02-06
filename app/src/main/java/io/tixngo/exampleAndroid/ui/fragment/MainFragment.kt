package io.tixngo.exampleAndroid.ui.fragment

import TixngoColor
import TixngoConfiguration
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setLoading(isShow: Boolean) {
        binding.splashscreen.visibility = if (isShow) View.VISIBLE else  View.GONE
    }

    private fun initialize() {
        val activity = requireActivity()
        setLoading(true)
        TixngoManager.instance.initialize(
            context = activity,
            config = TixngoConfiguration(
                sskLicenseKey = "",
                isEnableDebug = true,
                defaultEnv = TixngoEnv.INT,
                isEnableWallet = false,
                isCheckAppStatus = false,
                supportLanguages = arrayOf(TixngoLanguages.EN),
                defaultLanguage = TixngoLanguages.EN,
                theme = TixngoTheme(
                    font = "qatar2022",
                    colors = TixngoColor(primary = "0xff560055")
                ),
                appId = "io.tixngo.app",
            ),
            onInitializedHandler =
            {
                if (!it) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                }
                setLoading(false)
            },
            onGetJwtTokenHandler =
            {
                AwsCognitoService.instance.getToken { _, jwtToken ->
                    it.invoke(jwtToken)
                }
            },
            onGetDeviceTokenHandler = {
                FirebaseMessageHelper.instance.getFcmToken { fcmToken ->
                    it.invoke(fcmToken)
                }
            },
            onTokenExpiredHandler =
            {
                val shouldRetryOnTokenExpired = false
                it.invoke(shouldRetryOnTokenExpired)
            },
            onCloseHandler =
            {
                println("onCloseHandler")
            }
        )
//        TixngoManager.instance.setEnv(TixngoEnv.INT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        val activity = requireActivity()

        binding.home.setOnClickListener {
            TixngoManager.instance.openHomePage(activity)
        }
        binding.myTicket.setOnClickListener {
            TixngoManager.instance.openEventPage(activity)
        }

        binding.transferredTicket.setOnClickListener {
            TixngoManager.instance.openTransferredTicketPage(activity)
        }

        binding.pendingTicket.setOnClickListener {
            TixngoManager.instance.openPendingTicketPage(activity)
        }

        binding.logout.setOnClickListener {
            TixngoManager.instance.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}