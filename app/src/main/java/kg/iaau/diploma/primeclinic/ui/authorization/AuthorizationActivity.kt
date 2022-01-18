package kg.iaau.diploma.primeclinic.ui.authorization

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.CoreEvent.Success
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.databinding.ActivityAuthorizationBinding

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
        binding.btnEnter.setOnClickListener { auth() }
    }

    private fun auth() {
        val (login, password) = editTextHandler()
        vm.auth(login, password)
    }

    private fun editTextHandler(): Array<String> {
        val login = "+${binding.ccp.selectedCountryCode}${binding.etPhone.text.trim()}"
        val password = binding.etPassword.text.trim().toString()
        return arrayOf(login, password)
    }

    private fun observeLiveData() {
        vm.event.observe(this, { event ->
            when(event) {
                is Success -> {
//                    startActivity<MainActivity>()
                }
            }
        })
    }
}