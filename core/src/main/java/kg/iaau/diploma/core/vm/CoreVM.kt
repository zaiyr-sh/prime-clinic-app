package kg.iaau.diploma.core.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kg.iaau.diploma.core.utils.CoreEvent.Loading
import kg.iaau.diploma.core.utils.CoreEvent.Error
import kg.iaau.diploma.core.utils.CoreEvent.Success
import kg.iaau.diploma.core.utils.Event
import retrofit2.HttpException

abstract class CoreVM : ViewModel() {

    var event: MutableLiveData<Event> = MutableLiveData()

    fun safeLaunch(dispatcher: CoroutineDispatcher = Dispatchers.Main, action: suspend () -> Unit) {
        viewModelScope.launch(dispatcher) {
            try {
                event.postValue(Loading(true))
                event.postValue(Success(action()))
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        var message = ""
                        when(throwable.code()) {

                        }
                        event.postValue(Error(false, throwable.code(), throwable.response()?.errorBody(), message))
                    }
                    else -> {
                        event.postValue(Error(true, null, null, "Ошибка сети. Проверьте интернет соединение"))
                    }
                }
            }
        }
    }
}