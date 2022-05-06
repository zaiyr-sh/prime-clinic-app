package kg.iaau.diploma.primeclinic.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kg.iaau.diploma.core.constants.DEFAULT_PAGE_SIZE
import kg.iaau.diploma.core.utils.Event
import kg.iaau.diploma.data.Reservation
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.local_storage.prefs.StoragePreferences
import kg.iaau.diploma.network.api.ApiClinic
import kg.iaau.diploma.primeclinic.repository.paging.ClinicSpecialistsDS

class ClinicRepository(
    prefs: StoragePreferences,
    private val apiClinic: ApiClinic
) {

    var userId: Long? = prefs.id

    fun getSpecialistCategories(event: MutableLiveData<Event>, pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<SpecialistCategory>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ClinicSpecialistsDS(event, apiClinic) }
        ).liveData
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    suspend fun getSpecialistsCategoryDetailInfo(id: Long) = apiClinic.getSpecialistsCategoryDetailInfo(id)

    suspend fun getDoctorProfileById(id: Long) = apiClinic.getDoctorProfileById(id)

    suspend fun getScheduleByDoctorId(id: Long?) = apiClinic.getScheduleByDoctorId(id)

    suspend fun reserveVisit(reservation: Reservation) = apiClinic.reserveVisit(reservation)

    suspend fun getPaymentMethods() = apiClinic.getPaymentMethods()

}