package pavel.ivanov.pictureoftheday.ui.drawer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pavel.ivanov.pictureoftheday.R
import pavel.ivanov.pictureoftheday.databinding.BottomNavigatorLayoutBinding
import pavel.ivanov.pictureoftheday.ui.adapter.list.RecyclerAdapterImpl

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigatorLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.navigation_one -> {
                    val intent = Intent(context, RecyclerAdapterImpl::class.java)
                    startActivity(intent)
                }
                R.id.navigation_two -> {
                    Toast.makeText(context,"2",Toast.LENGTH_SHORT).show()
                }
            }

            true
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigatorLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

}