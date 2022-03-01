package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Payment
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentPaymentMethodBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.PaymentAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.PaymentListener

@AndroidEntryPoint
class PaymentMethodBottomSheetFragment : BottomSheetDialogFragment(), PaymentListener {

    private lateinit var vb: FragmentPaymentMethodBottomSheetBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val adapter = PaymentAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        setHasOptionsMenu(false)
        vm.getPaymentMethods()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        vb = FragmentPaymentMethodBottomSheetBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetView()
        observeLiveData()
    }

    private fun setupBottomSheetView() {
        vb.rvPaymentMethod.adapter = adapter
    }

    private fun observeLiveData() {
        vm.paymentMethodsLiveData.observe(viewLifecycleOwner, { paymentMethods ->
            setupPaymentMethods(paymentMethods)
        })
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Error -> errorAction(event)
            }
        })
    }

    private fun setupPaymentMethods(paymentMethods: List<Payment>?) {
        vb.run {
            if(paymentMethods.isNullOrEmpty()) {
                rvPaymentMethod.hide()
                ivEmpty.show()
            } else {
                rvPaymentMethod.show()
                ivEmpty.hide()
                adapter.submitList(paymentMethods)
            }
        }
    }

    private fun errorAction(event: CoreEvent.Error) {
        when (event.isNetworkError) {
            true -> requireActivity().toast(event.message)
            else -> requireActivity().toast(getString(R.string.unexpected_error))
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.progressBar.show()
    }

    private fun goneLoader() {
        vb.progressBar.gone()
    }

    override fun onPaymentClick(payment: Payment) {
        vm.setPaymentMethod(payment)
        findNavController().navigate(R.id.nav_payment)
        dismiss()
    }

}