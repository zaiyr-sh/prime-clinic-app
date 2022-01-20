package kg.iaau.diploma.primeclinic.ui.register

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.AUTH_ERROR
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.*
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityAuthorizationBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var vb: ActivityAuthorizationBinding
    private val vm: AuthorizationVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setupActivityView()
        observeLiveData()
    }

    private fun setupActivityView() {
        vb.apply {
            tvCodeSendPhone.show()
            tvEnter.text = getString(R.string.app_register)
            tvSign.text = getString(R.string.action_sign_in)
            btnEnter.text = getString(R.string.action_register)
            btnEnter.setEnable(false)
            etPhone.addTextChangedListener { checkEditTextFilling() }
            etPassword.addTextChangedListener { checkEditTextFilling() }
            btnEnter.setOnClickListener { register() }
            tvSign.setOnClickListener{ startAuthorizationActivity() }
        }
    }

    private fun checkEditTextFilling() {
        val (login, password) = editTextHandler()
        vb.btnEnter.setEnable(login.isNotEmpty() && password.isNotEmpty())
    }

    private fun startAuthorizationActivity() {
        AuthorizationActivity.startActivity(this)
        finish()
    }

    private fun register() {
        val (login, password) = editTextHandler()
        vm.register(login.convertPhoneNumberTo(vb.ccp.selectedCountryCode), password)
    }

    private fun editTextHandler(): Array<String> {
        vb.apply {
            val login = etPhone.text.trim().toString()
            val password = etPassword.text.trim().toString()
            return arrayOf(login, password)
        }
    }

    private fun observeLiveData() {
        vm.event.observe(this, { event ->
            when(event) {
                is Loading -> showLoader()
                is Success -> successAction()
                is Error -> errorAction(event)
            }
        })
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun successAction() {
        goneLoader()
        val phone = editTextHandler()[0]
        SmsCodeActivity.startActivity(this, phone, getDeviceId(this))
    }

    private fun errorAction(event: Error) {
        when (event.isNetworkError) {
            true -> toast(event.message)
            false -> toast(AUTH_ERROR)
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.progressBar.show()
    }

    private fun goneLoader() {
        vb.progressBar.gone()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity<RegisterActivity>()
        }
    }

}