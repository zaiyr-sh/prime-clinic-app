package kg.iaau.diploma.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import kg.iaau.diploma.core.constants.AUTH_ERROR
import kg.iaau.diploma.core.utils.CoreEvent
import kg.iaau.diploma.core.utils.toast
import kg.iaau.diploma.core.vm.CoreVM

abstract class CoreActivity<VB: ViewBinding, VM: CoreVM>(
    private val mViewModelClass: Class<VM>
) : AppCompatActivity() {

    private var _vb: ViewBinding? = null
    abstract val inflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val vb: VB
        get() = _vb as VB

    protected val vm by lazy {
        ViewModelProvider(this)[mViewModelClass]
    }

    protected lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _vb = inflater.invoke(layoutInflater)
        setContentView(requireNotNull(_vb).root)
        setupActivityView()
        observeLiveData()
    }

    abstract fun setupActivityView()

    open fun observeLiveData() {
        vm.event.observe(this) { event ->
            when (event) {
                is CoreEvent.Loading -> showLoader()
                is CoreEvent.Success -> successAction()
                is CoreEvent.Error -> errorAction(event)
                is CoreEvent.Notification -> notificationAction()
            }
        }
    }

    open fun showLoader() {
        LoadingScreen.showLoading(this)
    }

    open fun successAction() {
        goneLoader()
    }

    open fun notificationAction() {}

    open fun errorAction(event: CoreEvent.Error) {
        when (event.isNetworkError) {
            true -> toast(event.message)
            false -> toast(AUTH_ERROR)
        }
        goneLoader()
    }

    open fun goneLoader() {
        LoadingScreen.hideLoading()
    }

    open fun initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }

}