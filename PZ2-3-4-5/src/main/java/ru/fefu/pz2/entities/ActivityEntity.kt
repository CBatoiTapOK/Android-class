package ru.fefu.pz2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val type: String,
    val distanceInMeters: Int,
    val timeInSeconds: Int,
    val startTime: Long,
    val endTime: Long,
    val date: String,
    val author: String,
    val comment: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActivityEntity

        return id == other.id &&
                type == other.type &&
                distanceInMeters == other.distanceInMeters &&
                timeInSeconds == other.timeInSeconds &&
                author == other.author &&
                date == other.date
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + distanceInMeters
        result = 31 * result + timeInSeconds
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        return result
    }
}

