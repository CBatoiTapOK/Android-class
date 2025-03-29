package ru.fefu.pz2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.pz2.adapters.ActivityTypeAdapter
import ru.fefu.pz2.database.AppDatabase
import ru.fefu.pz2.entities.ActivityEntity
import ru.fefu.pz2.entities.ActivityType
import ru.fefu.pz2.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.fefu.pz2.databinding.ActivityNewBinding

class NewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBinding
    private var selectedActivityType: String? = null
    private var distance = 0
    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var author: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)
        author = sessionManager.getUserLogin() ?: "unknown_user"


        val activityTypes = listOf(
            ActivityType(1, "Велосипед"),
            ActivityType(2, "Бег"),
            ActivityType(3, "Шаг")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.activity_recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ActivityTypeAdapter(this, activityTypes) { activityType ->
            selectedActivityType = activityType.name
            binding.activityType.text = selectedActivityType
        }

        binding.startButton.setOnClickListener {
            if (selectedActivityType != null) {
                binding.activityMenu.visibility = View.GONE
                binding.activityInfoMenu.visibility = View.VISIBLE
                binding.activityDistance.text = "0 метров"
                binding.activityTime.text = "00:00:00"
                startStopwatch()
            } else {
                Toast.makeText(this, "Выберите тип активности", Toast.LENGTH_SHORT).show()
            }
        }

        binding.stopButton.setOnClickListener {
            stopStopwatch()
        }
    }

    private fun startStopwatch() {
        distance = 0
        seconds = 0
        runnable = object : Runnable {
            override fun run() {
                seconds++
                if (seconds % 2 == 0) {
                    distance += 10
                    binding.activityDistance.text = "$distance метров"
                }
                binding.activityTime.text = formatTime(seconds)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    private fun stopStopwatch() {
        handler.removeCallbacks(runnable)
        saveActivityToDatabase()
    }

    private fun saveActivityToDatabase() {
        val activityType = selectedActivityType ?: return
        val currentTime = System.currentTimeMillis()
        val activityEntity = ActivityEntity(
            id = 0,
            type = activityType,
            distanceInMeters = distance,
            timeInSeconds = seconds,
            startTime = currentTime - (seconds * 1000),
            endTime = currentTime,
            date = getCurrentDate(),
            author = author,
            comment = null
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.activityDao().insertActivities(activityEntity)
            withContext(Dispatchers.Main) {
                navigateToEmptystateActivity()
            }
        }
    }

    private fun navigateToEmptystateActivity() {
        val intent = Intent(this, ActivScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }
}