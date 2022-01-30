package kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kg.iaau.diploma.core.constants.MIMETYPE_IMAGES
import kg.iaau.diploma.primeclinic.BuildConfig
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentProfilePictureBottomSheetBinding
import kg.iaau.diploma.primeclinic.ui.main.med_card.MedCardVM
import java.io.File

class ProfilePictureBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var vb: FragmentProfilePictureBottomSheetBinding
    private val vm: MedCardVM by navGraphViewModels(R.id.main_navigation) { defaultViewModelProviderFactory }

    private var imageUri: Uri? = null

    private lateinit var pickImage: ActivityResultLauncher<String>

    private lateinit var takeImage: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            vm.setProfilePicture(contentUri.toString())
            dismiss()
        }
        takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                imageUri?.let { uri ->
                    vm.setProfilePicture(uri.toString())
                }
                dismiss()
            }
        }
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vb = FragmentProfilePictureBottomSheetBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.btnPickGallery.setOnClickListener {
            pickImage.launch(MIMETYPE_IMAGES)
        }
        vb.btnTakePicture.setOnClickListener {
            takeImage()
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getImageFileUri().let { uri ->
                imageUri = uri
                takeImage.launch(uri)
            }
        }
    }

    private fun getImageFileUri(): Uri {
        val file = File.createTempFile("profile_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", file)
    }

    companion object {
        private val bottomSheet = ProfilePictureBottomSheetFragment()
        fun show(supportFragmentManager: FragmentManager) {
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

}