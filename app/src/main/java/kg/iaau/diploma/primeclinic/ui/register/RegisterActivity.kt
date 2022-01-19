package kg.iaau.diploma.primeclinic.ui.register

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.AUTH_ERROR
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityAuthorizationBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private val vm: AuthorizationVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActivityView()
        observeLiveData()
    }

    private fun setupActivityView() {
        binding.apply {
            tvCodeSendPhone.show()
            tvEnter.text = getString(R.string.app_register)
            tvSign.text = getString(R.string.action_sign_in)
            btnEnter.text = getString(R.string.action_register)
            btnEnter.setOnClickListener { register() }
            tvSign.setOnClickListener{ startAuthorizationActivity() }
        }
    }

    private fun startAuthorizationActivity() {
        AuthorizationActivity.startActivity(this)
        finish()
    }

    private fun register() {
        val (login, password) = editTextHandler()
        vm.register(login, password)
    }

    private fun editTextHandler(): Array<String> {
        binding.apply {
            val login = etPhone.text.trim().toString().convertPhoneNumberTo(ccp.selectedCountryCode)
            val password = etPassword.text.trim().toString()
            return arrayOf(login, password)
        }
    }

    private fun observeLiveData() {
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> successAction()
                is CoreEvent.Error -> errorAction()
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

    private fun errorAction() {
        goneLoader()
        toast(AUTH_ERROR)
    }

    private fun showLoader() {
        binding.progressBar.show()
    }

    private fun goneLoader() {
        binding.progressBar.gone()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity<RegisterActivity>()
        }
    }

}