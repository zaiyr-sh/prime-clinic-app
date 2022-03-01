package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.DATE_NOT_SELECTED
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentCalendarBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DateAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DateListener

@AndroidEntryPoint
class DateBottomSheetFragment : BottomSheetDialogFragment(), DateListener {

    private lateinit var vb: FragmentCalendarBottomSheetBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val adapter = DateAdapter(this)
    private val args: DateBottomSheetFragmentArgs by navArgs()
    private val id: Long by lazy { args.id }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        vb = FragmentCalendarBottomSheetBinding.inflate(inflater, container, false)
        vm.getScheduleByDoctorId(id)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetView()
        observeLiveData()
    }

    private fun setupBottomSheetView() {
        vb.run {
            tvHeader.text = getString(R.string.choose_date)
            rvTime.adapter = adapter
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener { checkChoosingDate() }
        }
    }

    private fun checkChoosingDate() {
        requireActivity().apply {
            if (vm.scheduleDate != null) {
                findNavController().navigate(R.id.nav_time)
            } else {
                toast(DATE_NOT_SELECTED)
            }
        }
    }

    private fun observeLiveData() {
        vm.doctorScheduleLiveData.observe(viewLifecycleOwner, { schedule ->
            setupDate(schedule)
        })
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Error -> errorAction(event)
            }
        })
    }

    private fun setupDate(schedule: List<Interval>?) {
        vb.run {
            if(schedule.isNullOrEmpty()) {
                rvTime.hide()
                ivEmpty.show()
            } else {
                rvTime.show()
                ivEmpty.hide()
                adapter.submitList(schedule)
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

    override fun onDateClick(interval: Interval?) {
        vm.setDate(interval)
    }

}