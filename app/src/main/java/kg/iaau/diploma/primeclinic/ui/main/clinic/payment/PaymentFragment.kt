package kg.iaau.diploma.primeclinic.ui.main.clinic.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.BaseFragment
import kg.iaau.diploma.core.utils.loadBase64Image
import kg.iaau.diploma.data.Payment
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentPaymentBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.PaymentStepAdapter

@AndroidEntryPoint
class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPaymentBinding
        get() = FragmentPaymentBinding::inflate

    private val adapter = PaymentStepAdapter()
    private val args: PaymentFragmentArgs by navArgs()
    private val payment: Payment by lazy { args.payment }

    override fun setupFragmentView() {
        vb.toolbar.setNavigationOnClickListener {
            parentFragmentManager.clearBackStack("")
        }
        setupPaymentSteps(payment)
    }

    private fun setupPaymentSteps(payment: Payment) {
        vb.run {
            rvSteps.adapter = adapter
            ivLogo.loadBase64Image(requireContext(), payment.logo, R.drawable.shape_filled_dot)
            adapter.submitList(payment.paymentSteps)
        }
    }

}