package kg.iaau.diploma.primeclinic.ui.main.clinic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.iaau.diploma.core.vm.CoreVM
import kg.iaau.diploma.data.Doctor
import kg.iaau.diploma.data.Interval
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.primeclinic.repository.ClinicRepository
import javax.inject.Inject

@HiltViewModel
class ClinicVM @Inject constructor(val repository: ClinicRepository) : CoreVM() {

    private var page = 0

    val specialistsLiveData: LiveData<List<SpecialistCategory>?>
        get() = _specialistsLiveData
    private val _specialistsLiveData = MutableLiveData<List<SpecialistCategory>?>()

    val specialistLiveData: LiveData<SpecialistCategory?>
        get() = _specialistLiveData
    private val _specialistLiveData = MutableLiveData<SpecialistCategory?>()

    val doctorLiveData: LiveData<Doctor?>
        get() = _doctorLiveData
    private val _doctorLiveData = MutableLiveData<Doctor?>()

    val doctorSchedule: LiveData<List<Interval>?>
        get() = _doctorSchedule
    private val _doctorSchedule = MutableLiveData<List<Interval>?>()

    fun getSpecialistCategories() {
        safeLaunch(
            action = {
                _specialistsLiveData.postValue(repository.getSpecialistCategories(page).content)
                page++
                //todo
            }
        )
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

    fun getScheduleByDoctorId(id: Long) {
        safeLaunch(
            action = {
                _doctorSchedule.postValue(repository.getScheduleByDoctorId(id))
            }
        )
    }

}