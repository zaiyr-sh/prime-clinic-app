package kg.iaau.diploma.primeclinic.repository.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kg.iaau.diploma.core.R
import kg.iaau.diploma.core.constants.DEFAULT_PAGE_INDEX
import kg.iaau.diploma.core.utils.CoreEvent
import kg.iaau.diploma.core.utils.Event
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.network.api.ApiClinic

class ClinicSpecialistsDS(private var event: MutableLiveData<Event>, private val apiClinic: ApiClinic) : PagingSource<Int, SpecialistCategory>() {

    override fun getRefreshKey(state: PagingState<Int, SpecialistCategory>): Int? = null

    /**
     * calls api if there is any error getting results then return the [ LoadResult.Error ]
     * for successful response return the results using [ LoadResult.Page ] for some reason if the results
     * are empty from service like in case of no more data from api then we can pass [ null ] to
     * send signal that source has reached the end of list
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SpecialistCategory> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            event.postValue(CoreEvent.Loading(true))
            val response = apiClinic.getSpecialistCategories(page).content ?: listOf()
            event.postValue(CoreEvent.Success())
            LoadResult.Page(
                response,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (throwable: Throwable) {
            event.postValue(CoreEvent.Error(true, null, null, R.string.network_error))
            return LoadResult.Error(throwable)
        }
    }
}