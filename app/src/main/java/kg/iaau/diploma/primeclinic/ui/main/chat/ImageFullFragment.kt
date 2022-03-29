package kg.iaau.diploma.primeclinic.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.utils.gone
import kg.iaau.diploma.core.utils.loadWithGlide
import kg.iaau.diploma.primeclinic.databinding.FragmentImageFullBinding

@AndroidEntryPoint
class ImageFullFragment : Fragment() {

    private lateinit var vb: FragmentImageFullBinding
    private val args: ImageFullFragmentArgs by navArgs()
    private val image by lazy { args.image }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentImageFullBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentView()
    }

    private fun setupFragmentView() {
        vb.run {
            toolbar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            requireActivity().loadWithGlide(ivAvatar, image,
                onSuccess = {
                    progressBar.gone()
                },
                onFail = {
                    progressBar.gone()
                }
            )
        }
    }
}