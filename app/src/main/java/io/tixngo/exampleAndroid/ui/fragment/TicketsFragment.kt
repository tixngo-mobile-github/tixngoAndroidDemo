package io.tixngo.exampleAndroid.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.tixngo.exampleAndroid.R
import io.tixngo.sdkutils.TixngoManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TicketsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tickets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val frg = TixngoManager.instance.homePageFragment()
        val ft = parentFragmentManager.beginTransaction()
        ft.replace(R.id.ticket_content_frame, frg)
        ft.commit()
    }
}