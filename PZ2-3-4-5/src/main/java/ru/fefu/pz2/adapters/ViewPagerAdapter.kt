package ru.fefu.pz2.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.fefu.pz2.FriendsActivitiesFragment
import ru.fefu.pz2.MyActivitiesFragment

class ViewPagerAdapter(
    private val fragment: Fragment,
    private val loggedInUsername: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyActivitiesFragment.newInstance(loggedInUsername)
            1 -> FriendsActivitiesFragment.newInstance(loggedInUsername)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}