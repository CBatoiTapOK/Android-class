package ru.fefu.pz2

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import ru.fefu.pz2.databinding.ActivityLoginScreenBinding


class LoginScreenActivity : Activity() {

    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.BackButton.setOnClickListener {

            val backIntent = Intent(this, WelcomeScreenActivity::class.java)

            startActivity(backIntent)
        }
    }

}