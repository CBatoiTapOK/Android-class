package ru.fefu.pz2

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.fefu.pz2.databinding.ActivityLoginScreenBinding
import ru.fefu.pz2.database.UserDatabase
import ru.fefu.pz2.helpers.SessionManager


class LoginScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var userDatabase: UserDatabase
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        userDatabase = UserDatabase.getDatabase(this)
        sessionManager = SessionManager(this)

        binding.EnterButton.setOnClickListener {

            val login = binding.LoginEdit.text.toString().trim()
            val password = binding.PasswordEdit.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDatabase.userDao().findUserByLoginAndPassword(login, password)
                if (user != null) {
                    sessionManager.createLoginSession(login)
                    navigateToActivScreenActivity()
                } else {
                    Toast.makeText(
                        this@LoginScreenActivity,
                        "Неверный логин или пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        binding.BackButton.setOnClickListener {

            val backIntent = Intent(this, WelcomeScreenActivity::class.java)

            startActivity(backIntent)
        }


    }

    private fun navigateToActivScreenActivity() {
        val intent = Intent(this, ActivScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToWelcomeScreenActivity() {
        val intent = Intent(this, WelcomeScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

}