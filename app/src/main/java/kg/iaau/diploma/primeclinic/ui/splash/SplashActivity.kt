package kg.iaau.diploma.primeclinic.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.primeclinic.databinding.ActivitySplashBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAuthorizationActivityWithDelay()
    }

    private fun startAuthorizationActivityWithDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity<AuthorizationActivity>()
            finish()
        }, 2000)
    }
}