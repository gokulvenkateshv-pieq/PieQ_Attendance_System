import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Attendance(
    val employeeId: String,
    var dateTimeOfCheckIn: LocalDateTime,
    var dateTimeOfCheckOut: LocalDateTime? = null,
    var workedHours: Duration = Duration.ZERO

) {

    fun checkOut(dateTime: LocalDateTime) {
        this.dateTimeOfCheckOut = dateTime
        val duration = Duration.between(dateTimeOfCheckIn, dateTimeOfCheckOut)

        if (duration > Duration.ZERO) {
            this.workedHours = duration
        }
    }


    override fun toString(): String {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val checkInDate = dateTimeOfCheckIn.format(dateFormatter)
        val checkInTime = dateTimeOfCheckIn.format(timeFormatter)
        val checkOutTime = dateTimeOfCheckOut?.format(timeFormatter) ?: "N/A"

        return "Employee ID: $employeeId, Date: $checkInDate, Check-In: $checkInTime, Check-Out: $checkOutTime, Worked Hours: ${workedHours.toHours()}h ${workedHours.toMinutesPart()}m"
    }



}
