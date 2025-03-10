package ru.fefu.pz2

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ru.fefu.pz2.databinding.ActivityLoginScreenBinding


class LoginScreenActivity : Activity() {

    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.EnterButton.setOnClickListener {

            val backIntent = Intent(this, ActivScreenActivity::class.java)

            startActivity(backIntent)
        }

        binding.BackButton.setOnClickListener {

            val backIntent = Intent(this, WelcomeScreenActivity::class.java)

            startActivity(backIntent)
        }

    }

}