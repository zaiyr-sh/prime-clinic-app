package kg.iaau.diploma.primeclinic.ui.main.clinic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.utils.CoreEvent
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.*
import kg.iaau.diploma.primeclinic.repository.AuthRepository
import kg.iaau.diploma.primeclinic.repository.ClinicRepository
import javax.inject.Inject

@HiltViewModel
class ClinicVM @Inject constructor(
    val repository: ClinicRepository,
    val authRepository: AuthRepository
) : CoreVM() {

    val specialistLiveData: LiveData<SpecialistCategory?>
        get() = _specialistLiveData
    private val _specialistLiveData = MutableLiveData<SpecialistCategory?>()

    val doctorLiveData: LiveData<Doctor?>
        get() = _doctorLiveData
    private val _doctorLiveData = MutableLiveData<Doctor?>()

    val doctorScheduleLiveData: LiveData<List<Interval>?>
        get() = _doctorScheduleLiveData
    private val _doctorScheduleLiveData = MutableLiveData<List<Interval>?>()

    val scheduleDate: Interval?
        get() = _scheduleDate
    private var _scheduleDate: Interval? = null

    val slot: Slot?
        get() = _slot
    private var _slot: Slot? = null

    val paymentMethodsLiveData: LiveData<List<Payment>?>
        get() = _paymentMethodsLiveData
    private val _paymentMethodsLiveData = MutableLiveData<List<Payment>?>()

    fun getSpecialistCategories(): LiveData<PagingData<SpecialistCategory>> {
        return repository.getSpecialistCategories(event)
    }

    fun getSpecialistsCategoryDetailInfo(id: Long) {
        safeLaunch(
            action = {
                _specialistLiveData.postValue(repository.getSpecialistsCategoryDetailInfo(id))
            }
        )
    }

    fun getDoctorProfileById(id: Long) {
        safeLaunch(
            action = {
                _doctorLiveData.postValue(repository.getDoctorProfileById(id))
            }
        )
    }

    fun getScheduleByDoctorId(id: Long?) {
        safeLaunch(
            action = {
                _doctorScheduleLiveData.postValue(repository.getScheduleByDoctorId(id))
            }
        )
    }

    fun setDate(interval: Interval?) {
        _scheduleDate = interval
    }

    fun setSlot(slot: Slot?) {
        _slot = slot
    }

    fun reserveVisit(phoneNumber: String, comment: String) {
        val reservation = Reservation(
            clientId = repository.userId,
            worktimeId = scheduleDate?.id,
            start = slot?.start,
            end = slot?.end,
            comment = comment,
            phoneNumber = phoneNumber
        )
        safeLaunch(
            action = { repository.reserveVisit(reservation) },
            success = { event.postValue(CoreEvent.Notification(title = R.string.reservation_visit_successfully)) },
            fail = { event.postValue(CoreEvent.Notification(title = R.string.reservation_visit_unsuccessfully)) }
        )
    }

    fun getPaymentMethods() {
        safeLaunch(
            action = {
                _paymentMethodsLiveData.postValue(repository.getPaymentMethods())
            }
        )
    }

    fun logout() {
        authRepository.restorePinWithTokens()
    }

}