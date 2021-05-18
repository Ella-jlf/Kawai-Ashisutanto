package android.ella.assistant.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.ella.assistant.R
import android.ella.assistant.databinding.FragmentEditBinding
import android.ella.assistant.entity.Assistant
import android.ella.assistant.entity.RequestCode
import android.ella.assistant.viewmodel.ListViewModel
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.firebase.storage.internal.Sleeper

class EditFragment : Fragment(), View.OnClickListener {

    private lateinit var mBinding: FragmentEditBinding
    private var mBitmap : Bitmap? = null

    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEditBinding.inflate(layoutInflater)



        fill()
        mBinding.editBtnEdit.setOnClickListener (this)
        mBinding.editBtnBack.setOnClickListener(this)
        mBinding.editImage.setOnClickListener(this)



        return mBinding.root
    }



    private fun checkPermissions(permissionType: Int): Boolean {
        when (permissionType) {
            RequestCode.READ_EXTERNAL_STORAGE -> {
                if (
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    return true


                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        (arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)),
                        RequestCode.READ_EXTERNAL_STORAGE
                    )
                    return false
                }
            }
        }
        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            when (requestCode) {
                RequestCode.IMAGE -> {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            data.data!!
                        )
                    } else {
                        val source =
                            ImageDecoder.createSource(requireContext().contentResolver, data.data!!)
                        ImageDecoder.decodeBitmap(source)
                    }
                    mBinding.editImage.setImageBitmap(bitmap)
                    mBitmap = bitmap
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestCode.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Access granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Fuck u", Toast.LENGTH_SHORT).show()
        }

    }

    private fun back(){
        parentFragmentManager.popBackStack()
    }

    private fun fill(){
        val pos =viewModel.assistantPos.value
        if ( pos != null){
            mBinding.editImage.setImageBitmap(viewModel.getAssistants()[pos].imageBitmap)
            mBinding.editName.setText(viewModel.getAssistants()[pos].name)
            mBinding.editDescription.setText( viewModel.getAssistants()[pos].description)
            mBitmap = viewModel.getAssistants()[pos].imageBitmap
        }

    }

    override fun onClick(v: View?) {
        when (v){
            mBinding.editBtnBack ->{
                back()
            }
            mBinding.editImage ->{
                while (!checkPermissions(RequestCode.READ_EXTERNAL_STORAGE)){

                }
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                startActivityForResult(intent,RequestCode.IMAGE)
            }
            mBinding.editBtnEdit ->{
                if (mBinding.editName.text.isEmpty() || mBinding.editDescription.text.isEmpty() || mBitmap == null) {
                    Toast.makeText(context, "Fill fields", Toast.LENGTH_SHORT).show()
                } else {
                    val nAssist = Assistant(
                        name = mBinding.editName.text.toString(),
                        description = mBinding.editDescription.text.toString(),
                        imageBitmap = mBitmap,
                        imageName = ListViewModel.uploadImage(mBitmap!!)
                    )

                    viewModel.editAssistant(nAssist)
                    back()
                }
            }

        }
    }


}