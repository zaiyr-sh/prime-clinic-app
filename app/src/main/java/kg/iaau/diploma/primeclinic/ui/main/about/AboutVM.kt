package kg.iaau.diploma.primeclinic.ui.main.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.About
import kg.iaau.diploma.primeclinic.repository.AboutRepository
import javax.inject.Inject

@HiltViewModel
class AboutVM @Inject constructor(private val repository: AboutRepository) : CoreVM() {

    val aboutInfoLiveData: LiveData<List<About>?>
        get() = _aboutInfoLiveData
    private val _aboutInfoLiveData = MutableLiveData<List<About>?>()

    fun getInfoAboutUs() {
        safeLaunch(
            action = {
                val aboutInfo = repository.getInfoAboutUs()
                insertAboutUsInfo(aboutInfo)
            }
        )
    }

    private fun insertAboutUsInfo(aboutInfo: List<About>) {
        safeLaunch(
            action = {
                repository.insertAboutUsInfo(aboutInfo)
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

}