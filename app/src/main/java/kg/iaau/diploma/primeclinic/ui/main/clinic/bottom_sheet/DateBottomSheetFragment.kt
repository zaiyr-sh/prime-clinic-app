package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreBottomSheetFragment
import kg.iaau.diploma.core.utils.formatForCurrentDate
import kg.iaau.diploma.core.utils.hide
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentCalendarBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DateAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DateListener

@AndroidEntryPoint
class DateBottomSheetFragment : CoreBottomSheetFragment<FragmentCalendarBottomSheetBinding, ClinicVM>(ClinicVM::class.java), DateListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalendarBottomSheetBinding
        get() = FragmentCalendarBottomSheetBinding::inflate

    private val adapter = DateAdapter(this)
    private val args: DateBottomSheetFragmentArgs by navArgs()
    private val schedule: Array<Interval> by lazy { args.schedule }

    override fun setupFragmentView() {
        vb.run {
            tvHeader.text = getString(R.string.choose_date)
            rvTime.adapter = adapter
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener { checkChoosingDate() }
        }
        setupDate(schedule)
    }

    private fun checkChoosingDate() {
        requireActivity().apply {
            when(vm.scheduleDate) {
                null -> toast(getString(R.string.date_not_selected))
                else -> navigateToChoosingTime(vm.scheduleDate)
            }
        }
    }

    private fun navigateToChoosingTime(scheduleDate: Interval?) {
        findNavController().navigate(
            R.id.nav_time,
            Bundle().apply {
                putParcelable("schedule", scheduleDate)
                putParcelableArray("date", scheduleDate?.reservation?.filter { it.id == null }?.toTypedArray())
            }
        )
    }

    private fun setupDate(schedule: Array<Interval>?) {
        when(schedule.isNullOrEmpty()) {
            true -> hideTime()
            else -> showTime(schedule)
        }
    }

    private fun hideTime() {
        vb.run {
            rvTime.hide()
            ivEmpty.show()
        }
    }

    private fun showTime(schedule: Array<Interval>) {
       vb.run {
           rvTime.show()
           ivEmpty.hide()
           adapter.submitList(schedule.distinctBy { it.start?.formatForCurrentDate() })
       }
    }

    override fun onDateClick(interval: Interval?): Boolean {
        if (interval != null && vm.scheduleDate != null) return false
        else vm.setDate(interval)
        return true
    }

}