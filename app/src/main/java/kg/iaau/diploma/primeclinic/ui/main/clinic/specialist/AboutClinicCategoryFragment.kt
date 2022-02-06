package kg.iaau.diploma.primeclinic.ui.main.clinic.specialist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.CoreEvent.*
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutClinicCategoryBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DoctorAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DoctorListener

@AndroidEntryPoint
class AboutClinicCategoryFragment : Fragment(), DoctorListener {

    private lateinit var vb: FragmentAboutClinicCategoryBinding
    private val args: AboutClinicCategoryFragmentArgs by navArgs()
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private var adapter = DoctorAdapter(this)
    private val id: Long by lazy { args.id }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getSpecialistsCategoryDetailInfo(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentAboutClinicCategoryBinding.inflate(inflater, container, false)
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
            toolbar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack()  }
        }
    }

    private fun observeLiveData() {
        vm.specialistLiveData.observe(viewLifecycleOwner, { specialist ->
            setupSpecialistView(specialist)
        })
        vm.event.observe(this, { event ->
            when(event) {
                is Loading -> showLoader()
                is Success -> goneLoader()
                is Error -> errorAction(event)
            }
        })
    }

    private fun setupSpecialistView(specialist: SpecialistCategory?) {
        vb.run {
            if (specialist != null) {
                toolbar.show()
                clDescription.show()
            }
            tvCategory.text = specialist?.name
            tvPositionDescription.text = specialist?.description
            Glide.with(requireContext()).load(specialist?.image)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                        toolbar.background = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            adapter.submitList(specialist?.doctors)
        }
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

    override fun onDoctorClick(id: Long?) {
        val args = Bundle()
        if (id != null) { args.putLong("id", id) }
        view?.findNavController()?.navigate(R.id.nav_about_doctor, args)
    }

}