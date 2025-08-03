import java.time.LocalDate

class AttendanceList : ArrayList<Attendance>() {
    override fun add(element: Attendance): Boolean {
        return super.add(element)
    }

    override fun toString(): String {
        if (this.isEmpty()) return "No attendance records found."

        return this.joinToString(separator = "\n") {
            val checkOut = it.dateTimeOfCheckOut?.toString() ?: "N/A"
            val hours = it.workedHours ?: "N/A"
            "${it.employeeId} | Check-in: ${it.dateTimeOfCheckIn} | Check-out: $checkOut | Hours Worked: $hours"
        }
    }


    fun hasAlreadyCheckedIn(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date
        }
    }

    fun hasAlreadyCheckedOut(employeeId: String, date: LocalDate): Boolean {
        return this.any {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date &&
                    it.dateTimeOfCheckOut != null
        }
    }
}