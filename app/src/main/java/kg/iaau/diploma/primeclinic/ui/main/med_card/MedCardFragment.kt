package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.data.MedCardImage
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentMedCardBinding

@AndroidEntryPoint
class MedCardFragment : CoreFragment<FragmentMedCardBinding, MedCardVM>(MedCardVM::class.java) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMedCardBinding
        get() = FragmentMedCardBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getMedCard()
    }

    override fun onResume() {
        super.onResume()
        vm.getMedCard()
    }

    override fun setupFragmentView() {
        vb.run {
            llAddMedCard.setOnClickListener { navigateToAddMedCard() }
            ibEdit.setOnClickListener { navigateToAddMedCard(true) }
            swipeToRefresh.setOnRefreshListener { vm.getMedCard() }
        }
    }

    private fun navigateToAddMedCard(isAgreementAccepted: Boolean = false) {
        findNavController().navigate(
            R.id.nav_add_med_card,
            Bundle().apply {
                putBoolean("isAgreementAccepted", isAgreementAccepted)
            }
        )
    }

    override fun observeLiveData() {
        super.observeLiveData()
        vm.medCardLiveData.observe(viewLifecycleOwner) { medCard ->
            medCard?.let {
                setupMedCardFields(it)
            }
        }
        vm.medCardImageLiveData.observe(viewLifecycleOwner) { medCardImage ->
            medCardImage?.let {
                setupMedCardImage(it)
            }
        }
    }

    private fun setupMedCardFields(medCard: MedCard) {
        vb.run {
            cvMedCard.show()
            llAddMedCard.gone()
            tvName.text = getString(R.string.full_name, medCard.lastName, medCard.firstName, medCard.patronymic)
            tvBirthday.text = getString(R.string.birth_date, medCard.birthDate)
            when(medCard.medCardPhoneNumber.isNullOrEmpty()) {
                true -> getString(R.string.whatsapp_number, getString(R.string.undefined_phone_number))
                else -> getString(R.string.whatsapp_number, medCard.medCardPhoneNumber)
            }
        }
    }

    private fun setupMedCardImage(medCardImage: MedCardImage) {
        vb.ivUser.loadBase64Image(requireContext(), medCardImage.image, R.drawable.ic_photo)
    }

    override fun errorAction(event: CoreEvent.Error) {
        super.errorAction(event)
        if (!event.isNetworkError) requireActivity().toast(getString(R.string.unexpected_error))
        goneLoader()
    }

    override fun showLoader() {
        if(!vb.swipeToRefresh.isRefreshing)
            super.showLoader()
        vb.clContainer.run {
            setAnimateAlpha(0.5f)
            setEnable(false)
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        with(vb) {
            clContainer.run {
                setAnimateAlpha(1f)
                setEnable(true)
            }
            swipeToRefresh.isRefreshing = false
        }
    }

}