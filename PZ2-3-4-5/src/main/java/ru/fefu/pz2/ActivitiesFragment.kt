package ru.fefu.pz2

import SharedViewModel
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.fefu.pz2.databinding.FragmentActivitiesBinding
import ru.fefu.pz2.adapters.ViewPagerAdapter
import ru.fefu.pz2.database.AppDatabase
import ru.fefu.pz2.helpers.SessionManager


class ActivitiesFragment : Fragment() {

    private lateinit var addActivityButton: View
    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var loggedInUsername: String
    private lateinit var viewModel: SharedViewModel
    private var _binding: FragmentActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())
        sessionManager = SessionManager(requireContext())
        loggedInUsername = sessionManager.getUserLogin() ?: "unknown_user"
        addActivityButton = binding.addActivityButton

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        loadInitialData()

        binding.addActivityButton.setOnClickListener {
            val intent = Intent(context, NewActivity::class.java)
            startActivity(intent)
        }

        val viewPager = binding.ViewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(this, loggedInUsername)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Мои"
                1 -> tab.text = "Пользователей"
            }
        }.attach()

        binding.addActivityButton.setOnClickListener {
            val intent = Intent(context, NewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadInitialData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val initialActivities = withContext(Dispatchers.IO) {
                db.activityDao().getAllActivities()
            }
            viewModel.updateActivities(initialActivities)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance() = ActivitiesFragment()
    }
}

