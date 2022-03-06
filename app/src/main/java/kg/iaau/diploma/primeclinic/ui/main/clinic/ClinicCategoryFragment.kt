package kg.iaau.diploma.primeclinic.ui.main.clinic

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.*
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentClinicCategoryBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.ClinicSpecialistAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.ClinicSpecialistListener
import kotlinx.coroutines.launch

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
        setHasOptionsMenu(true)
        (requireActivity() as? MainActivity)?.setSupportActionBar(vb.toolbar)
        return vb.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                requireContext().showDialog(R.string.exit_confirmation, {
                    vm.logout()
                    activity?.finishAffinity()
                    AuthorizationActivity.startActivity(requireContext())
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.rvSpecialists.adapter = adapter
    }

    private fun observeLiveData() {
        vm.getSpecialistCategories().observe(viewLifecycleOwner) { specialists ->
            lifecycleScope.launch {
                adapter.submitData(specialists)
            }
        }
        vm.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Loading -> showLoader()
                is Success -> goneLoader()
                is Error -> errorAction(event)
            }
        }
    }

    private fun errorAction(event: Error) {
        when (event.isNetworkError) {
            true -> requireActivity().toast(event.message)
            else -> requireActivity().toast(getString(R.string.unexpected_error))
        }
        goneLoader()
    }

    private fun showLoader() {
        vb.run {
            progressBar.show()
            clContainer.setAnimateAlpha(0.5f)
        }
    }

    private fun goneLoader() {
        vb.run {
            progressBar.gone()
            clContainer.setAnimateAlpha(1f)
        }
    }

    override fun onSpecialistClick(id: Long?) {
        findNavController().navigate(
            R.id.nav_about_clinic_category,
            Bundle().apply {
                id?.let { putLong("id", it) }
            }
        )
    }

}