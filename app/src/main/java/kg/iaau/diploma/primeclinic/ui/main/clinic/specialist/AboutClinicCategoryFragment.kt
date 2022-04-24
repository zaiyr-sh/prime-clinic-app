package kg.iaau.diploma.primeclinic.ui.main.clinic.specialist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.core.utils.CoreEvent.Error
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
            toolbar.background = specialist?.image?.convertBase64ToDrawable(requireContext(), R.drawable.ic_specialist)
            if (!specialist?.doctors.isNullOrEmpty()) {
                adapter.submitList(specialist?.doctors)
                tvOurSpecialists.show()
                rvSpecialists.show()
            }
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