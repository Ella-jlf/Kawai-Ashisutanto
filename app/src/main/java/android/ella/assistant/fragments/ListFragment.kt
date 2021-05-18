package android.ella.assistant.fragments

import android.ella.assistant.R
import android.ella.assistant.adapter.ListAdapter
import android.ella.assistant.adapter.RecyclerItemClickListener
import android.ella.assistant.databinding.FragmentListBinding
import android.ella.assistant.viewmodel.ListViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager


class ListFragment : Fragment() {

    private lateinit var mBinding: FragmentListBinding
    private val viewModel: ListViewModel by activityViewModels()
    private lateinit var adapter :ListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentListBinding.inflate(inflater, container, false)


        viewModel.getAssistantsLiveData().observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
        })

        adapter = ListAdapter(requireContext(), viewModel.list)
        mBinding.recycleList.let {
        it.adapter = adapter
        it.layoutManager = GridLayoutManager(requireContext(), 3)
        }

        mBinding.recycleList.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                mBinding.recycleList,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        viewModel.assistantPos.value = position
                        parentFragmentManager.commit{
                            replace<RepresentFragment>(R.id.fragment_container)
                            setReorderingAllowed(true)
                            addToBackStack(null)
                        }
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // do whatever
                    }
                })
        )



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


























