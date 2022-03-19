package kg.iaau.diploma.primeclinic.ui.main.chat

import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import javax.inject.Inject

@HiltViewModel
class ChatVM @Inject constructor(
    private val prefs: StoragePreferences,
) : CoreVM() {

    val userId: Long? = prefs.userId
    val phone: String? = prefs.phone


}