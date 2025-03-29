package ru.fefu.pz2

import SharedViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.pz2.adapters.ActivityAdapter
import ru.fefu.pz2.databinding.FragmentMyActivitiesBinding

class MyActivitiesFragment : Fragment() {

    private lateinit var emptyStateMessageTop: View
    private lateinit var emptyStateMessageBottom: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActivityAdapter
    private lateinit var viewModel: SharedViewModel
    private lateinit var loggedInUsername: String
    private var _binding: FragmentMyActivitiesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyActivitiesBinding.inflate(inflater, container, false)
        loggedInUsername = arguments?.getString("LOGGED_IN_USERNAME") ?: "unknown_user"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ActivityAdapter(requireContext(), listOf(), loggedInUsername)
        recyclerView.adapter = adapter
        emptyStateMessageTop = binding.emptyStateMessageTop
        emptyStateMessageBottom = binding.emptyStateMessageBottom

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        viewModel.activities.observe(viewLifecycleOwner) { activities ->
            val filteredActivities = activities.filter { it.author == loggedInUsername }
            if (filteredActivities.isEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
            }

            adapter.updateData(filteredActivities)
        }
    }

    private fun showEmptyState(show: Boolean) {
        if (show) {
            recyclerView.visibility = View.GONE
            emptyStateMessageTop.visibility = View.VISIBLE
            emptyStateMessageBottom.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateMessageTop.visibility = View.GONE
            emptyStateMessageBottom.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(loggedInUsername: String): MyActivitiesFragment {
            val fragment = MyActivitiesFragment()
            val args = Bundle()
            args.putString("LOGGED_IN_USERNAME", loggedInUsername)
            fragment.arguments = args
            return fragment
        }
    }
}