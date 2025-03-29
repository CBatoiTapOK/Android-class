package ru.fefu.pz2

import android.os.Bundle
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.RadioButton
import ru.fefu.pz2.databinding.ActivityRegistrationScreenBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.fefu.pz2.database.UserDatabase
import ru.fefu.pz2.helpers.SessionManager
import ru.fefu.pz2.entities.UserEntity

class RegistrationScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationScreenBinding
    private lateinit var userDatabase: UserDatabase
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        userDatabase = UserDatabase.getDatabase(this)
        sessionManager = SessionManager(this)

        setupAgreementText()

        binding.BackButton.setOnClickListener {

            val Intent = Intent(this, WelcomeScreenActivity::class.java)

            startActivity(Intent)
        }

        binding.RegistrationButton.setOnClickListener {

            val login = binding.LoginEdit.text.toString().trim()
            val username = binding.NicknameEdit.text.toString().trim()
            val password = binding.PasswordEdit.text.toString().trim()
            val repeatPassword = binding.RepeatPasswordEdit.text.toString().trim()
            val genderId = binding.radioGroup.checkedRadioButtonId

            if (login.isEmpty() || username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || genderId == -1) {
                Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = findViewById<RadioButton>(genderId)?.text.toString()

            if (password == repeatPassword) {
                lifecycleScope.launch {
                    val existingUser = userDatabase.userDao().findUserByLogin(login)
                    if (existingUser != null) {
                        Toast.makeText(
                            this@RegistrationScreenActivity,
                            "Логин уже существует",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val user = UserEntity(
                            login = login,
                            username = username,
                            password = password,
                            gender = gender
                        )
                        userDatabase.userDao().insert(user)
                        sessionManager.createLoginSession(login)
//                        logAllUsers()
                        navigateToActivScreenActivity()
                    }
                }
            } else {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun navigateToActivScreenActivity() {
        val intent = Intent(this, ActivScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupAgreementText() {
        val agreementTextView = binding.agreement
        val text = getString(R.string.agreement)
        val spannableString = SpannableString(text)

        val purpleColor = ContextCompat.getColor(this, R.color.purple)
        val policyText = "политикой конфиденциальности"
        val agreementText = "пользовательское соглашение"

        val policyStart = text.indexOf(policyText)
        val policyEnd = policyStart + policyText.length
        val agreementStart = text.indexOf(agreementText)
        val agreementEnd = agreementStart + agreementText.length

        if (policyStart != -1 && agreementStart != -1) {
            spannableString.setSpan(
                ForegroundColorSpan(purpleColor),
                policyStart,
                policyEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(purpleColor),
                agreementStart,
                agreementEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        agreementTextView.text = spannableString
    }


}



