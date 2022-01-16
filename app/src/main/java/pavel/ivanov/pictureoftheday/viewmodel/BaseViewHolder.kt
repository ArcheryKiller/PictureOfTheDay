package pavel.ivanov.pictureoftheday.viewmodel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import pavel.ivanov.pictureoftheday.repository.recyclerlist.Data

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: Pair<Data,Boolean>)
}