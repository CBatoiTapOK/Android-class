package ru.fefu.pz2.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.pz2.ActivityDetailActivity
import ru.fefu.pz2.databinding.ActivityItemBinding
import ru.fefu.pz2.entities.ActivityEntity
import java.text.SimpleDateFormat
import java.util.*

class ActivityAdapter(private val context: Context, private var activities: List<ActivityEntity>, private val loggedInUsername: String) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    fun updateData(newActivities: List<ActivityEntity>) {
        val diffResult = DiffUtil.calculateDiff(ActivityDiffCallback(activities, newActivities))
        this.activities = newActivities
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.bind(activity, loggedInUsername)
    }

    override fun getItemCount(): Int = activities.size

    inner class ActivityViewHolder(private val binding: ActivityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: ActivityEntity, loggedInUsername: String) {
            binding.activityType.text = activity.type
            binding.activityDistance.text = formatDistance(activity.distanceInMeters)
            binding.activityTime.text = formatTime(activity.timeInSeconds)
            binding.activityAuthor.text = activity.author?.let { "@$it" }
            binding.activityDateDetail.text = activity.date
            binding.activityDate.text = activity.date?.let { formatActivityDate(it) }

            itemView.setOnClickListener {
                val intent = Intent(context, ActivityDetailActivity::class.java)
                intent.putExtra("activity_id", activity.id)
                context.startActivity(intent)
            }

            if (activity.author == loggedInUsername) {
                binding.activityAuthor.visibility = View.GONE
            } else {
                binding.activityAuthor.visibility = View.VISIBLE
            }
        }

        private fun formatDistance(distanceInMeters: Int): String {
            return if (distanceInMeters >= 1000) {
                String.format("%.1f км", distanceInMeters / 1000.0)
            } else {
                "$distanceInMeters м"
            }
        }

        private fun formatTime(timeInSeconds: Int): String {
            return if (timeInSeconds >= 3600) {
                String.format("%.1f ч %.1f мин", timeInSeconds / 3600.0, (timeInSeconds % 3600) / 60)
            }
            else {
                "${timeInSeconds / 60} мин ${timeInSeconds % 60} сек"
            }
        }

        private fun formatActivityDate(date: String): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val activityDate = sdf.parse(date)
            val currentDate = Calendar.getInstance().time

            if (activityDate == null) return "Дата не указана"

            val diff = currentDate.time - activityDate.time
            val daysDiff = diff / (1000 * 60 * 60 * 24)

            return when {
                daysDiff == 0L -> "Сегодня"
                daysDiff == 1L -> "Вчера"
                daysDiff == 2L -> "Позавчера"
                else -> SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(activityDate)
            }
        }

    }

    class ActivityDiffCallback(
        private val oldList: List<ActivityEntity>,
        private val newList: List<ActivityEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}