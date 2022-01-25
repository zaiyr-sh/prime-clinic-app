package kg.iaau.diploma.primeclinic.ui.main.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.data.About
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutBinding
import kg.iaau.diploma.primeclinic.repository.AboutAdapter
import kg.iaau.diploma.primeclinic.repository.AboutListener

@AndroidEntryPoint
class AboutFragment : Fragment(), AboutListener {

    private lateinit var vb: FragmentAboutBinding
    private val vm: AboutVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private var aboutAdapter = AboutAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getInfoAboutUs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentAboutBinding.inflate(inflater, container, false)
        setupFragmentView()
        observeLiveData()
        return vb.root
    }

    private fun setupFragmentView() {
        vb.run {
            rvAbout.adapter = aboutAdapter
        }
    }

    private fun observeLiveData() {
        vm.aboutInfoLiveData.observe(viewLifecycleOwner, { aboutInfo ->
            aboutInfo?.let {
                setupFragmentViewVisibility()
                aboutAdapter.submitList(it)
            }
        })
    }

    private fun setupFragmentViewVisibility() {
        vb.run {
            progressBar.gone()
            ivEmpty.show()
        }
    }

    override fun onAboutClick(about: About) {
        TODO("Not yet implemented")
    }

}