package kg.iaau.diploma.primeclinic.ui.main.about

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.About
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.main.about.adapter.AboutAdapter

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private lateinit var vb: FragmentAboutBinding
    private val vm: AboutVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private var aboutAdapter = AboutAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getInfoAboutUs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentAboutBinding.inflate(inflater, container, false)
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
                context?.showDialog(R.string.exit_confirmation, {
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
        vb.rvAbout.adapter = aboutAdapter
    }

    private fun observeLiveData() {
        vm.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Error -> errorAction(event)
            }
        }
        vm.aboutInfoLiveData.observe(viewLifecycleOwner) { aboutInfo ->
            setupFragmentViewVisibility(aboutInfo)
            aboutInfo?.let {
                aboutAdapter.submitList(it)
            }
        }
    }

    private fun errorAction(event: CoreEvent.Error) {
        if (event.isNetworkError) requireActivity().toast(event.message)
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

    private fun setupFragmentViewVisibility(aboutInfo: List<About>?) {
        vb.run {
            if (aboutInfo.isNullOrEmpty())
                ivEmpty.show()
            else
                ivEmpty.gone()
        }
    }

}