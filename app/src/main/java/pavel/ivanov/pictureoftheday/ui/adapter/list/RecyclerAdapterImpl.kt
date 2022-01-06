package pavel.ivanov.pictureoftheday.ui.adapter.list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pavel.ivanov.pictureoftheday.databinding.ActivityRecyclerListBinding
import pavel.ivanov.pictureoftheday.repository.recyclerlist.*

class RecyclerAdapterImpl : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerListBinding
    lateinit var itemTouchHelper: ItemTouchHelper

    private var isNewList = false
    private lateinit var adapter: RecyclerActivityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = arrayListOf(
            Data(0,"Earth", type = TYPE_EARTH) to false,
            Data(1,"Mars", "", type = TYPE_MARS) to false/*,
            Data("Earth", type = TYPE_EARTH),
            Data("Mars", "", type = TYPE_MARS),
            Data("Earth", type = TYPE_EARTH),
            Data("Earth", type = TYPE_EARTH),
            Data("Earth", type = TYPE_EARTH),
            Data("Mars", null, type = TYPE_MARS)*/
        )
        data.add(0, Data(3,"Заголовок", type = TYPE_HEADER) to false)

        val lat  = 55
        val lon  = 37
        val coordinate1 = lat to lon
        val coordinate2 = Pair(lat,lon)
        coordinate1.first
        coordinate1.second
        val coordinate3d = Triple(1,2,3)
        coordinate3d.first
        coordinate3d.second
        coordinate3d.third

        adapter = RecyclerActivityAdapter(data,
            object : MyCallback {
                override fun onClick(position: Int) {
                    Toast.makeText(
                        this@RecyclerAdapterImpl,
                        "РАБОТАЕТ ${data[position].first.someText} ${data[position].first.someDescription}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, object : RecyclerActivityAdapter.OnStartDragListener{
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }

        )
        binding.recyclerView.adapter = adapter
        binding.recyclerActivityDiffUtilFAB.setOnClickListener {
            adapter.setItems(createItemList(isNewList).map { it })
            isNewList = !isNewList
        }
        binding.recyclerActivityFAB.setOnClickListener {
            adapter.appendItem()
        }
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<Data, Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(Data(0, "Header"), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Mars", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Mars", ""), false),
                Pair(Data(5, "Mars", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
            true -> listOf(
                Pair(Data(0, "Header"), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Jupiter", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Neptune", ""), false),
                Pair(Data(5, "Saturn", ""), false),
                Pair(Data(7, "Mars", ""), false)
            )
        }
    }


}