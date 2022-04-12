package kg.iaau.diploma.primeclinic.ui.main.clinic.specialist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.CoreEvent.Error
import kg.iaau.diploma.core.utils.setAnimateAlpha
import kg.iaau.diploma.core.utils.setEnable
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.data.SpecialistCategory
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutClinicCategoryBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DoctorAdapter
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.DoctorListener

@AndroidEntryPoint
class AboutClinicCategoryFragment : CoreFragment<FragmentAboutClinicCategoryBinding, ClinicVM>(ClinicVM::class.java), DoctorListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutClinicCategoryBinding
        get() = FragmentAboutClinicCategoryBinding::inflate

    private val args: AboutClinicCategoryFragmentArgs by navArgs()
    private var adapter = DoctorAdapter(this)
    private val id: Long by lazy { args.id }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getSpecialistsCategoryDetailInfo(id)
    }

    override fun setupFragmentView() {
        vb.run {
            rvSpecialists.adapter = adapter
            toolbar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack()  }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        vm.specialistLiveData.observe(viewLifecycleOwner) { specialist ->
            setupSpecialistView(specialist)
        }
    }

    private fun setupSpecialistView(specialist: SpecialistCategory?) {
        vb.run {
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

    override fun errorAction(event: Error) {
        super.errorAction(event)
        if (!event.isNetworkError) requireActivity().toast(getString(R.string.unexpected_error))
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

    override fun onDoctorClick(id: Long?) {
        findNavController().navigate(
            R.id.nav_about_doctor,
            Bundle().apply {
                id?.let { putLong("id", it) }
            }
        )
    }

}