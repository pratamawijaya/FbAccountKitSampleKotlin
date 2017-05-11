package com.pratamawijaya.facebookaccountkitdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.accountkit.AccountKitLoginResult
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        val RC_PHONE_NUMBER = 1
        val RC_FACEBOOK_ACCOUNT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLoginFacebook.setOnClickListener {
            loginFacebook()
        }

        btnLoginPhonenumber.setOnClickListener {
            loginPhoneNumber()
        }
    }


    private fun loginPhoneNumber() {
        val intent = Intent(this, AccountKitActivity::class.java)
        val configBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN)

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configBuilder.build())
        startActivityForResult(intent, RC_PHONE_NUMBER)
    }

    private fun loginFacebook() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_PHONE_NUMBER -> {
                Log.d("debug", "user request phone number")

                val loginResult = data?.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)

                var toastMsg: String?

                if (loginResult != null) {
                    Log.d("debug", "login result $loginResult")
                    toastMsg = handleLoginResult(loginResult)
                } else {
                    toastMsg = "login gagal"
                }

                showMessage(toastMsg)

            }
            RC_FACEBOOK_ACCOUNT -> {

            }
        }
    }

    private fun handleLoginResult(loginResult: AccountKitLoginResult): String? {
        var msg: String? = null

        if (loginResult.error != null) {
            Log.e("debug", "login error")
            msg = loginResult?.error?.errorType?.message
        } else if (loginResult.wasCancelled()) {
            Log.d("debug", "login cancelled")
            msg = "login cancel"
        } else {
            if (loginResult.accessToken != null) {
                val accessToken = loginResult.accessToken
                val accountId = accessToken?.accountId
                if (accountId != null) {
                    msg = "success $accountId"
                    Log.d("debug", "acces token ${accessToken.token}")
                } else {
                    Log.e("debug", "account id null")
                }
            } else {
                Log.e("debug", "access token null")
            }
        }
        return msg
    }

    private fun showMessage(msg: String?) {
        if (msg != null) {
            Log.d("debug", "message $msg")
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
