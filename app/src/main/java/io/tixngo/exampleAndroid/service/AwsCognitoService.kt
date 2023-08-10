package io.tixngo.exampleAndroid.service

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentityprovider.model.CodeDeliveryDetailsType
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import java.lang.Exception


class AwsCognitoService(private val applicationContext: Context, private val regions: Regions, private val clientId: String, private val poolId: String) {

    private val userPool: CognitoUserPool;
    companion object {
        lateinit var instance: AwsCognitoService
            private set
    }

    init {
        instance = this
        userPool = CognitoUserPool(applicationContext, poolId, clientId, null, regions)
    }

    fun login(username: String, password: String, completion: (Exception?) -> Unit) {
        userPool.user.initiateUserAuthentication(AuthenticationDetails(username, password, null), object: AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                completion(null)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                completion(Exception(authenticationContinuation?.toString() ?: "Unknown Exception on AuthenticationContinuation"))
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                completion(Exception(continuation?.toString() ?: "Unknown Exception on MultiFactorAuthenticationContinuation"))
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                completion(Exception(continuation?.toString() ?: "Unknown Exception on ChallengeContinuation"))
            }

            override fun onFailure(exception: Exception?) {
                completion(exception ?: Exception("Unknown Exception on Failure"))
            }

        }, true).run()


    }

    fun signup(username: String, password: String, completion: (Exception?, CodeDeliveryDetailsType?) -> Unit) {
        userPool.signUpInBackground(username, password, CognitoUserAttributes(), mapOf(), object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                completion(null, signUpResult?.codeDeliveryDetails)
            }

            override fun onFailure(exception: Exception?) {
                completion(exception ?: Exception("Unknown Exception"), null)
            }

        })
    }

    fun confirmSignup(username: String, code: String, completion: (Exception?) -> Unit) {
        userPool.getUser(username).confirmSignUpInBackground(code, true, mapOf(), object : GenericHandler {
            override fun onSuccess() {
                completion(null)
            }

            override fun onFailure(exception: Exception?) {
                completion(exception ?: Exception("Unknown Exception"))
            }

        })
    }

    fun forgotPassword(username: String, completion: (Exception?, CognitoUserCodeDeliveryDetails?) -> Unit) {
        userPool.getUser(username).forgotPasswordInBackground( object : ForgotPasswordHandler {
            override fun onSuccess() {
                completion(null, null)
            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                completion(null, continuation?.parameters)
            }

            override fun onFailure(exception: Exception?) {
                completion(exception ?: Exception("Unknown Exception"), null)
            }

        })
    }

    fun confirmForgotPassword(username: String, code: String, password: String, completion: (Exception?) -> Unit) {
        userPool.getUser(username).confirmPasswordInBackground(code, password, object : ForgotPasswordHandler {
            override fun onSuccess() {
                completion(null)
            }

            override fun getResetCode(continuation: ForgotPasswordContinuation?) {
                completion(null)
            }

            override fun onFailure(exception: Exception?) {
                completion(exception ?: Exception("Unknown Exception"))
            }

        })
    }

    fun getToken(completion: (Exception?, String?) -> Unit) {
        userPool.currentUser.getSessionInBackground( object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                completion(null, userSession!!.idToken.jwtToken)
            }

            override fun getAuthenticationDetails(
                authenticationContinuation: AuthenticationContinuation?,
                userId: String?
            ) {
                completion(Exception(authenticationContinuation?.toString() ?: "Unknown Exception on AuthenticationContinuation"), null)
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                completion(Exception(continuation?.toString() ?: "Unknown Exception on MultiFactorAuthenticationContinuation"), null)
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                completion(Exception(continuation?.toString() ?: "Unknown Exception on ChallengeContinuation"), null)
            }

            override fun onFailure(exception: Exception?) {
                completion(exception ?: Exception("Unknown Exception on Failure"), null)
            }

        })
    }


}