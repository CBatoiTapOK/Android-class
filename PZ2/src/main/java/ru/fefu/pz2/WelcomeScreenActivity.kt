package ru.fefu.pz2

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import ru.fefu.pz2.databinding.ActivityWelcomeScreenBinding

class WelcomeScreenActivity : Activity() {

    private lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.RegistrationButton.setOnClickListener {

            val registrationIntent = Intent(this, RegistrationScreenActivity::class.java)

            startActivity(registrationIntent)
        }

        binding.LoginButton.setOnClickListener {
            val loginIntent = Intent(this, LoginScreenActivity::class.java)

            startActivity(loginIntent)

        }

    }
}






