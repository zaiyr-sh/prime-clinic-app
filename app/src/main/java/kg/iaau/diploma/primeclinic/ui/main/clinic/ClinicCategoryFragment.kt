package kg.iaau.diploma.primeclinic.ui.main.clinic

import android.os.Bundle
import android.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.Error
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentClinicCategoryBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.ClinicSpecialistAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.ClinicSpecialistListener
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClinicCategoryFragment : CoreFragment<FragmentClinicCategoryBinding, ClinicVM>(ClinicVM::class.java), ClinicSpecialistListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentClinicCategoryBinding =
        FragmentClinicCategoryBinding::inflate

    private var adapter = ClinicSpecialistAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
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

    override fun setupFragmentView() {
        vb.rvSpecialists.adapter = adapter
    }

    override fun observeLiveData() {
        super.observeLiveData()
        vm.getSpecialistCategories().observe(viewLifecycleOwner) { specialists ->
            lifecycleScope.launch {
                adapter.submitData(specialists)
            }
        }
    }

    override fun errorAction(event: Error) {
        super.errorAction(event)
        if (!event.isNetworkError) requireActivity().toast(getString(R.string.unexpected_error))
        vb.ivEmpty.show()
    }

    override fun showLoader() {
        super.showLoader()
        vb.clContainer.run {
            setAnimateAlpha(0.5f)
            setEnable(false)
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        vb.clContainer.run {
            setAnimateAlpha(1f)
            setEnable(true)
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