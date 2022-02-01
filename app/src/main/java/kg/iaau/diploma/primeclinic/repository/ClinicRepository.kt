package kg.iaau.diploma.primeclinic.repository

import kg.iaau.diploma.network.api.ApiClinic

class ClinicRepository(
    private val apiClinic: ApiClinic
) {

    suspend fun getSpecialistCategories(page: Int) = apiClinic.getSpecialistCategories(page)

    suspend fun getSpecialistsCategoryDetailInfo(id: Long) = apiClinic.getSpecialistsCategoryDetailInfo(id)

}