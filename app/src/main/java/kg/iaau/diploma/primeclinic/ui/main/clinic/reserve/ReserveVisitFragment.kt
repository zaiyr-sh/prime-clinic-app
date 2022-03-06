package kg.iaau.diploma.primeclinic.ui.main.clinic.reserve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.VISIT_RESERVED_SUCCESSFULLY
import kg.iaau.diploma.core.constants.VISIT_RESERVED_UNSUCCESSFULLY
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentReserveVisitBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM

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
            toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            btnBook.setOnClickListener {
                bookVisit()
            }
        }
    }

    private fun bookVisit() {
        vb.run {
            val phoneNumber = etPhone.text.toString()
            val comment = etComment.text.toString()
            if (phoneNumber.isPhoneNotFieldCorrectly) {
                etPhone.error = getString(R.string.enter_valid_phone_number)
                return
            }
            vm.reserveVisit(phoneNumber, comment)
        }
    }

    private fun observeLiveData() {
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Notification -> notificationAction(event.message)
                is CoreEvent.Error -> errorAction(event)
            }
        })
    }

    private fun notificationAction(message: String?) {
        goneLoader()
        message?.let {
            view?.showSnackBar(requireContext(), message)
            if (it == VISIT_RESERVED_SUCCESSFULLY)
                findNavController().navigate(R.id.nav_payment_method)
        }
    }

    private fun errorAction(event: CoreEvent.Error) {
        when (event.isNetworkError) {
            true -> requireActivity().toast(event.message)
            else -> view?.showSnackBar(requireContext(), VISIT_RESERVED_UNSUCCESSFULLY)
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.run {
            progressBar.show()
            clContainer.setAnimateAlpha(0.5f)
            btnBook.setEnable(false)
        }
    }

    private fun goneLoader() {
        vb.run {
            progressBar.gone()
            clContainer.setAnimateAlpha(1f)
            btnBook.setEnable(true)
        }
    }

}