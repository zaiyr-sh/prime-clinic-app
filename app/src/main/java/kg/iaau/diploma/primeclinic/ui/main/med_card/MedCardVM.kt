package kg.iaau.diploma.primeclinic.ui.main.med_card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.utils.convertToUTC
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.primeclinic.repository.MedCardRepository
import javax.inject.Inject

@HiltViewModel
class MedCardVM @Inject constructor(private val repository: MedCardRepository) : CoreVM() {

    val medCardLiveData: LiveData<MedCard?>
        get() = _medCardLiveData
    private val _medCardLiveData = MutableLiveData<MedCard?>()

    fun uploadMedCard(firstName: String?, lastName: String?, patronymic: String?, birth: String?, phone: String?) {
        val medCard = MedCard(
            image = null,
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
            birthDate = birth?.convertToUTC(),
            phone = phone
        )
        safeLaunch(
            action = {
                repository.uploadMedCard(medCard)
                medCard.birthDate = birth
                saveMedCardInDb(medCard)
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
}