package ru.fefu.pz2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.fefu.pz2.dao.UserDao
import ru.fefu.pz2.database.UserDatabase
import ru.fefu.pz2.databinding.ActivityChangePasswordBinding
import ru.fefu.pz2.helpers.SessionManager

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        sessionManager = SessionManager(this)
        userDao = UserDatabase.getDatabase(this).userDao()

        val oldPasswordEditText = binding.oldPasswordEdittext
        val newPasswordEditText = binding.newPasswordEdittext
        val repeatNewPasswordEditText = binding.repeatNewPasswordEdittext
        val agreeChangesButton = binding.agreeChangesButton

        agreeChangesButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val repeatNewPassword = repeatNewPasswordEditText.text.toString()

            if (newPassword == repeatNewPassword) {
                lifecycleScope.launch {
                    val login = sessionManager.getUserLogin()
                    val user = login?.let { userDao.findUserByLoginAndPassword(it, oldPassword) }
                    if (user != null) {
                        updatePassword(login, newPassword)
                        Toast.makeText(this@ChangePasswordActivity, "Пароль изменён", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, "Старый пароль неправильный", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Новые пароли не совпадают", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, ActivScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun updatePassword(login: String, newPassword: String) {
        withContext(Dispatchers.IO) {
            userDao.updatePassword(login, newPassword)
        }
    }
}