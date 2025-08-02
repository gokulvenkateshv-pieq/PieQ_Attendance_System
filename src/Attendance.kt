import java.time.LocalDateTime

class Attendance(
    val employeeId: String,
    var dateTimeOfCheckIn: LocalDateTime,
    var dateTimeOfCheckOut: LocalDateTime? = null
) {}