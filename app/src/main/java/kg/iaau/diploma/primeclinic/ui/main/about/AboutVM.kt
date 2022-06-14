package kg.iaau.diploma.primeclinic.ui.main.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.About
import kg.iaau.diploma.primeclinic.repository.AboutRepository
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AboutVM @Inject constructor(
    private val repository: AboutRepository,
    private val authRepository: AuthRepository
) : CoreVM() {

    val aboutInfoLiveData: LiveData<List<About>?>
        get() = _aboutInfoLiveData
    private val _aboutInfoLiveData = MutableLiveData<List<About>?>()

    fun getInfoAboutUs() {
        safeLaunch(
            action = {
                insertAboutUsInfo(repository.getInfoAboutUs())
            }
        )
    }

    private fun insertAboutUsInfo(aboutInfo: List<About>) {
        safeLaunch(
            action = {
                repository.insertAboutUsInfo(aboutInfo)
            },
            success = {
                getInfoAboutUsFromDb()
            },
            fail = {
                getInfoAboutUsFromDb()
            }
        )
    }

    private fun getInfoAboutUsFromDb() {
        safeLaunch(
            action = {
                _aboutInfoLiveData.postValue(repository.getInfoAboutUsFromDb())
            }
        )
    }

    fun logout() {
        authRepository.restorePinWithTokens()
    }

}