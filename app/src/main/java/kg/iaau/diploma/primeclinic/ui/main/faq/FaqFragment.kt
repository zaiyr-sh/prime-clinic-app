package kg.iaau.diploma.primeclinic.ui.main.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Faq
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentFaqBinding
import kg.iaau.diploma.primeclinic.ui.main.faq.adapter.FaqAdapter

@AndroidEntryPoint
class FaqFragment : Fragment() {

    private lateinit var vb: FragmentFaqBinding
    private val vm: FaqVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }
    private var adapter = FaqAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getFaq()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentFaqBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    private fun setupFragmentView() {
        vb.rvFaq.adapter = adapter
    }

    private fun observeLiveData() {
        vm.event.observe(this, { event ->
            when(event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> goneLoader()
                is CoreEvent.Error -> errorAction(event)
            }
        })
        vm.faqLiveData.observe(viewLifecycleOwner, { faqs ->
            setupFragmentViewVisibility(faqs)
            faqs?.let {
                adapter.submitList(it)
            }
        })
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

    private fun setupFragmentViewVisibility(faqs: List<Faq>?) {
        vb.run {
            if (faqs.isNullOrEmpty())
                ivEmpty.show()
            else
                ivEmpty.gone()
        }
    }

}