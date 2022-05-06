package kg.iaau.diploma.primeclinic.ui.main.chat

import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ChatVM @Inject constructor(
    val prefs: StoragePreferences,
    val repository: AuthRepository
) : CoreVM() {

    val phone: String? = prefs.phone
    val userId: Long? = prefs.userId
    val id: Long? = prefs.id

    fun logout() {
        repository.restorePinWithTokens()
    }

}