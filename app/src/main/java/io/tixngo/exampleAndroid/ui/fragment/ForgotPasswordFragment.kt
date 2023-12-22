package io.tixngo.exampleAndroid.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.tixngo.exampleAndroid.R
import io.tixngo.exampleAndroid.service.AwsCognitoService
import io.tixngo.exampleAndroid.databinding.FragmentForgotPasswordBinding
import io.tixngo.exampleAndroid.support.hideKeyboard

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setLoading(isShow: Boolean) {
        binding.loadingView.visibility = if (isShow) View.VISIBLE else  View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSend.setOnClickListener {
            val username = binding.usernameField.text.toString()
            hideKeyboard()
            setLoading(true)
            AwsCognitoService.instance.forgotPassword(username) { error, _ ->
                setLoading(false)
                if (error == null) {
                    val bundle = Bundle()
                    bundle.putString("username", username)
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_confirmForgotPasswordFragment, bundle)
                } else {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Error")
                        .setMessage(error.localizedMessage)
                        .show()
                }
            }
        }
    }
}