package android.ella.assistant.adapter

import android.content.Context
import android.ella.assistant.R
import android.ella.assistant.entity.Assistant
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(context: Context, list: ArrayList<Assistant>?) : RecyclerView.Adapter<ListViewHolder>() {

    private val mContext = context
    private val mList  = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val rowInflater = LayoutInflater.from(mContext)
        val view = rowInflater.inflate(R.layout.row_list,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (!mList.isNullOrEmpty()){
            holder.nameView.text = mList[position].name
            if (mList[position].imageBitmap != null){
                holder.imageView.setImageBitmap(mList[position].imageBitmap)
            }
            holder.itemView.setOnClickListener {
                Toast.makeText(mContext,"Item Clicked $position",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
       return mList?.size ?: 0
    }

}