package kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreBottomSheetFragment
import kg.iaau.diploma.primeclinic.databinding.FragmentAgreementBottomSheetBinding

@AndroidEntryPoint
class AgreementBottomSheetFragment : CoreBottomSheetFragment<FragmentAgreementBottomSheetBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAgreementBottomSheetBinding =
        FragmentAgreementBottomSheetBinding::inflate

    override fun setupFragmentView() {
        vb.ibDismiss.setOnClickListener { dismiss() }
    }

    companion object {
        private val bottomSheet = AgreementBottomSheetFragment()
        fun show(supportFragmentManager: FragmentManager) {
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

}