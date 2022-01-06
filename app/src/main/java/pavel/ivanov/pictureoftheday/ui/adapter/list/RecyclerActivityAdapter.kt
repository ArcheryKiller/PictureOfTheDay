package pavel.ivanov.pictureoftheday.ui.adapter.list

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pavel.ivanov.pictureoftheday.databinding.ActivityRecyclerItemEarthBinding
import pavel.ivanov.pictureoftheday.databinding.ActivityRecyclerItemHeaderBinding
import pavel.ivanov.pictureoftheday.databinding.ActivityRecyclerItemMarsBinding
import pavel.ivanov.pictureoftheday.repository.recyclerlist.*
import pavel.ivanov.pictureoftheday.viewmodel.BaseViewHolder

class RecyclerActivityAdapter(
    private val data: MutableList<Pair<Data, Boolean>>,
    private val callbackListener: MyCallback,
    private val onStartDragListener: OnStartDragListener
) : RecyclerView.Adapter<BaseViewHolder>(),ItemTouchHelperAdapter {

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }


    fun setItems(newItems: List<Pair<Data, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }


    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem(): Pair<Data, Boolean> {
        return Data((0..9999999).random(),someText = "Mars") to false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_EARTH -> {
                val bindingViewHolder = ActivityRecyclerItemEarthBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EarthViewHolder(bindingViewHolder.root)
            }
            TYPE_HEADER -> {
                val bindingViewHolder = ActivityRecyclerItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(bindingViewHolder.root)
            }
            else -> {
                val bindingViewHolder = ActivityRecyclerItemMarsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MarsViewHolder(bindingViewHolder.root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].first.type
    }

    /*override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as MutableList<Change<Pair<Data, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData
            if (newData.first.someText != oldData.first.someText) {
                ActivityRecyclerItemMarsBinding.bind(holder.itemView).marsTextView.text = newData.first.someText
            }
        }
    }*/

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int,payloads: MutableList<Any>) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            val combinedChange =
                createCombinedPayload(payloads as MutableList<Change<Pair<Data, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData
            Log.d("mylogs","${(1..9999999).random()} ${
                newData.first.someText!=oldData.first.someText
            }")


            ActivityRecyclerItemMarsBinding.bind(holder.itemView).someTextTextView.text = newData.first.someText

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class EarthViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<Data, Boolean>) {
            ActivityRecyclerItemEarthBinding.bind(itemView).apply {
                someTextTextView.text = data.first.someText
                descriptionTextView.text = data.first.someDescription
                wikiImageView.setOnClickListener {
                    callbackListener.onClick(layoutPosition)
                }
            }
        }
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view),ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Data, Boolean>) {
            ActivityRecyclerItemMarsBinding.bind(itemView).apply {
                someTextTextView.text = data.first.someText
                marsImageView.setOnClickListener {
                    callbackListener.onClick(layoutPosition)
                }
                addItemImageView.setOnClickListener {
                    addItemToPosition()
                }
                removeItemImageView.setOnClickListener {
                    removeItem()
                }
                moveItemDown.setOnClickListener {
                    moveDown()
                }
                moveItemUp.setOnClickListener {
                    moveUp()
                }
                marsDescriptionTextView.visibility = if(data.second) View.VISIBLE else View.GONE
                someTextTextView.setOnClickListener {
                    toggleDescription()
                }

                dragHandleImageView.setOnTouchListener{v, event->
                    Log.d("mylogs","setOnTouchListener $event")
                    if(MotionEventCompat.getActionMasked(event)== MotionEvent.ACTION_DOWN){
                        onStartDragListener.onStartDrag(this@MarsViewHolder)
                    }
                    false
                }
            }
        }

        private fun toggleDescription() {
            data[layoutPosition] = data[layoutPosition].run {
                first to !second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            if (layoutPosition > 0) {
                data.removeAt(layoutPosition).apply {
                    data.add(layoutPosition - 1, this)
                }
            }
            notifyItemMoved(layoutPosition, layoutPosition - 1)
        }

        private fun moveDown() {
            if (layoutPosition < data.size -1) {
                data.removeAt(layoutPosition).apply {
                    data.add(layoutPosition + 1, this)
                }
            }
            notifyItemMoved(layoutPosition, layoutPosition + 1)
        }

        private fun addItemToPosition() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.CYAN)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }


    }


    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<Data, Boolean>) {
            ActivityRecyclerItemHeaderBinding.bind(itemView).apply {
                header.text = data.first.someText
                root.setOnClickListener {
                    callbackListener.onClick(layoutPosition)
                }
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(toPosition, this)
        }
        notifyItemMoved(fromPosition,toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}