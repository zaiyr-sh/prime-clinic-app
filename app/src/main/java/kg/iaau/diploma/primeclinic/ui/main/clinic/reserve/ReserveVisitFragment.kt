package kg.iaau.diploma.primeclinic.ui.main.clinic.reserve

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.data.Slot
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentReserveVisitBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM

@AndroidEntryPoint
class ReserveVisitFragment : CoreFragment<FragmentReserveVisitBinding, ClinicVM>(ClinicVM::class.java) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReserveVisitBinding
        get() = FragmentReserveVisitBinding::inflate

    private val args: ReserveVisitFragmentArgs by navArgs()
    private val schedule: Interval by lazy { args.schedule }
    private val slot: Slot by lazy { args.slot }

    override fun setupFragmentView() {
        vm.setDate(schedule)
        vm.setSlot(slot)
        vb.run {
            toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            ccp.registerCarrierNumberEditText(etPhone)
            btnBook.setOnClickListener {
                bookVisit()
            }
        }
    }

    private fun bookVisit() {
        vb.run {
            val phoneNumber = etPhone.text.toString()
                .filterNot { it.isWhitespace() }
                .convertPhoneNumberWithCode(vb.ccp.selectedCountryCode)
            if (phoneNumber.isPhoneNotFieldCorrectly) {
                etPhone.error = getString(R.string.enter_valid_phone_number)
                return
            }
            val comment = etComment.text.toString()
            vm.reserveVisit(phoneNumber, comment)
        }
    }

    override fun notificationAction(event: CoreEvent.Notification) {
        goneLoader()
        event.title?.let {
            view?.showSnackBar(requireContext(), getString(it))
            if (it == R.string.reservation_visit_successfully) {
                vm.setSlot(null)
                vm.setDate(null)
                findNavController().navigate(R.id.nav_payment_method)
            }
        }
    }

    override fun errorAction(event: CoreEvent.Error) {
        super.errorAction(event)
        if (!event.isNetworkError)
            view?.showSnackBar(requireContext(), getString(R.string.reservation_visit_unsuccessfully))
    }

    override fun showLoader() {
        super.showLoader()
        vb.run {
            clContainer.setAnimateAlpha(0.5f)
            btnBook.setEnable(false)
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        vb.run {
            clContainer.setAnimateAlpha(1f)
            btnBook.setEnable(true)
        }
    }

}