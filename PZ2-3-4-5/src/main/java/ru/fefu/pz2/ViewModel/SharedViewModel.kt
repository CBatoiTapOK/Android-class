import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.fefu.pz2.entities.ActivityEntity

class SharedViewModel : ViewModel() {

    private val _activities = MutableLiveData<List<ActivityEntity>>()
    val activities: LiveData<List<ActivityEntity>> get() = _activities


    fun updateActivities(newActivities: List<ActivityEntity>) {
        _activities.value = newActivities
    }
}