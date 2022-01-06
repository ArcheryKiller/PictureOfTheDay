package pavel.ivanov.pictureoftheday.ui.adapter.list

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}