import java.time.LocalDate
import java.time.LocalDateTime

class AttendanceList : ArrayList<Attendance>() {
    override fun add(element: Attendance): Boolean {
        return super.add(element)
    }

    override fun toString(): String {
        if (this.isEmpty()) return "No attendance records found."

        return this.joinToString(separator = "\n")
    }

    fun hasAlreadyCheckedIn(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date
        }
    }

    //check id exist,checkIn done on that date and checkout not null and after checkIn
    fun validateCheckedOut(employeeId: String, date: LocalDateTime): Attendance?{

        return this.find {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date.toLocalDate() &&
                    it.dateTimeOfCheckOut == null &&
                    date.isAfter(it.dateTimeOfCheckIn) }
    }

    fun deleteAttendanceRecord(employeeId: String,date: LocalDate): Boolean
    {
        val record = this.find {
            it.employeeId == employeeId && it.dateTimeOfCheckIn.toLocalDate() == date
        }

        return if (record != null) {
            this.remove(record)
            true
        } else {
            false
        }
    }



}


