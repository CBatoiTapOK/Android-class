package ru.fefu.pz2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.fefu.pz2.databinding.ActivityActivScreenBinding


class ActivScreenActivity: AppCompatActivity() {

    private lateinit var binding: ActivityActivScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActivScreenBinding.inflate(layoutInflater)

        val bottomNavigationView = binding.bottomNavigationView

        // (костыль т.к в задании попросили navigationView а у нас всего 2 элемента)
        val menuView = bottomNavigationView.getChildAt(0) as? BottomNavigationView
        menuView?.let {
            for (i in 0 until it.childCount) {
                val itemView = it.getChildAt(i)
                val layoutParams = itemView.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 1f
                layoutParams.width = 0
                itemView.layoutParams = layoutParams
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragmentActivities, ActivitiesFragment.newInstance(), "activities_tag")
                addToBackStack("activities_tag")
                commit()
            }
        }

        setContentView(binding.root)



        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.activities -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentActivities, ActivitiesFragment.newInstance(), "activities_tag")
                        commit()
                    }
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentActivities, ProfileFragment.newInstance(), "profile_tag")
                        commit()
                    }
                    true
                }
                else -> false
            }
        }

    }

}
