package kg.iaau.diploma.primeclinic.ui.main.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.ui.CoreFragment
import kg.iaau.diploma.core.utils.*
import kg.iaau.diploma.data.Faq
import kg.iaau.diploma.primeclinic.databinding.FragmentFaqBinding
import kg.iaau.diploma.primeclinic.ui.main.faq.adapter.FaqAdapter

@AndroidEntryPoint
class FaqFragment : CoreFragment<FragmentFaqBinding, FaqVM>(FaqVM::class.java) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFaqBinding
        get() = FragmentFaqBinding::inflate

    private var adapter = FaqAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getFaq()
    }

    override fun setupFragmentView() {
        vb.run {
            rvFaq.adapter = adapter
            swipeToRefresh.setOnRefreshListener { vm.getFaq() }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        vm.faqLiveData.observe(viewLifecycleOwner) { faqs ->
            setupFragmentViewVisibility(faqs)
            faqs?.let {
                adapter.submitList(it)
            }
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