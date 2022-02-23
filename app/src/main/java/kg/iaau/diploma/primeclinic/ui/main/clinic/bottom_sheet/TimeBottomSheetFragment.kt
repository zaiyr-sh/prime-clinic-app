package kg.iaau.diploma.primeclinic.ui.main.clinic.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kg.iaau.diploma.core.constants.DATE_NOT_SELECTED
import kg.iaau.diploma.core.constants.TIME_NOT_SELECTED
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.Slot
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentCalendarBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.TimeAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.TimeListener

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
            rvTime.adapter = adapter
            adapter.submitList(vm.scheduleDate?.reservation)
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener { checkChoosingTime() }
        }
    }

    private fun checkChoosingTime() {
        requireActivity().apply {
            if (vm.slot != null) {
                dismiss()
            } else {
                toast(TIME_NOT_SELECTED)
            }
        }
    }

    override fun onTimeClick(slot: Slot) {
        vm.setSlot(slot)
    }

    companion object {
        private val bottomSheet = TimeBottomSheetFragment()
        fun show(supportFragmentManager: FragmentManager) {
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

}