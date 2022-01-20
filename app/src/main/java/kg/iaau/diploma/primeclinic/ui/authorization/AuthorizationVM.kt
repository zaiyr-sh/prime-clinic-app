package kg.iaau.diploma.primeclinic.ui.authorization

import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthorizationVM @Inject constructor(private val repository: AuthRepository) : CoreVM() {

    var pin: StringBuilder = StringBuilder()
    var dotPosition: Int = 0

    fun isFirstTimePinCreating() = repository.getPin().isNullOrEmpty()

    fun isUserSignIn() = !repository.token.isNullOrEmpty()

    fun auth(phone: String, password: String) {
        safeLaunch(
            action = {
                repository.auth(phone, password)
            }
        )
    }

    fun register(phone: String, password: String) {
        safeLaunch(
            action = {
                repository.register(phone, password)
            }
        )
    }

    fun verify(code: String) {
        safeLaunch(
            action = {
                repository.verify(code)
            }
        )
    }

    fun fillPin(digit: Int) {
        dotPosition++
        pin.append(digit)
    }

    fun deleteLastDigit() {
        dotPosition--
        pin.deleteCharAt(dotPosition)
    }

    fun clearPin() {
        dotPosition = 0
        pin.clear()
    }

    fun savePin() {
        repository.savePin(pin.toString())
    }

    fun restorePinWithTokens() {
        repository.restorePinWithTokens()
    }

    fun isPinVerified() = repository.getPin() == pin.toString()

    fun savePhoneWithDeviceId(phone: String?, deviceId: String?) {
        repository.phone = phone
        repository.deviceId = deviceId
    }

}