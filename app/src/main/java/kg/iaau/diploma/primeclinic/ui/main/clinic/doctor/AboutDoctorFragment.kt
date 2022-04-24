package kg.iaau.diploma.primeclinic.ui.main.clinic.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Doctor
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutDoctorBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.EducationAdapter

@AndroidEntryPoint
class AboutDoctorFragment : CoreFragment<FragmentAboutDoctorBinding, ClinicVM>(ClinicVM::class.java) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAboutDoctorBinding
        get() = FragmentAboutDoctorBinding::inflate

    private val args: AboutDoctorFragmentArgs by navArgs()
    private val adapter = EducationAdapter()
    private val id: Long by lazy { args.id }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getDoctorProfileById(id)
    }

    override fun setupFragmentView() {
        vb.run {
            toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
            rvEducation.adapter = adapter
            btnMakeAppointment.setOnClickListener {
                findNavController().navigate(
                    R.id.nav_date,
                    Bundle().apply {
                        putLong("id", id)
                    }
                )
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        vm.doctorLiveData.observe(viewLifecycleOwner) { doctor ->
            setupDoctorView(doctor)
        }
    }

    private fun setupDoctorView(doctor: Doctor?) {
        vb.run {
            tvName.text = getString(R.string.full_name, doctor?.lastName, doctor?.firstName, doctor?.patronymic)
            tvPosition.text = doctor?.position
            tvBio.text = doctor?.bio ?: getString(R.string.absent_information)
            if (!doctor?.image.isNullOrEmpty())
                ivProfile.setImageDrawable(doctor?.image?.convertBase64ToDrawable(requireContext(), R.drawable.shape_filled_dot))
            if (!doctor?.information.isNullOrEmpty()) {
                adapter.submitList(doctor?.information)
                tvEducation.show()
                rvEducation.show()
            }
        }
    }

    override fun errorAction(event: CoreEvent.Error) {
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

}