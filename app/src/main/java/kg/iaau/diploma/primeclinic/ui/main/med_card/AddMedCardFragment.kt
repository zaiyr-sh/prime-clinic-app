package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.MED_CARD_CREATED_UNSUCCESSFULLY
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.*
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.data.MedCardImage
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAddMedCardBinding
import kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet.AgreementBottomSheetFragment
import kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet.ProfilePictureBottomSheetFragment
import java.io.File
import java.util.*

@AndroidEntryPoint
class AddMedCardFragment : Fragment() {

    private lateinit var vb: FragmentAddMedCardBinding
    private val vm: MedCardVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val args: AddMedCardFragmentArgs by navArgs()
    private val isAgreementAccepted: Boolean by lazy { args.isAgreementAccepted }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentAddMedCardBinding.inflate(inflater, container, false)
        vm.getMedCard()
        vm.getMedCardImageById()
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.run {
            if (isAgreementAccepted) llCheckAgreement.gone()
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
                ProfilePictureBottomSheetFragment.show(requireActivity().supportFragmentManager)
            }
            setupBirthdateListener()
        }
    }

    private fun setupBirthdateListener() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        vb.run {
            vb.etBirthdate.setOnClickListener {
                val dpd = DatePickerDialog(requireActivity(), R.style.DialogTheme, { view, mYear, mMonth, mDay ->
                    etBirthdate.text = view.calendarView.date.formatForDate()
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
            if(phone.isPhoneNotFieldCorrectly) {
                etPhone.error  = getString(R.string.enter_valid_phone_number)
                isDataValid = false
            }
            if (isDataValid) {
                vm.uploadMedCard(name, surname, patronymic, birth, phone)
            }
        }
    }

    private fun observeLiveData() {
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
                Glide.with(requireContext()).load(uri).into(vb.ivUserPicture)
                vm.uploadMedCardImage(File(uri.path))
            }
        }
        vm.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Loading -> showLoader()
                is Success, is Error -> goneLoader()
                is Notification -> notificationAction(event)
            }
        }
    }

    private fun showLoader() {
        vb.run {
            progressBar.show()
            clContainer.setAnimateAlpha(0.5f)
            btnSendMedCard.setEnable(false)
        }
    }

    private fun goneLoader() {
        vb.run {
            progressBar.gone()
            clContainer.setAnimateAlpha(1f)
            btnSendMedCard.setEnable(true)
        }
    }

    private fun notificationAction(event: Notification) {
        goneLoader()
        event.title?.let { view?.showSnackBar(requireContext(), it) }
        if (event.title != MED_CARD_CREATED_UNSUCCESSFULLY) {
            findNavController().navigateUp()
        }
    }

    private fun setupMedCardFields(medCard: MedCard) {
        vb.run {
            etName.setText(medCard.firstName)
            etSurname.setText(medCard.lastName)
            etPatronymic.setText(medCard.patronymic)
            etPhone.setText(medCard.medCardPhoneNumber)
            etBirthdate.text = medCard.birthDate
        }
    }

    private fun setupMedCardImage(medCardImage: MedCardImage) {
        medCardImage.image?.let { image ->
            Glide.with(requireContext()).load(Uri.parse(image)).into(vb.ivUserPicture)
        }
    }

}