package io.tixngo.exampleAndroid.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.tixngo.exampleAndroid.R
import io.tixngo.exampleAndroid.service.AwsCognitoService
import io.tixngo.exampleAndroid.databinding.FragmentConfirmForgotPasswordBinding
import io.tixngo.exampleAndroid.support.hideKeyboard

class ConfirmForgotPasswordFragment : Fragment() {

    private lateinit var username: String

    private var _binding: FragmentConfirmForgotPasswordBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString("username")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setLoading(isShow: Boolean) {
        binding.loadingView.visibility = if (isShow) View.VISIBLE else  View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSend.setOnClickListener {
            val code = binding.codeField.text.toString()
            val password = binding.passwordField.text.toString()
            hideKeyboard()
            setLoading(true)
            AwsCognitoService.instance.confirmForgotPassword(username, code, password) { error ->
                setLoading(false)
                if (error == null) {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Successful")
                        .setMessage("Please sign in")
                        .setNeutralButton("OK") { _, _ ->
                            findNavController().navigate(R.id.action_confirmForgotPasswordFragment_to_loginFragment)
                        }
                        .show()
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