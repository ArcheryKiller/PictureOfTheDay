package pavel.ivanov.pictureoftheday.ui.picture.fragmentsoftheday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import pavel.ivanov.pictureoftheday.R
import pavel.ivanov.pictureoftheday.databinding.FragmentOfTheDayBinding
import pavel.ivanov.pictureoftheday.viewmodel.PictureOfTheDayState
import pavel.ivanov.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*

class TodayFragment : Fragment() {
    private var _binding: FragmentOfTheDayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sendServerRequest(takeDate(0))

        viewModel.getData().observe(viewLifecycleOwner, {
            renderData(it)
        })
    }

    private fun renderData(state: PictureOfTheDayState) {
        when(state) {
            is PictureOfTheDayState.Error -> {

                Snackbar
                    .make(binding.root, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getData() }
                    .show()
            }
            is PictureOfTheDayState.Loading -> {


            }
            is PictureOfTheDayState.Success -> {

                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData
                val url = pictureOfTheDayResponseData.url
                val title = pictureOfTheDayResponseData.title
                val date = pictureOfTheDayResponseData.date
                val description = pictureOfTheDayResponseData.explanation


                binding.imageView.load(url) {
                    lifecycle(this@TodayFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
                binding.titleView.text = title + "\n" + date
            }
        }
    }

    private fun takeDate(count: Int): String {
        val currentDate = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (count == 0) {
            return "null"
        }

        currentDate.add(Calendar.DAY_OF_MONTH, count)
        format.timeZone = TimeZone.getTimeZone("EST")

        return format.format(currentDate.time)
    }
}