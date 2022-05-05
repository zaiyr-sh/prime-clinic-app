package kg.iaau.diploma.primeclinic.ui.main.med_card.bottom_sheet

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.core.constants.MIMETYPE_IMAGES
import kg.iaau.diploma.core.ui.BaseBottomSheetFragment
import kg.iaau.diploma.core.utils.RealPath
import kg.iaau.diploma.core.utils.getUriForFile
import kg.iaau.diploma.primeclinic.databinding.FragmentProfilePictureBottomSheetBinding

@AndroidEntryPoint
class ProfilePictureBottomSheetFragment : BaseBottomSheetFragment<FragmentProfilePictureBottomSheetBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfilePictureBottomSheetBinding
        get() = FragmentProfilePictureBottomSheetBinding::inflate

    private lateinit var onItemSelected: (Uri, String?) -> Unit

    private var imageUri: Uri? = null
    private var imagePath: String? = null

    private lateinit var pickImage: ActivityResultLauncher<String>
    private lateinit var takeImage: ActivityResultLauncher<Uri>
    private lateinit var requestPickImagePermission: ActivityResultLauncher<String>
    private lateinit var requestTakeImagePermissions: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPickingImageContract()
        setupTakingImageContract()
    }

    private fun setupPickingImageContract() {
        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            contentUri?.let { uri ->
                imagePath = RealPath(requireContext()).getPath(uri)
                onItemSelected(uri, imagePath)
            }
            dismiss()
        }
        requestPickImagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) pickImage.launch(MIMETYPE_IMAGES)
            else dismiss()
        }
    }

    private fun setupTakingImageContract() {
        takeImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                imageUri?.let { uri -> onItemSelected(uri, imagePath) }
                dismiss()
            }
        }
        requestTakeImagePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    dismiss()
                    return@registerForActivityResult
                }
            }
            takeImage()
        }
    }

    override fun setupFragmentView() {
        vb.btnPickGallery.setOnClickListener {
            requestPickImagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        vb.btnTakePicture.setOnClickListener {
            requestTakeImagePermissions.launch(permissions)
        }
    }

    private fun takeImage() {
        imageUri = requireContext().getUriForFile { path ->
            imagePath = path
        }
        takeImage.launch(imageUri)
    }

    companion object {
        private val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        private val bottomSheet = ProfilePictureBottomSheetFragment()
        fun show(supportFragmentManager: FragmentManager, onItemSelected: (uri: Uri, path: String?) -> Unit) {
            bottomSheet.onItemSelected = onItemSelected
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

}