package kg.iaau.diploma.primeclinic.ui.main.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.Faq
import kg.iaau.diploma.primeclinic.repository.FaqRepository
import javax.inject.Inject

@HiltViewModel
class FaqVM @Inject constructor(private val repository: FaqRepository) : CoreVM() {

    val faqLiveData: LiveData<List<Faq>?>
        get() = _faqLiveData
    private val _faqLiveData = MutableLiveData<List<Faq>?>()

    fun getFaq() {
        safeLaunch(
            action = {
                insertFaqs(repository.getFaq())
            }
        )
    }

    private fun insertFaqs(faqs: List<Faq>) {
        safeLaunch(
            action = {
                repository.insertFaqs(faqs)
                getFaqFromDb()
            },
            fail = {
                getFaqFromDb()
            }
        )
    }

    private fun getFaqFromDb() {
        safeLaunch(
            action = {
                _faqLiveData.postValue(repository.getFaqFromDb())
            }
        )
    }

}