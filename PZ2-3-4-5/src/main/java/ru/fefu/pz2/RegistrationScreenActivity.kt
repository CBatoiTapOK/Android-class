package ru.fefu.pz2

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import ru.fefu.pz2.databinding.ActivityRegistrationScreenBinding

class RegistrationScreenActivity : Activity() {

    private lateinit var binding: ActivityRegistrationScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.BackButton.setOnClickListener {

            val backIntent = Intent(this, WelcomeScreenActivity::class.java)

            startActivity(backIntent)
        }

        binding.RegistrationButton.setOnClickListener {

            val backIntent = Intent(this, ActivScreenActivity::class.java)

            startActivity(backIntent)
        }
    }

}



