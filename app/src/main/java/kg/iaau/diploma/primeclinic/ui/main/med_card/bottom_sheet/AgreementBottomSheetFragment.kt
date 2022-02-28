package kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAgreementBottomSheetBinding

@AndroidEntryPoint
class AgreementBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var vb: FragmentAgreementBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vb = FragmentAgreementBottomSheetBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.ibDismiss.setOnClickListener { dismiss() }
    }

    companion object {
        private val bottomSheet = AgreementBottomSheetFragment()
        fun show(supportFragmentManager: FragmentManager) {
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

}