package android.ella.assistant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.ella.assistant.R
import android.ella.assistant.databinding.FragmentRepresentBinding
import android.ella.assistant.viewmodel.ListViewModel
import androidx.fragment.app.activityViewModels

class RepresentFragment : Fragment(), View.OnClickListener {

    private lateinit var mBinding:FragmentRepresentBinding

    private val viewModel: ListViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentRepresentBinding.inflate(layoutInflater)

        fill()


        mBinding.representBtnBack.setOnClickListener(this)
        mBinding.representBtnEdit.setOnClickListener(this)
        mBinding.representBtnRemove.setOnClickListener(this)

        return mBinding.root
    }



    private fun fill(){
        val pos =viewModel.assistantPos.value
        if ( pos != null){
            mBinding.representImage.setImageBitmap(viewModel.getAssistants()[pos].imageBitmap)
            mBinding.representName.text = viewModel.getAssistants()[pos].name
            mBinding.representDescription.text = viewModel.getAssistants()[pos].description
        }

    }

    private fun back(){
        parentFragmentManager.popBackStack()
    }

    override fun onClick(v: View?) {
        when(v) {
            mBinding.representBtnBack -> {
                back()
            }
            mBinding.representBtnEdit ->{

            }
            mBinding.representBtnRemove -> {
                viewModel.removeAssistant()
                back()
            }
        }
    }
}