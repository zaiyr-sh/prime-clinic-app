package kg.iaau.diploma.primeclinic.ui.main.chat

import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import kg.iaau.diploma.primeclinic.repository.ClinicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatVM @Inject constructor(
    prefs: StoragePreferences,
    val repository: ClinicRepository
) : CoreVM() {

    val phone: String? = prefs.phone
    val userId: Long? = prefs.userId
    val id: Long? = prefs.id

    private val isTokenUpdated: MutableStateFlow<Boolean?> = MutableStateFlow(prefs.isTokenUpdated)

    fun getTokenUpdatingNotifyFlow() : StateFlow<Boolean?> = isTokenUpdated.asStateFlow()

    fun logout() {
        repository.restorePinWithTokens()
    }

}