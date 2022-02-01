package kg.iaau.diploma.primeclinic.ui.main.clinic.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import kg.iaau.diploma.core.utils.convertBase64ToDrawable
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.show
import kg.iaau.diploma.data.Doctor
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentAboutDoctorBinding
import kg.iaau.diploma.primeclinic.ui.main.clinic.ClinicVM
import kg.iaau.diploma.primeclinic.ui.main.clinic.adapter.EducationAdapter

class AboutDoctorFragment : Fragment() {

    private lateinit var vb: FragmentAboutDoctorBinding
    private val args: AboutDoctorFragmentArgs by navArgs()
    private val vm: ClinicVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private val adapter = EducationAdapter()
    private val id: Long by lazy { args.id }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getDoctorProfileById(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentAboutDoctorBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.run {
            toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }
            rvEducation.adapter = adapter
        }
    }

    private fun observeLiveData() {
        vm.doctorLiveData.observe(viewLifecycleOwner, { doctor ->
            setupDoctorView(doctor)
        })
    }

    private fun setupDoctorView(doctor: Doctor?) {
        vb.run {
            if(doctor != null) {
                progressBar.gone()
            }
            tvName.text = getString(R.string.full_name, doctor?.lastName, doctor?.firstName, doctor?.patronymic)
            tvPosition.text = doctor?.position
            tvBio.text = doctor?.bio
            if (!doctor?.image.isNullOrEmpty())
                ivProfile.setImageDrawable(doctor?.image?.convertBase64ToDrawable(requireContext()))
            adapter.submitList(doctor?.information)
        }
    }

}