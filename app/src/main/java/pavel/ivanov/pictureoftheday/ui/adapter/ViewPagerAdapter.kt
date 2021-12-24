package pavel.ivanov.pictureoftheday.ui.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import pavel.ivanov.pictureoftheday.ui.picture.fragmentsoftheday.DayBeforeYesterdayFragment
import pavel.ivanov.pictureoftheday.ui.picture.fragmentsoftheday.TodayFragment
import pavel.ivanov.pictureoftheday.ui.picture.fragmentsoftheday.YesterdayFragment

open class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val fragments = arrayOf(DayBeforeYesterdayFragment(), YesterdayFragment(), TodayFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}