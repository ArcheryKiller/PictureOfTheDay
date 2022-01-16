package pavel.ivanov.pictureoftheday.repository.recyclerlist

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback (
    private var oldItems: List<Pair<Data, Boolean>>,
    private var newItems: List<Pair<Data, Boolean>>
) : DiffUtil.Callback() {
    override fun getOldListSize()= oldItems.size

    override fun getNewListSize()= newItems.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem.first.someText==newItem.first.someText
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return Change(
            oldItem,
            newItem
        )
    }



}