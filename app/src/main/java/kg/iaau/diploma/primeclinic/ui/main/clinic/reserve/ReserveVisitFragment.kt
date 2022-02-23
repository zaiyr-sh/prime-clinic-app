package kg.iaau.diploma.primeclinic.ui.main.clinic.reserve

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentReserveVisitBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet.PaymentMethodBottomSheetFragment

@AndroidEntryPoint
class ReserveVisitFragment : Fragment() {

    private lateinit var vb: FragmentReserveVisitBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentReserveVisitBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.run {
            toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
            btnBook.setOnClickListener {
//                bookVisit()
                PaymentMethodBottomSheetFragment.show(requireActivity().supportFragmentManager)
            }
        }
    }

    private fun bookVisit() {
        vb.run {
            val phoneNumber = etPhone.text.toString()
            val comment = etComment.text.toString()
            if (phoneNumber.isPhoneNotField) {
                etPhone.error = getString(R.string.enter_valid_phone_number)
                return
            }
            vm.reserveVisit(phoneNumber, comment)
        }
    }

    private fun observeLiveData() {
        vm.paymentLiveData.observe(viewLifecycleOwner, { payment ->
            if (payment != null)
                view?.findNavController()?.navigate(R.id.nav_payment)
        })
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
//        PaymentMethodBottomSheetFragment.show(requireActivity().supportFragmentManager)
    }

    private fun errorAction(event: CoreEvent.Error) {
        when (event.isNetworkError) {
            true -> requireActivity().toast(event.message)
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.run {
            progressBar.show()
            clContainer.setAnimateAlpha(0.5f)
        }
    }

    private fun goneLoader() {
        vb.run {
            progressBar.gone()
            clContainer.setAnimateAlpha(1f)
        }
    }

}