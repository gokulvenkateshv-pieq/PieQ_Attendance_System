import java.time.Duration
import java.time.LocalDateTime

class Attendance(
    val employeeId: String,
    var dateTimeOfCheckIn: LocalDateTime,
    var dateTimeOfCheckOut: LocalDateTime? = null,
    var workedHours: String? = null
) {
    fun calculateWorkedHours() {
        if (dateTimeOfCheckOut != null) {
            val duration = Duration.between(dateTimeOfCheckIn, dateTimeOfCheckOut)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            workedHours = String.format("%02d:%02d", hours, minutes)
        } else {
            workedHours = null
        }
    }
}
