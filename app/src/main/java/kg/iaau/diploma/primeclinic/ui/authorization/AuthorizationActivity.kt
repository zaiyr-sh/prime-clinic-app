package kg.iaau.diploma.primeclinic.ui.authorization

import android.content.Context
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreActivity
import kg.iaau.diploma.core.utils.convertPhoneNumberWithCode
import kg.iaau.diploma.core.utils.setAnimateAlpha
import kg.iaau.diploma.core.utils.setEnable
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.primeclinic.databinding.ActivityAuthorizationBinding
import kg.iaau.diploma.primeclinic.ui.pin.PinActivity
import kg.iaau.diploma.primeclinic.ui.register.RegisterActivity

@AndroidEntryPoint
class AuthorizationActivity :
    CoreActivity<ActivityAuthorizationBinding, AuthorizationVM>(AuthorizationVM::class.java) {

    override val inflater: (LayoutInflater) -> ActivityAuthorizationBinding =
        ActivityAuthorizationBinding::inflate

    override fun setupActivityView() {
        vb.apply {
            ccp.registerCarrierNumberEditText(etPhone)
            btnEnter.setEnable(false)
            etPhone.addTextChangedListener { checkEditTextFilling() }
            etPassword.addTextChangedListener { checkEditTextFilling() }
            tvSign.setOnClickListener { startRegisterActivity() }
            btnEnter.setOnClickListener { auth() }
        }
    }

    private fun checkEditTextFilling() {
        val (login, password) = editTextHandler()
        vb.btnEnter.setEnable(login.isNotEmpty() && password.isNotEmpty())
    }

    private fun startRegisterActivity() {
        RegisterActivity.startActivity(this)
        finish()
    }

    private fun auth() {
        val (login, password) = editTextHandler()
        vm.auth(login.convertPhoneNumberWithCode(vb.ccp.selectedCountryCode), password)
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
        PinActivity.startActivity(this)
        finish()
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
            context.startActivity<AuthorizationActivity>()
        }
    }

}