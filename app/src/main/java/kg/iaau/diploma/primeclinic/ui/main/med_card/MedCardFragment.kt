package kg.iaau.diploma.primeclinic.ui.main.med_card

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.MedCard
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentMedCardBinding

@AndroidEntryPoint
class MedCardFragment : Fragment() {

    private lateinit var vb: FragmentMedCardBinding
    private val vm: MedCardVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentMedCardBinding.inflate(inflater, container, false)
        vm.getMedCard()
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.run {
            llAddMedCard.setOnClickListener { openAddMedCardFragment() }
            ibEdit.setOnClickListener { openAddMedCardFragment(true) }
        }
    }

    private fun openAddMedCardFragment(isAgreementAccepted: Boolean = false) {
        findNavController().navigate(
            R.id.nav_add_med_card,
            Bundle().apply {
                putBoolean("isAgreementAccepted", isAgreementAccepted)
            }
        )
    }

    private fun observeLiveData() {
        vm.medCardLiveData.observe(viewLifecycleOwner) { medCard ->
            medCard?.let {
                setupMedCardFields(medCard)
            }
        }
        vm.imageUriLiveData.observe(viewLifecycleOwner) { imageUri ->
            imageUri?.let {
                Glide.with(requireContext()).load(Uri.parse(it)).into(vb.ivUser)
            }
        }
        vm.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Error -> errorAction(event)
            }
        }
    }

    private fun setupMedCardFields(medCard: MedCard) {
        vb.run {
            cvMedCard.show()
            llAddMedCard.gone()
            medCard.image?.let { image ->
                Glide.with(requireContext()).load(Uri.parse(image)).into(ivUser)
            }
            tvName.text = getString(R.string.full_name, medCard.lastName, medCard.firstName, medCard.patronymic)
            tvBirthday.text = getString(R.string.birth_date, medCard.birthDate)
            medCard.medCardPhoneNumber.let {
                if(it.isNullOrEmpty())
                    tvPhone.text = getString(R.string.whatsapp_number, getString(R.string.undefined_phone_number))
                else
                    tvPhone.text = getString(R.string.whatsapp_number, it)
            }
        }
    }

    private fun errorAction(event: CoreEvent.Error) {
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

}