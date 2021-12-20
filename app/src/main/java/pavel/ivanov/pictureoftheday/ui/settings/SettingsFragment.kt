package pavel.ivanov.pictureoftheday.ui.settings

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import pavel.ivanov.pictureoftheday.R
import pavel.ivanov.pictureoftheday.databinding.FragmentChipsBinding
import pavel.ivanov.pictureoftheday.ui.picture.PictureOfTheDayFragment


class SettingsFragment : Fragment() {

    private var _binding: FragmentChipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChipsBinding.inflate(inflater, container, false)

        val currentNightMode = (resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK)

        when(currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.darkTheme.chipIcon = resources.getDrawable(R.drawable.ic_baseline_moon_24)
                binding.darkTheme.text = getText(R.string.Enable_dark_theme)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.darkTheme.chipIcon = resources.getDrawable(R.drawable.ic_baseline_sunny_24)
                binding.darkTheme.text = getText(R.string.Enable_light_theme)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentNightMode = (resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK)
        binding.darkTheme.setOnClickListener {
            when(currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                        .commit()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}