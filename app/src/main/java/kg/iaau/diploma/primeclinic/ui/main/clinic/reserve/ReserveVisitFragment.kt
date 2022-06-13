package kg.iaau.diploma.primeclinic.ui.main.clinic.reserve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
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

    private var originalMode : Int? = null
    private val args: ReserveVisitFragmentArgs by navArgs()
    private val schedule: Interval by lazy { args.schedule }
    private val slot: Slot by lazy { args.slot }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        originalMode = activity?.window?.attributes?.softInputMode
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        )
    }

    override fun setupFragmentView() {
        vm.getPaymentMethods()
        vm.setDate(schedule)
        vm.setSlot(slot)
        vb.run {
            btnBook.setEnable(false)
            etPhone.addTextChangedListener { validatePhoneNumber() }
            toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
            ccp.registerCarrierNumberEditText(etPhone)
            btnBook.setOnClickListener { reserveVisit() }
        }
    }

    private fun validatePhoneNumber() {
        val phoneNumber = filterFields()[0]
        vb.btnBook.setEnable(!phoneNumber.isPhoneNotFieldCorrectly)
    }

    private fun reserveVisit() {
        val (phoneNumber, comment) = filterFields()
        vm.reserveVisit(phoneNumber, comment)
    }

    private fun filterFields(): Array<String> {
        vb.run {
            val phoneNumber = etPhone.text.toString()
                .filterNot { it.isWhitespace() }
                .convertPhoneNumberWithCode(vb.ccp.selectedCountryCode)
            val comment = etComment.text.toString()
            return arrayOf(phoneNumber, comment)
        }
    }

    override fun notificationAction(event: CoreEvent.Notification) {
        goneLoader()
        event.title?.let {
            view?.showSnackBar(requireContext(), getString(it))
            if (it == R.string.reservation_visit_successfully)
                navigateToPaymentMethod()
        }
    }

    private fun navigateToPaymentMethod() {
        findNavController().navigate(
            R.id.nav_payment_method,
            Bundle().apply {
                putParcelableArray("paymentMethods", vm.paymentMethodsLiveData.value?.toTypedArray())
            }
        )
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
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        vb.run {
            clContainer.setAnimateAlpha(1f)
        }
    }

    override fun onPause() {
        super.onPause()
        originalMode?.let { activity?.window?.setSoftInputMode(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        originalMode?.let { activity?.window?.setSoftInputMode(it) }
    }

}