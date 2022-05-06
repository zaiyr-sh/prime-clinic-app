package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.BaseBottomSheetFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Payment
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentPaymentMethodBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.PaymentAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.PaymentListener

@AndroidEntryPoint
class PaymentMethodBottomSheetFragment : BaseBottomSheetFragment<FragmentPaymentMethodBottomSheetBinding>(), PaymentListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentMethodBottomSheetBinding
        get() = FragmentPaymentMethodBottomSheetBinding::inflate

    private val adapter = PaymentAdapter(this)
    private val args: PaymentMethodBottomSheetFragmentArgs by navArgs()
    private val paymentMethods: Array<Payment> by lazy { args.paymentMethods }

    override fun setupFragmentView() {
        vb.rvPaymentMethod.adapter = adapter
        setupPaymentMethods(paymentMethods.toList())
    }

    private fun setupPaymentMethods(paymentMethods: List<Payment>?) {
        when(paymentMethods.isNullOrEmpty()) {
            true -> hidePaymentMethods()
            else -> showPaymentMethods(paymentMethods)
        }
    }

    private fun hidePaymentMethods() {
        vb.run {
            rvPaymentMethod.hide()
            ivEmpty.show()
        }
    }

    private fun showPaymentMethods(paymentMethods: List<Payment>) {
        vb.run {
            rvPaymentMethod.show()
            ivEmpty.hide()
            adapter.submitList(paymentMethods)
        }
    }

    override fun onPaymentClick(payment: Payment) {
        findNavController().navigate(
            R.id.nav_payment,
            Bundle().apply {
                putParcelable("payment", payment)
            }
        )
    }

}