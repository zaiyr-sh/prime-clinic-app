package kg.iaau.diploma.primeclinic.ui.register

import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreActivity
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.local_storage.prefs.StoragePreferences.Keys.PHONE
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivitySmsCodeBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM

@AndroidEntryPoint
class SmsCodeActivity : CoreActivity<ActivitySmsCodeBinding, AuthorizationVM>(AuthorizationVM::class.java) {

    override val bindingInflater: (LayoutInflater) -> ActivitySmsCodeBinding =
        ActivitySmsCodeBinding::inflate

    private val phone by lazy { intent.getStringExtra(PHONE) }

    override fun setupActivityView() {
        vb.apply {
            vb.btnNext.setEnable(false)
            tvSendCodeTitle.text = getString(R.string.sent_code_to_number, phone)
            etCode.addTextChangedListener { checkSmsCodeFilling(etCode.text.toString()) }
            btnNext.setOnClickListener { vm.verify(etCode.text.toString()) }
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
        vm.verify(code)
    }

    private fun checkSmsCodeFilling(code: String) {
        vb.btnNext.setEnable(code.isNotEmpty())
    }

    override fun successAction() {
        super.successAction()
        vm.savePhoneNumber(phone)
        initFirebaseAuth()
        vb.clContainer.showSnackBar(this, getString(R.string.success_registration_description))
        AuthorizationActivity.startActivity(this)
        finish()
    }

    override fun initFirebaseAuth() {
        super.initFirebaseAuth()
        vm.createNewUserInFirebase(mAuth)
    }

    override fun errorAction(event: CoreEvent.Error) {
        super.errorAction(event)
        if (!event.isNetworkError) toast(getString(R.string.sending_code_error))
    }

    override fun showLoader() {
        super.showLoader()
        vb.clContainer.run {
            setAnimateAlpha(0.5f)
            setEnable(false)
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        vb.clContainer.run {
            setAnimateAlpha(1f)
            setEnable(true)
        }
    }

    companion object {
        fun startActivity(context: Context, phone: String?) {
            context.startActivity<SmsCodeActivity> {
                putExtra(PHONE, phone)
            }
        }
    }

}