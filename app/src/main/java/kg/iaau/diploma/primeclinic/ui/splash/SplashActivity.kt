package kg.iaau.diploma.primeclinic.ui.splash

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreActivity
import kg.iaau.diploma.primeclinic.BuildConfig
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivitySplashBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM
import kg.iaau.diploma.primeclinic.ui.pin.PinActivity

@AndroidEntryPoint
class SplashActivity : CoreActivity<ActivitySplashBinding, AuthorizationVM>(AuthorizationVM::class.java) {

    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() = ActivitySplashBinding::inflate

    override fun setupActivityView() {
        vb.appVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
        Handler(Looper.getMainLooper()).postDelayed({
            if (vm.isUserSignIn())
                PinActivity.startActivity(this)
            else
                AuthorizationActivity.startActivity(this)
            finish()
        }, 1000)
    }

}