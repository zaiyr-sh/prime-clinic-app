package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentCalendarBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.CalendarAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.CalendarListener

class CalendarBottomSheetFragment : BottomSheetDialogFragment(), CalendarListener {

    private lateinit var vb: FragmentCalendarBottomSheetBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val adapter = CalendarAdapter(this)
    private val id by lazy { Bundle().getLong(DOCTOR_ID) }

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
            rvTime.adapter = adapter
            rvTime.layoutManager = GridLayoutManager(requireContext(), 4)
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener {  }
        }
    }

    private fun observeLiveData() {
        vm.doctorSchedule.observe(viewLifecycleOwner, { schedule ->
            setupSchedule(schedule)
        })
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Error -> errorAction(event)
            }
        })
    }

    private fun setupSchedule(schedule: List<Interval>?) {
        vb.run {
            if(schedule.isNullOrEmpty()) {
                rvTime.show()
                ivEmpty.hide()
                adapter.submitList(schedule)
            } else {
                rvTime.hide()
                ivEmpty.show()
            }
        }
    }

    private fun errorAction(event: CoreEvent.Error) {
        when (event.isNetworkError) {
            true -> requireActivity().toast(event.message)
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.progressBar.show()
    }

    private fun goneLoader() {
        vb.progressBar.gone()
    }

    override fun onIntervalClick(interval: Interval) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DOCTOR_ID = "ID"
        private val bottomSheet = CalendarBottomSheetFragment()
        fun show(supportFragmentManager: FragmentManager, id: Long) {
            bottomSheet.show(supportFragmentManager, bottomSheet.tag).apply {
                Bundle().apply {
                    putLong(DOCTOR_ID, id)
                }
            }
        }
    }

}