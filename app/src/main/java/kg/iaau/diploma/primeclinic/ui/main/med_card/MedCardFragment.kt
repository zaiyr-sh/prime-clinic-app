package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        vm.getMedCard()
        vm.getMedCardImageById()
        return vb.root
    }

    override fun setupFragmentView() {
        vb.run {
            llAddMedCard.setOnClickListener { openAddMedCardFragment() }
            ibEdit.setOnClickListener { openAddMedCardFragment(true) }
        }
    }

    private fun openAddMedCardFragment(isAgreementAccepted: Boolean = false) {
        findNavController().navigate(
            R.id.nav_add_med_card,
            Bundle().apply {
                putBoolean("isAgreementAccepted", isAgreementAccepted)
            }
        )
    }

    override fun observeLiveData() {
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
        vm.imageUriLiveData.observe(viewLifecycleOwner) { imageUri ->
            imageUri?.let {
                Glide.with(requireContext()).load(it).into(vb.ivUser)
            }
        }
    }

    private fun setupMedCardFields(medCard: MedCard) {
        vb.run {
            cvMedCard.show()
            llAddMedCard.gone()
            tvName.text = getString(R.string.full_name, medCard.lastName, medCard.firstName, medCard.patronymic)
            tvBirthday.text = getString(R.string.birth_date, medCard.birthDate)
            medCard.medCardPhoneNumber.let {
                if(it.isNullOrEmpty())
                    tvPhone.text = getString(R.string.whatsapp_number, getString(R.string.undefined_phone_number))
                else
                    tvPhone.text = getString(R.string.whatsapp_number, it)
            }
        }
    }

    private fun setupMedCardImage(medCardImage: MedCardImage) {
        medCardImage.image?.let { image ->
            Glide.with(requireContext()).load(Uri.parse(image)).into(vb.ivUser)
        }
    }

    override fun errorAction(event: CoreEvent.Error) {
        super.errorAction(event)
        if (!event.isNetworkError) requireActivity().toast(getString(R.string.unexpected_error))
        goneLoader()
    }

    override fun showLoader() {
        super.showLoader()
        vb.clContainer.run {
            setAnimateAlpha(0.5f)
            setEnable(false)
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        vb.clContainer.run {
            setAnimateAlpha(1f)
            setEnable(true)
        }
    }

}