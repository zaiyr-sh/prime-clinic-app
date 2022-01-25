package kg.iaau.diploma.primeclinic.ui.register

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.SEND_CODE_ERROR
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.local_storage.prefs.StoragePreferences.Keys.DEVICE_ID
import kg.iaau.diploma.local_storage.prefs.StoragePreferences.Keys.PHONE
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivitySmsCodeBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM
import kg.iaau.diploma.primeclinic.ui.pin.PinActivity

@AndroidEntryPoint
class SmsCodeActivity : AppCompatActivity() {

    private lateinit var vb: ActivitySmsCodeBinding
    private val vm: AuthorizationVM by viewModels()
    private val phone by lazy { intent.getStringExtra(PHONE) }
    private val deviceId by lazy { intent.getStringExtra(DEVICE_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySmsCodeBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setupViewForActivity()
        observeLiveData()
    }

    private fun setupViewForActivity() {
        vb.apply {
            vb.btnNext.setEnable(false)
            tvSendCodeTitle.text = getString(R.string.sent_code_to_number, phone)
            etCode.addTextChangedListener { checkSmsCodeFilling(etCode.text.toString()) }
            btnNext.setOnClickListener { verifyCode(etCode.text.toString()) }
            tvResend.setOnClickListener { resendCode(etCode.text.toString()) }
        }
    }

    private fun resendCode(code: String) {
        vb.apply {
            etCode.text.clear()
            tvCodeSendPhone.text = getString(R.string.send_code_duration)
            tvSendCodeTitle.text = getString(R.string.sent_code_to_number_again, phone)
            tvResend.hide()
        }
        verifyCode(code)
    }

    private fun verifyCode(code: String) {
        vm.verify(code)
    }

    private fun checkSmsCodeFilling(code: String) {
        vb.btnNext.setEnable(code.isNotEmpty() && code.length == CODE_LENGTH)
    }

    private fun observeLiveData() {
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> successAction()
                is CoreEvent.Error -> errorAction(event)
            }
        })
    }

    private fun successAction() {
        goneLoader()
        vm.savePhoneWithDeviceId(phone, deviceId)
        finish()
        PinActivity.startActivity(this)
    }

    private fun errorAction(event: CoreEvent.Error) {
        when (event.isNetworkError) {
            true -> toast(event.message)
            false -> toast(SEND_CODE_ERROR)
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
        const val CODE_LENGTH = 6

        fun startActivity(context: Context, phone: String?, deviceId: String?) {
            context.startActivity<SmsCodeActivity> {
                putExtra(PHONE, phone)
                putExtra(DEVICE_ID, deviceId)
            }
        }
    }

}