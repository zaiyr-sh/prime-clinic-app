package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.YYYY_MM_DD
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.Notification
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.data.MedCardImage
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAddMedCardBinding
import kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet.AgreementBottomSheetFragment
import kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet.ProfilePictureBottomSheetFragment
import java.io.File
import java.util.*

@AndroidEntryPoint
class AddMedCardFragment : CoreFragment<FragmentAddMedCardBinding, MedCardVM>(MedCardVM::class.java) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddMedCardBinding =
        FragmentAddMedCardBinding::inflate

    private val args: AddMedCardFragmentArgs by navArgs()
    private val isAgreementAccepted: Boolean by lazy { args.isAgreementAccepted }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        vm.getMedCard()
        vm.getMedCardImageById()
        return vb.root
    }

    override fun setupFragmentView() {
        vb.run {
            if (isAgreementAccepted) {
                btnSendMedCard.text = getString(R.string.edit)
                llCheckAgreement.gone()
            }
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            btnSendMedCard.setOnClickListener {
                uploadMedCard()
            }
            tvAgreement.setOnClickListener {
                AgreementBottomSheetFragment.show(requireActivity().supportFragmentManager)
            }
            ivUserPicture.setOnClickListener {
                ProfilePictureBottomSheetFragment.show(requireActivity().supportFragmentManager) { uri ->
                    vm.setProfilePicture(uri)
                }
            }
            ccp.registerCarrierNumberEditText(etPhone)
            addBirthdateListener()
        }
    }

    private fun addBirthdateListener() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        vb.run {
            vb.etBirthdate.setOnClickListener {
                val dpd = DatePickerDialog(requireActivity(), R.style.DialogTheme, { _, mYear, mMonth, mDay  ->
                    etBirthdate.text = Calendar.getInstance()
                        .apply { set(mYear, mMonth, mDay) }.time
                        .formatForDate(YYYY_MM_DD)
                }, year, month, day)
                dpd.show()
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
                .filterNot { it.isWhitespace() }
                .convertPhoneNumberWithCode(vb.ccp.selectedCountryCode)
            if (!cbAgreement.isChecked && llCheckAgreement.isVisible) {
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
            if(birth.isEmpty()) {
                etBirthdate.error  = getString(R.string.enter_valid_birthdate)
                isDataValid = false
            }
            if(phone.isEmpty()) {
                etPhone.error  = getString(R.string.enter_valid_phone_number)
                isDataValid = false
            }
            if (isDataValid) {
                vm.uploadMedCard(name, surname, patronymic, birth, phone)
            }
        }
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
                setupMedCardImage(medCardImage)
            }
        }
        vm.imageUriLiveData.observe(viewLifecycleOwner) { imageUri ->
            imageUri?.let { uri ->
                vb.ivUserPicture.loadWithFresco(uri, onFail = {
                    vb.ivUserPicture.setActualImageResource(R.drawable.ic_photo)
                })
                uploadImage(uri)
            }
        }
    }

    private fun setupMedCardFields(medCard: MedCard) {
        vb.run {
            etName.setText(medCard.firstName)
            etSurname.setText(medCard.lastName)
            etPatronymic.setText(medCard.patronymic)
            ccp.fullNumber = medCard.medCardPhoneNumber
            etPhone.setText(medCard.medCardPhoneNumber?.removePrefix(ccp.selectedCountryCodeWithPlus))
            ccp.registerCarrierNumberEditText(etPhone)
            etBirthdate.text = medCard.birthDate
        }
    }

    private fun setupMedCardImage(medCardImage: MedCardImage) {
        vb.ivUserPicture.loadBase64Image(requireContext(), medCardImage.image, R.drawable.ic_photo)
    }

    private fun uploadImage(uri: Uri) {
        uri.path?.let { path ->
            vm.uploadMedCardImage(File(path).createFormData("imageFile"))
        }
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

    override fun notificationAction(event: Notification) {
        goneLoader()
        event.title?.let { view?.showSnackBar(requireContext(), getString(it)) }
        when (event.title) {
            R.string.med_card_photo_sent_unsuccessfully -> vb.ivUserPicture.setActualImageResource(R.drawable.ic_photo)
            R.string.med_card_created_unsuccessfully -> findNavController().navigateUp()
        }
    }

}