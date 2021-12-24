package pavel.ivanov.pictureoftheday.ui.picture

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import pavel.ivanov.pictureoftheday.R
import pavel.ivanov.pictureoftheday.databinding.FragmentMainBinding
import pavel.ivanov.pictureoftheday.ui.MainActivity
import pavel.ivanov.pictureoftheday.ui.adapter.ViewPagerAdapter
import pavel.ivanov.pictureoftheday.ui.drawer.BottomNavigationDrawerFragment
import pavel.ivanov.pictureoftheday.ui.picture.fragmentsoftheday.TodayFragment
import pavel.ivanov.pictureoftheday.ui.settings.SettingsFragment
import pavel.ivanov.pictureoftheday.viewmodel.PictureOfTheDayState
import pavel.ivanov.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var isMain = true
    private var backPressedTime = 0L;

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.pictureOfTheDayViewPager.adapter = ViewPagerAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.pictureOfTheDayViewPager){ tab, position -> }.attach()
        binding.tabLayout.getTabAt(0)?.setText(R.string.day_before_yesterday)
        binding.tabLayout.getTabAt(1)?.setText(R.string.yesterday)
        binding.tabLayout.getTabAt(2)?.setText(R.string.today)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pictureOfTheDayViewPager.setCurrentItem(2, false)

        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })

        when (binding.pictureOfTheDayViewPager.currentItem) {
            0 -> viewModel.sendServerRequest(takeDate(-2))
            1 -> viewModel.sendServerRequest(takeDate(-1))
            2 -> viewModel.sendServerRequest(takeDate(0))
        }

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        setBottomAppBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.app_bar_fav -> {
                Toast.makeText(context, R.string.favourite, Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_settings -> {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager, "")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderData(state: PictureOfTheDayState) {
        when(state) {
            is PictureOfTheDayState.Error -> {

                binding.pictureOfTheDayViewPager.isVisible = false
                binding.fragmentMainLoadingLayout.isVisible = false

                Snackbar
                    .make(binding.main, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getData() }
                    .show()
            }
            is PictureOfTheDayState.Loading -> {

                binding.pictureOfTheDayViewPager.isVisible = false
                binding.fragmentMainLoadingLayout.isVisible = true

            }
            is PictureOfTheDayState.Success -> {

                binding.pictureOfTheDayViewPager.isVisible = true
                binding.fragmentMainLoadingLayout.isVisible = false

                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData
                val title = pictureOfTheDayResponseData.title
                val description = pictureOfTheDayResponseData.explanation

                binding.includeBottomSheet.bottomSheetDescriptionHeader.text = title
                binding.includeBottomSheet.bottomSheetDescription.text = description

            }
        }
    }

    private fun setBottomAppBar() {
        val behavior = BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheetContainer)
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                binding.bottomAppBar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed")

                    if (isMain) {
                        val t = System.currentTimeMillis()
                        if (t - backPressedTime > 2000) {    // 2 secs
                            backPressedTime = t
                            Toast.makeText(
                                context, "Press back again to exit",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            requireActivity().onBackPressed()
                        }
                    } else {
                        isMain = true
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.bottomAppBar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                        binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                        binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                    }
                }
            })
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

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}