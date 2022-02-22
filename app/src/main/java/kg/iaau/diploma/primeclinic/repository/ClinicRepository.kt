package kg.iaau.diploma.primeclinic.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import kg.iaau.diploma.core.constants.DEFAULT_PAGE_SIZE
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.network.api.ApiClinic
import kg.iaau.diploma.primeclinic.repository.paging.ClinicSpecialistsDS

class ClinicRepository(
    private val apiClinic: ApiClinic
) {

    fun getSpecialistCategories(pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<SpecialistCategory>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { ClinicSpecialistsDS(apiClinic) }
        ).liveData
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }
    suspend fun getSpecialistsCategoryDetailInfo(id: Long) = apiClinic.getSpecialistsCategoryDetailInfo(id)

    suspend fun getDoctorProfileById(id: Long) = apiClinic.getDoctorProfileById(id)

    suspend fun getScheduleByDoctorId(id: Long?) = apiClinic.getScheduleByDoctorId(id)

}