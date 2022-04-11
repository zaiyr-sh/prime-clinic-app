package kg.iaau.diploma.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kg.iaau.diploma.core.constants.AUTH_ERROR
import kg.iaau.diploma.core.utils.CoreEvent
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.core.vm.CoreVM

abstract class CoreFragment<VB: ViewBinding, VM: CoreVM>(
    private val mViewModelClass: Class<VM>
) : Fragment() {

    private var _vb: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val vb: VB
        get() = _vb as VB

    protected val vm by lazy {
        ViewModelProvider(this)[mViewModelClass]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_vb).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
        observeLiveData()
    }

    abstract fun setupFragmentView()

    open fun observeLiveData() {
        vm.event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> successAction()
                is CoreEvent.Error -> errorAction(event)
                is CoreEvent.Notification -> notificationAction()
            }
        }
    }

    open fun showLoader() {
        LoadingScreen.showLoading(requireActivity())
    }

    open fun successAction() {
        goneLoader()
    }

    open fun notificationAction() {}

    open fun errorAction(event: CoreEvent.Error) {
        if (event.isNetworkError) requireActivity().toast(event.message)
        goneLoader()
    }

    open fun goneLoader() {
        LoadingScreen.hideLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }

}