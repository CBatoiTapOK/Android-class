package ru.fefu.pz2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.fefu.pz2.dao.UserDao
import ru.fefu.pz2.database.UserDatabase
import ru.fefu.pz2.databinding.FragmentProfileBinding
import ru.fefu.pz2.helpers.SessionManager


class ProfileFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var userDao: UserDao
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        userDao = UserDatabase.getDatabase(requireContext()).userDao()

        val loginEditText = binding.loginEdittext
        val usernameEditText = binding.usernameEdittext
        val changePasswordButton = binding.changePasswordButton
        val logoutButton = binding.logoutButton
        val saveButton = binding.saveButton

        changePasswordButton.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            Toast.makeText(requireContext(), "Лень это делать )", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        logoutButton.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(activity, LoginScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        loginEditText.setText(sessionManager.getUserLogin())

        lifecycleScope.launch {
            val nickname = fetchUsernameFromDatabase()
            withContext(Dispatchers.Main) {
                usernameEditText.setText(nickname)
            }
        }
    }

    private suspend fun fetchUsernameFromDatabase(): String? {
        val login = sessionManager.getUserLogin()
        return withContext(Dispatchers.IO) {
            login?.let {
                val user = userDao.findUserByLogin(it)
                user?.username
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}