package android.ella.assistant.fragments

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
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager

class ListFragment : Fragment() {

    private lateinit var mBinding: FragmentListBinding
    private val viewModel: ListViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentListBinding.inflate(inflater,container,false)


        viewModel.getAssistantsLiveData().observe(viewLifecycleOwner,{
            viewModel.getAdapter().notifyDataSetChanged()
        })

        mBinding.recycleList.let {
            viewModel.setAdapter(ListAdapter(requireContext(),viewModel.getAssistants()))
        it.adapter = viewModel.getAdapter()
        it.layoutManager = GridLayoutManager(requireContext(),3)
        }


        mBinding.listBtnAdd.setOnClickListener {
            parentFragmentManager.commit {
                replace<AddFragment>(R.id.fragment_container)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        return mBinding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}


























