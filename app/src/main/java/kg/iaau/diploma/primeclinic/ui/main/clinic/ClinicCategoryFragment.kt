package kg.iaau.diploma.primeclinic.ui.main.clinic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.CoreEvent.*
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentClinicCategoryBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.ClinicSpecialistAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.ClinicSpecialistListener

@AndroidEntryPoint
class ClinicCategoryFragment : Fragment(), ClinicSpecialistListener {

    private lateinit var vb: FragmentClinicCategoryBinding
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private var adapter = ClinicSpecialistAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb =  FragmentClinicCategoryBinding.inflate(inflater, container, false)
        vm.getSpecialistCategories()
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.run {
            rvSpecialists.adapter = adapter
            adapter.submitList(
                listOf(
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                    SpecialistCategory(id = 1, name = "Диетолог", description = "Специалист правильного питания"),
                ))
        }
    }

    private fun observeLiveData() {
        vm.specialistsLiveData.observe(viewLifecycleOwner, { specialists ->
            specialists?.let {
                adapter.submitList(it)
            }
        })
        vm.event.observe(this, { event ->
            when(event) {
                is Loading -> showLoader()
                is Success -> goneLoader()
                is Error -> errorAction(event)
            }
        })
    }

    private fun errorAction(event: Error) {
        when (event.isNetworkError) {
            true -> requireActivity().toast(event.message)
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.progressBar.show()
    }

    private fun goneLoader() {
        vb.progressBar.gone()
    }

    override fun onSpecialistClick(id: Long?) {
        val args = Bundle()
        if (id != null) { args.putLong("id", id) }
        view?.findNavController()?.navigate(R.id.nav_about_clinic_category, args)
    }

}