package io.tixngo.exampleAndroid.ui.fragment

import TixngoProfile
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.tixngo.exampleAndroid.R
import io.tixngo.exampleAndroid.service.AwsCognitoService
import io.tixngo.exampleAndroid.databinding.FragmentLoginBinding
import io.tixngo.exampleAndroid.support.RandomUtility
import io.tixngo.exampleAndroid.support.hideKeyboard
import io.tixngo.sdkutils.TixngoManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setLoading(isShow: Boolean) {
        binding.loadingView.visibility = if (isShow) View.VISIBLE else  View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            val username = binding.usernameField.text.toString()
            val password = binding.passwordField.text.toString()
            hideKeyboard()
            AlertDialog.Builder(requireActivity())
                .setTitle("Note!!!")
                .setMessage("Firstname, lastname, gender and date of birth are randomly because we don't store user-info in AWS Cognito")
                .setNeutralButton("Continue") { _, _ ->
                    setLoading(true)
                    AwsCognitoService.instance.login(username, password) { error ->
                        if (error == null) {
                            // Randomly user information - In real case, please get profile in SSO
                            val firstname = RandomUtility.randomFirstName()
                            val lastname = RandomUtility.randomLastName()
                            val gender = if (RandomUtility.randomBoolean())  TixngoGender.MALE else TixngoGender.FEMALE
                            val dob = RandomUtility.randomDateWithinDaysBeforeToday(365 * 20)
                            val profile = TixngoProfile(firstname, lastname, gender, dob, null, null, null, null, username, null, null, null, null, null)
                            TixngoManager.instance.signIn(profile) { errorMessage ->
                                setLoading(false)
                                if (errorMessage == null) {
                                    requireActivity().finish()
                                } else {
                                    AlertDialog.Builder(requireActivity())
                                        .setTitle("Error")
                                        .setMessage(errorMessage)
                                        .show()
                                }
                            }
                        } else {
                            setLoading(false)
                            AlertDialog.Builder(requireActivity())
                                .setTitle("Error")
                                .setMessage(error.localizedMessage)
                                .show()
                        }
                    }
                }
                .show()
        }

        binding.buttonForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.buttonOpenSdkUi.setOnClickListener {
            val activity = requireActivity()
            TixngoManager.instance.openCurrentPage(activity)
        }

    }

}