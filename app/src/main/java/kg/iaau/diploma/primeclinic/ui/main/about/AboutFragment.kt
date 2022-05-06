package kg.iaau.diploma.primeclinic.ui.main.about

import android.os.Bundle
import android.view.*
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.About
import kg.iaau.diploma.primeclinic.MainActivity
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutBinding
import kg.iaau.diploma.primeclinic.ui.authorization.AuthorizationActivity
import kg.iaau.diploma.primeclinic.ui.main.about.adapter.AboutAdapter

@AndroidEntryPoint
class AboutFragment : CoreFragment<FragmentAboutBinding, AboutVM>(AboutVM::class.java) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutBinding
        get() = FragmentAboutBinding::inflate

    private var adapter = AboutAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getInfoAboutUs()
    }

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
                context?.showDialog(R.string.exit_confirmation, { logout() })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        vm.logout()
        activity?.finishAffinity()
        AuthorizationActivity.startActivity(requireContext())
    }

    override fun setupFragmentView() {
        vb.run {
            rvAbout.adapter = adapter
            swipeToRefresh.setOnRefreshListener { vm.getInfoAboutUs() }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        vm.aboutInfoLiveData.observe(viewLifecycleOwner) { aboutInfo ->
            setupAboutInfo(aboutInfo)
        }
    }

    private fun setupAboutInfo(aboutInfo: List<About>?) {
        when(aboutInfo.isNullOrEmpty()) {
            true -> hideAboutInfo()
            else -> showAboutInfo(aboutInfo)
        }
    }

    private fun hideAboutInfo() {
        vb.run {
            ivEmpty.show()
            rvAbout.hide()
        }
    }

    private fun showAboutInfo(aboutInfo: List<About>) {
        vb.run {
            adapter.submitList(aboutInfo)
            ivEmpty.gone()
            rvAbout.show()
        }
    }

    override fun errorAction(event: CoreEvent.Error) {
        super.errorAction(event)
        vb.ivEmpty.show()
    }

    override fun showLoader() {
        if(!vb.swipeToRefresh.isRefreshing)
            super.showLoader()
        vb.clContainer.run {
            setAnimateAlpha(0.5f)
            setEnable(false)
        }
    }

    override fun goneLoader() {
        super.goneLoader()
        with(vb) {
            clContainer.run {
                setAnimateAlpha(1f)
                setEnable(true)
            }
            swipeToRefresh.isRefreshing = false
        }
    }

}