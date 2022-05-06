package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreBottomSheetFragment
import kg.iaau.diploma.core.utils.hide
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.data.Slot
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentCalendarBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.TimeAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.TimeListener

@AndroidEntryPoint
class TimeBottomSheetFragment : CoreBottomSheetFragment<FragmentCalendarBottomSheetBinding, ClinicVM>(ClinicVM::class.java), TimeListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalendarBottomSheetBinding
        get() = FragmentCalendarBottomSheetBinding::inflate

    private val adapter = TimeAdapter(this)
    private val args: TimeBottomSheetFragmentArgs by navArgs()
    private val schedule: Interval by lazy { args.schedule }
    private val date: Array<Slot> by lazy { args.date }

    override fun setupFragmentView() {
        vb.run {
            tvHeader.text = getString(R.string.choose_time)
            rvTime.adapter = adapter
            setupDate(date.toMutableList())
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener { checkChoosingTime() }
        }
    }

    private fun setupDate(slots: MutableList<Slot>) {
        when(slots.isNullOrEmpty()) {
            true -> hideTime()
            else -> showTime(slots)
        }
    }

    private fun hideTime() {
        vb.run {
            rvTime.hide()
            ivEmpty.show()
        }
    }

    private fun showTime(slots: MutableList<Slot>) {
        vb.run {
            rvTime.show()
            ivEmpty.hide()
            adapter.submitList(slots)
        }
    }

    private fun checkChoosingTime() {
        requireActivity().apply {
            when(vm.slot) {
                null -> toast(getString(R.string.time_not_selected))
                else -> navigateToReverseVisit(vm.slot)
            }
        }
    }

    private fun navigateToReverseVisit(slot: Slot?) {
        findNavController().navigate(
            R.id.nav_reserve_visit,
            Bundle().apply {
                putParcelable("schedule", schedule)
                putParcelable("slot", slot)
            }
        )
    }

    override fun onTimeClick(slot: Slot?): Boolean {
        if (slot != null && vm.slot != null) return false
        else vm.setSlot(slot)
        return true
    }

}