package kg.iaau.diploma.primeclinic.ui.main.clinic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.Doctor
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.primeclinic.repository.ClinicRepository
import javax.inject.Inject

@HiltViewModel
class ClinicVM @Inject constructor(val repository: ClinicRepository) : CoreVM() {

    val specialistLiveData: LiveData<SpecialistCategory?>
        get() = _specialistLiveData
    private val _specialistLiveData = MutableLiveData<SpecialistCategory?>()

    val doctorLiveData: LiveData<Doctor?>
        get() = _doctorLiveData
    private val _doctorLiveData = MutableLiveData<Doctor?>()

    val doctorSchedule: LiveData<List<Interval>?>
        get() = _doctorSchedule
    private val _doctorSchedule = MutableLiveData<List<Interval>?>()

    fun getSpecialistCategories(): LiveData<PagingData<SpecialistCategory>> {
        return repository.getSpecialistCategories()
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
                _doctorSchedule.postValue(repository.getScheduleByDoctorId(id))
            }
        )
    }

}