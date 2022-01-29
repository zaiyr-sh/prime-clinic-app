package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.isNotField
import kg.iaau.diploma.core.utils.isDateNotField
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAddMedCardBinding
import kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet.AgreementBottomSheetFragment

@AndroidEntryPoint
class AddMedCardFragment : Fragment() {

    private lateinit var vb: FragmentAddMedCardBinding
    private val vm: MedCardVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentAddMedCardBinding.inflate(inflater, container, false)
        vm.getMedCard()
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.run {
            btnSendMedCard.setOnClickListener {
                uploadMedCard()
            }
            tvAgreement.setOnClickListener {
                AgreementBottomSheetFragment.show(requireActivity().supportFragmentManager)
            }
            toolbar.setNavigationOnClickListener {
                view?.findNavController()?.navigateUp()
            }
        }
    }

    private fun uploadMedCard() {
        var isDataValid = true
        vb.run {
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val patronymic = etPatronymic.text.toString()
            val birth = etBirthdate.text.toString()
            val phone = etPhone.text.toString()
            if (!cbAgreement.isChecked) {
                requireActivity().toast(getString(R.string.agreement_absent))
                isDataValid = false
            }
            if(name.isNotField) {
                etName.error  = getString(R.string.enter_valid_name)
                isDataValid = false
            }
            if(surname.isNotField) {
                etSurname.error  = getString(R.string.enter_valid_surname)
                isDataValid = false
            }
            if(patronymic.isNotField) {
                etPatronymic.error  = getString(R.string.enter_valid_patronymic)
                isDataValid = false
            }
            if(birth.isDateNotField) {
                etBirthdate.error  = getString(R.string.enter_valid_birthdate)
                isDataValid = false
            }
            if (isDataValid) {
                vm.uploadMedCard(name, surname, patronymic, birth, phone)
                view?.findNavController()?.navigateUp()
            }
        }
    }

    private fun observeLiveData() {
        vm.medCardLiveData.observe(viewLifecycleOwner, { medCard ->
            medCard?.let {
                setupMedCardFields(medCard)
            }
        })
    }

    private fun setupMedCardFields(medCard: MedCard) {
        vb.run {
            etName.setText(medCard.firstName)
            etSurname.setText(medCard.lastName)
            etPatronymic.setText(medCard.patronymic)
            etBirthdate.setText(medCard.birthDate)
            etPhone.setText(medCard.phone)
            cbAgreement.isChecked = medCard.agreed == true
            medCard.image?.let { image ->
                Glide.with(requireContext()).load(Uri.parse(image)).into(ivUserPicture)
            }
        }
    }

}