package ru.fefu.pz2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.fefu.pz2.entities.ActivityEntity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY startTime DESC")
    fun getAllActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE id = :id")
    fun getActivityById(id: Int): ActivityEntity?

    @Insert
    fun insertActivities(vararg activities: ActivityEntity)

    @Query("DELETE FROM activities")
    fun deleteAllActivities()

    @Query("UPDATE activities SET comment = :comment WHERE id = :id")
    fun updateComment(id: Int, comment: String)

    @Query("DELETE FROM activities WHERE id = :id")
    fun deleteActivityById(id: Int)
}