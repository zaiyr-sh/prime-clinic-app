package kg.iaau.diploma.primeclinic.ui.main.clinic.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.convertBase64ToDrawable
import kg.iaau.diploma.data.Payment
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentPaymentBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.PaymentStepAdapter

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var vb: FragmentPaymentBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val adapter = PaymentStepAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentPaymentBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setupFragmentView()
    }

    private fun setupFragmentView() {
        vb.run {
            toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            rvSteps.adapter = adapter
        }
    }

    private fun observeLiveData() {
        vm.paymentLiveData.observe(viewLifecycleOwner, { payment ->
            payment?.let {
                setupPaymentSteps(payment)
            }
        })
    }

    private fun setupPaymentSteps(payment: Payment) {
        vb.run {
            ivLogo.setImageDrawable(payment.logo?.convertBase64ToDrawable(requireActivity()))
            adapter.submitList(payment.paymentSteps)
        }
    }

}