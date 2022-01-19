package kg.iaau.diploma.primeclinic.ui.register

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.SEND_CODE_ERROR
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.local_storage.StoragePreferences.Keys.DEVICE_ID
import kg.iaau.diploma.local_storage.StoragePreferences.Keys.PHONE
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivitySmsCodeBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM
import kg.iaau.diploma.primeclinic.ui.pin.PinActivity

@AndroidEntryPoint
class SmsCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmsCodeBinding
    private val vm: AuthorizationVM by viewModels()
    private val phone by lazy { intent.getStringExtra(PHONE) }
    private val deviceId by lazy { intent.getStringExtra(DEVICE_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewForActivity()
        observeLiveData()
    }

    private fun setupViewForActivity() {
        binding.apply {
            tvSendCodeTitle.text = getString(R.string.sent_code_to_number, phone)
            btnNext.setOnClickListener { verifyCode(etCode.text.toString()) }
            tvResend.setOnClickListener { resendCode(etCode.text.toString()) }
        }
    }

    private fun resendCode(code: String) {
        binding.apply {
            tvCodeSendPhone.text = getString(R.string.send_code_duration)
            tvSendCodeTitle.text = getString(R.string.sent_code_to_number_again, phone)
            tvResend.hide()
        }
        toast(getString(R.string.send_code_again))
        verifyCode(code)
    }

    private fun verifyCode(code: String) {
        vm.verify(code)
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

    private fun successAction() {
        goneLoader()
        vm.savePhoneWithDeviceId(phone, deviceId)
        finish()
        PinActivity.startActivity(this)
    }

    private fun errorAction() {
        goneLoader()
        toast(SEND_CODE_ERROR)
    }

    private fun showLoader() {
        binding.progressBar.show()
    }

    private fun goneLoader() {
        binding.progressBar.gone()
    }

    companion object {
        fun startActivity(context: Context, phone: String, deviceId: String) {
            context.startActivity<SmsCodeActivity> {
                putExtra(PHONE, phone)
                putExtra(DEVICE_ID, deviceId)
            }
        }
    }

}