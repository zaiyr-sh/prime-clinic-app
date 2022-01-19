package kg.iaau.diploma.primeclinic.ui.pin

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.hide
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.core.utils.startActivity
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.ActivityPinBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationVM

@AndroidEntryPoint
class PinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinBinding
    private val vm: AuthorizationVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActivityView()
    }

    private fun setupActivityView() {
        if (vm.isFirstTimePinCreating) setupTvPin(R.string.pin_creation)
        else if (vm.isUserSignIn()) setupTvPin(R.string.pin_enter)
        binding.apply {
            btnZero.setOnClickListener { onEnterPin(0) }
            btnOne.setOnClickListener { onEnterPin(1) }
            btnTwo.setOnClickListener { onEnterPin(2) }
            btnThree.setOnClickListener { onEnterPin(3) }
            btnFour.setOnClickListener { onEnterPin(4) }
            btnFive.setOnClickListener { onEnterPin(5) }
            btnSix.setOnClickListener { onEnterPin(6) }
            btnSeven.setOnClickListener { onEnterPin(7) }
            btnEight.setOnClickListener { onEnterPin(8) }
            btnNine.setOnClickListener { onEnterPin(9) }
            ibDeleteNumber.setOnClickListener { deleteLastDigit() }
            tvRestorePin.setOnClickListener { restorePin() }
        }
    }

    private fun setupTvPin(@StringRes stringRes: Int) {
        binding.apply {
            tvPin.text = getString(stringRes)
            tvRestorePin.show()
        }
    }

    private fun onEnterPin(digit: Int) {
        binding.apply {
            llIndicatorDots[vm.dotPosition].setBackgroundResource(R.drawable.shape_filled_dot)
            vm.fillPin(digit)
            ibDeleteNumber.show()
            tvWrongPin.hide()
            checkIsPinCompletelyFilled()
        }
    }

    private fun checkIsPinCompletelyFilled() {
        binding.apply {
            if(vm.dotPosition == 4) {
                llIndicatorDots.hide()
                when {
                    vm.isFirstTimePinCreating -> startCurrentActivity()
                    vm.isPinVerified() -> startMainActivity()
                    else -> wrongPin()
                }
            }
        }
    }

    private fun startCurrentActivity() {
        vm.savePin()
        startActivity(intent)
        finish()
        return
    }

    private fun deleteLastDigit() {
        vm.deleteLastDigit()
        binding.apply {
            llIndicatorDots[vm.dotPosition].setBackgroundResource(R.drawable.shape_empty_dot)
            checkIsPinEmpty()
        }
    }

    private fun checkIsPinEmpty() {
        if(vm.dotPosition == 0) binding.ibDeleteNumber.hide()
    }

    private fun wrongPin() {
        vm.clearPin()
        binding.apply {
            tvWrongPin.show()
            tlKeypad.show()
            ibDeleteNumber.visibility = View.INVISIBLE
            setDotsBackgroundRes(R.drawable.shape_empty_dot)
        }
    }

    private fun setDotsBackgroundRes(res_id: Int) {
        for (position in 0..3) {
            binding.llIndicatorDots[position].setBackgroundResource(res_id)
        }
    }

    private fun startMainActivity() {
        startActivity<MainActivity>()
        finish()
    }

    private fun restorePin() {
        vm.restorePin()
        startActivity<AuthorizationActivity>()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity<PinActivity>()
        }
    }

}