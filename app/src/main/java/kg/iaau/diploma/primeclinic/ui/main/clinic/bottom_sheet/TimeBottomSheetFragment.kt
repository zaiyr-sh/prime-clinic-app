package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.hide
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.Slot
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentCalendarBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.TimeAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.TimeListener

@AndroidEntryPoint
class TimeBottomSheetFragment : BottomSheetDialogFragment(), TimeListener {

    private lateinit var vb: FragmentCalendarBottomSheetBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val adapter = TimeAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        vb = FragmentCalendarBottomSheetBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetView()
    }

    private fun setupBottomSheetView() {
        vb.run {
            vb.progressBar.gone()
            tvHeader.text = getString(R.string.choose_time)
            rvTime.adapter = adapter
            setupDate(vm.scheduleDate?.reservation?.filter { it.id == null })
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener { checkChoosingTime() }
        }
    }

    private fun setupDate(slots: List<Slot>?) {
        vb.run {
            if(slots.isNullOrEmpty()) {
                rvTime.hide()
                ivEmpty.show()
            } else {
                rvTime.show()
                ivEmpty.hide()
                adapter.submitList(slots)
            }
        }
    }

    private fun checkChoosingTime() {
        requireActivity().apply {
            if (vm.slot != null)
                findNavController().navigate(R.id.nav_reserve_visit)
            else
                toast(getString(R.string.time_not_selected))
        }
    }

    override fun onTimeClick(slot: Slot?): Boolean {
        if (slot != null && vm.slot != null) return false
        else vm.setSlot(slot)
        return true
    }

}