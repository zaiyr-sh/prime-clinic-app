package kg.iaau.diploma.primeclinic.ui.register

import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreActivity
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityAuthorizationBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM

@AndroidEntryPoint
class RegisterActivity :
    CoreActivity<ActivityAuthorizationBinding, AuthorizationVM>(AuthorizationVM::class.java)  {

    override val bindingInflater: (LayoutInflater) -> ActivityAuthorizationBinding =
        ActivityAuthorizationBinding::inflate

    override fun setupActivityView() {
        vb.apply {
            ccp.registerCarrierNumberEditText(etPhone)
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
        vm.register(login.convertPhoneNumberWithCode(vb.ccp.selectedCountryCode), password)
    }

    private fun editTextHandler(): Array<String> {
        vb.apply {
            val login = etPhone.text.toString().filterNot { it.isWhitespace() }
            val password = etPassword.text.toString().filterNot { it.isWhitespace() }
            return arrayOf(login, password)
        }
    }

    override fun successAction() {
        super.successAction()
        val phone = editTextHandler()[0]
        SmsCodeActivity.startActivity(this, phone)
    }

    override fun errorAction(event: CoreEvent.Error) {
        super.errorAction(event)
        if (!event.isNetworkError) toast(getString(R.string.auth_error))
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
        fun startActivity(context: Context) {
            context.startActivity<RegisterActivity>()
        }
    }

}