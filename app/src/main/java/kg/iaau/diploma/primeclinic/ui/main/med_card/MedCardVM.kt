package kg.iaau.diploma.primeclinic.ui.main.med_card

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.constants.MED_CARD_CREATED_SUCCESSFULLY
import kg.iaau.diploma.core.constants.MED_CARD_CREATED_UNSUCCESSFULLY
import kg.iaau.diploma.core.utils.CoreEvent
import kg.iaau.diploma.core.utils.convertToUTC
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.primeclinic.repository.MedCardRepository
import javax.inject.Inject

@HiltViewModel
class MedCardVM @Inject constructor(
    private val repository: MedCardRepository
) : CoreVM() {

    val medCardLiveData: LiveData<MedCard?>
        get() = _medCardLiveData
    private val _medCardLiveData = MutableLiveData<MedCard?>()

    val imageUriLiveData: LiveData<String?>
        get() = _imageUriLiveData
    private val _imageUriLiveData = MutableLiveData<String?>()

    fun uploadMedCard(firstName: String?, lastName: String?, patronymic: String?, birth: String?, phone: String?) {
        val medCard = MedCard(
            image = imageUriLiveData.value,
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
            birthDate = birth?.convertToUTC(),
            medCardPhoneNumber = phone
        )
        safeLaunch(
            action = {
                repository.uploadMedCard(medCard)
                medCard.birthDate = birth
                saveMedCardInDb(medCard)
                event.postValue(CoreEvent.Notification(message = MED_CARD_CREATED_SUCCESSFULLY))
            },
            fail = {
                event.postValue(CoreEvent.Notification(
                    title = MED_CARD_CREATED_UNSUCCESSFULLY
                ))
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

    fun setProfilePicture(imageUri: String) {
        _imageUriLiveData.value = imageUri
    }
}