package io.tixngo.exampleAndroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.tixngo.exampleAndroid.R
import io.tixngo.sdkutils.TixngoManager
import io.tixngo.exampleAndroid.databinding.FragmentMainBinding
import io.tixngo.exampleAndroid.databinding.FragmentMatchesBinding
import io.tixngo.exampleAndroid.ui.activity.LoginActivity


/**
 * A simple [Fragment] subclass.
 * Use the [MatchesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatchesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentMatchesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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