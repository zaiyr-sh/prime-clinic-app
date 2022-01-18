package kg.iaau.diploma.primeclinic.ui.authorization

import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthorizationVM @Inject constructor(private val repository: AuthRepository) : CoreVM() {

    fun auth(phone: String, password: String) {
        safeLaunch {
            repository.auth(phone, password)
        }
    }

}