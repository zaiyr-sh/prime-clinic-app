package kg.iaau.diploma.primeclinic.ui.authorization

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.AUTH_ERROR
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.*
import kg.iaau.diploma.primeclinic.databinding.ActivityAuthorizationBinding
import kg.iaau.diploma.primeclinic.ui.pin.PinActivity
import kg.iaau.diploma.primeclinic.ui.register.RegisterActivity

@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

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
            tvSign.setOnClickListener { startRegisterActivity() }
            btnEnter.setOnClickListener { auth() }
        }
    }

    private fun startRegisterActivity() {
        RegisterActivity.startActivity(this)
        finish()
    }

    private fun auth() {
        val (login, password) = editTextHandler()
        vm.auth(login, password)
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
                is Loading -> showLoader()
                is Success -> successAction()
                is Error -> errorAction()
            }
        })
    }

    private fun successAction() {
        goneLoader()
        PinActivity.startActivity(this)
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
            context.startActivity<AuthorizationActivity>()
        }
    }

}