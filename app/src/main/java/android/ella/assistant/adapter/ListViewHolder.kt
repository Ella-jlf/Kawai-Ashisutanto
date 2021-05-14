package android.ella.assistant.adapter

import android.ella.assistant.R
import android.ella.assistant.databinding.RowListBinding
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView : CircleImageView = itemView.findViewById(R.id.row_image)
    val nameView : TextView = itemView.findViewById(R.id.row_name)
    init {

    }
}