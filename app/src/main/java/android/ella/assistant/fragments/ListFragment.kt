package android.ella.assistant.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.ella.assistant.R
import android.ella.assistant.adapter.ListAdapter
import android.ella.assistant.databinding.FragmentListBinding
import android.ella.assistant.viewmodel.ListViewModel
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager

class ListFragment : Fragment() {

    private lateinit var mBinding: FragmentListBinding
    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentListBinding.inflate(inflater,container,false)


        mBinding.recycleList.let {
        it.adapter = ListAdapter(requireContext(),viewModel.getAssistants())
        it.layoutManager = GridLayoutManager(requireContext(),3)
        }


        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}