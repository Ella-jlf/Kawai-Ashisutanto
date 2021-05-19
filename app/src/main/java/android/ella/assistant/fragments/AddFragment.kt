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
import android.ella.assistant.databinding.FragmentAddBinding
import android.ella.assistant.entity.Assistant
import android.ella.assistant.entity.RequestCode
import android.ella.assistant.viewmodel.ListViewModel
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels


class AddFragment : Fragment() {

    private lateinit var mBinding: FragmentAddBinding
    private var mBitmap: Bitmap? = null
    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAddBinding.inflate(layoutInflater, container, false)




        mBinding.addBtnAdd.setOnClickListener {
            if (mBinding.addName.text.isEmpty() || mBinding.addDescription.text.isEmpty() || mBitmap == null) {
                Toast.makeText(context, "Fill fields", Toast.LENGTH_SHORT).show()
            } else {
                val nAssist = Assistant(
                    name = mBinding.addName.text.toString(),
                    description = mBinding.addDescription.text.toString(),
                    imageBitmap = mBitmap
                )
                viewModel.addAssistant(nAssist)
                back()
            }
        }


        mBinding.addBtnBack.setOnClickListener {
            back()
        }

        mBinding.addImage.setOnClickListener {
            while (!checkPermissions(RequestCode.READ_EXTERNAL_STORAGE)) {
            }
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(intent, RequestCode.IMAGE)
        }

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
                    mBinding.addImage.setImageBitmap(bitmap)
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

}


































