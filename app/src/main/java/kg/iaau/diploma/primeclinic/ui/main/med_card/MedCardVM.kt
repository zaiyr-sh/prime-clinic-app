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
import okhttp3.MultipartBody
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

    val imagePathLiveData: LiveData<String?>
        get() = _imagePathLiveData
    private val _imagePathLiveData = MutableLiveData<String?>()

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
            },
            success = {
                event.postValue(CoreEvent.Notification(title = R.string.med_card_created_successfully))
            },
            fail = {
                event.postValue(CoreEvent.Notification(title = R.string.med_card_created_unsuccessfully))
            }
        )
    }

    fun uploadMedCardImage(file: MultipartBody.Part) {
        _imageUriLiveData.value = null
        _imagePathLiveData.value = null
        safeLaunch(
            action = {
                repository.uploadMedCardImageById(file)
            },
            success = {
                event.postValue(CoreEvent.Notification(title = R.string.med_card_photo_sent_successfully))
            },
            fail = {
                event.postValue(CoreEvent.Notification(title = R.string.med_card_photo_sent_unsuccessfully))
            }
        )
    }

    fun getMedCard() {
        safeLaunch(
            action = {
                _medCardLiveData.postValue(repository.getMedCard())
                _medCardImageLiveData.postValue(repository.getMedCardImageById())
            }
        )
    }

    fun setProfilePicture(imageUri: Uri, path: String?) {
        _imageUriLiveData.value = imageUri
        _imagePathLiveData.value = path
    }

}