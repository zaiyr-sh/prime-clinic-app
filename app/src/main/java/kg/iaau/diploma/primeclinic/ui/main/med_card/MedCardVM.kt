package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.utils.CoreEvent
import kg.iaau.diploma.core.utils.convertToUTC
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.data.MedCardImage
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.repository.MedCardRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MedCardVM @Inject constructor(
    private val repository: MedCardRepository
) : CoreVM() {

    val medCardLiveData: LiveData<MedCard?>
        get() = _medCardLiveData
    private val _medCardLiveData = MutableLiveData<MedCard?>()

    val medCardImageLiveData: LiveData<MedCardImage?>
        get() = _medCardImageLiveData
    private val _medCardImageLiveData = MutableLiveData<MedCardImage?>()

    val imageUriLiveData: LiveData<Uri?>
        get() = _imageUriLiveData
    private val _imageUriLiveData = MutableLiveData<Uri?>()

    fun uploadMedCard(firstName: String, lastName: String, patronymic: String, birth: String, phone: String) {
        val medCard = MedCard(
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
            birthDate = birth.convertToUTC(),
            medCardPhoneNumber = phone
        )
        safeLaunch(
            action = {
                repository.uploadMedCard(medCard)
                medCard.birthDate = birth
                saveMedCardInDb(medCard)
                event.postValue(CoreEvent.Notification(title = R.string.med_card_created_successfully))
            },
            fail = {
                event.postValue(CoreEvent.Notification(title = R.string.med_card_created_unsuccessfully))
            }
        )
    }

    fun uploadMedCardImage(file: File) {
        val filePart: MultipartBody.Part = createFormData(
            "imageFile",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        safeLaunch(
            action = {
                repository.uploadMedCardImageById(filePart)
            }
        )
    }

    private fun saveMedCardInDb(medCard: MedCard) {
        safeLaunch(
            action = {
                repository.saveMedCardInDb(medCard)
            }
        )
    }

    fun getMedCard() {
        safeLaunch(
            action = {
                _medCardLiveData.postValue(repository.getMedCard())
            }
        )
    }

    fun getMedCardImageById() {
        safeLaunch(
            action = {
                _medCardImageLiveData.postValue(repository.getMedCardImageById())
            }
        )
    }

    fun setProfilePicture(imageUri: Uri) {
        _imageUriLiveData.value = imageUri
    }

}