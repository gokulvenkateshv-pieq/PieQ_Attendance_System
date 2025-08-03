import data.Role
import data.Department
import java.time.LocalDate
import java.time.LocalDateTime

class EmployeeManager {

    // Declared as static (companion object) so it's shared across all instances.
    // If not, every new instance would create its own counter and generate duplicate IDs.
    companion object {
        private var idCounter = 1
        private val employeeList = EmployeeList()
        private val attendanceList = AttendanceList()

        private fun generateEmployeeId(fName: String, lName: String): String {
            val safeFirst = fName.trim().ifEmpty { "X" }
            val safeLast = lName.trim().ifEmpty { "X" }
            val character = safeFirst.trim().first().uppercaseChar().toString() + safeLast.trim().first().uppercaseChar().toString()
            return character + idCounter.toString().padStart(3, '0')

        }
    }

    fun addEmployee(
        firstName: String,
        lastName: String,
        role: Role,
        department: Department,
        reportingTo: String
    ): Employee? {
        val generatedId = generateEmployeeId(firstName,lastName)
        val employee = Employee(generatedId, firstName, lastName, role, department, reportingTo)

        return if (employee.validateEmployee()) {
            employeeList.add(employee)
            idCounter++ // Only increment after successful addition
            employee
        } else {
            null
        }
    }

    fun updateEmployee(
        employeeId: String,
        firstName: String,
        lastName: String,
        role: Role,
        department: Department,
        reportingTo: String
    ): Boolean {
        val index = getEmployeeIndex(employeeId)
        if (index == -1) {
            println("Employee with ID $employeeId not found.")
            return false
        }

        val updatedEmployee = Employee(employeeId, firstName, lastName, role, department, reportingTo)
        return if (updatedEmployee.validateEmployee()) {
            employeeList[index] = updatedEmployee
            println("Employee $employeeId updated.")
            true
        } else {
            println("Invalid update data.")
            false
        }
    }

    fun deleteEmployee(employeeId: String): Boolean {
        val index = getEmployeeIndex(employeeId)

        return if (index != -1) {
            employeeList.removeAt(index)
            println("Employee with ID $employeeId deleted.")
            true
        } else {
            println("Employee with ID $employeeId not found.")
            false
        }
    }

    fun getEmployeeIndex(employeeId: String): Int {
        return employeeList.indexOfFirst { it.employeeId == employeeId }
    }

    fun getAllEmployees(): List<Employee> = employeeList

    fun checkIn(employeeId: String, dateTime: LocalDateTime): Boolean {
        val checkInDate = dateTime.toLocalDate()

        if (attendanceList.hasAlreadyCheckedIn(employeeId, checkInDate)) {
            println("Employee $employeeId has already checked in on $checkInDate.")
            return false
        }

        val attendance = Attendance(employeeId, dateTime)
        attendanceList.add(attendance)
        println("Employee added")
        return true
    }

    fun updateCheckIn(employeeId: String, newCheckInTime: LocalDateTime): Boolean {
        val checkInDate = newCheckInTime.toLocalDate()

        val record = attendanceList.find {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == checkInDate
        }

        return if (record != null) {
            record.dateTimeOfCheckIn = newCheckInTime
            println(" Check-in time updated for $employeeId on $checkInDate.")
            true
        } else {
            println(" No existing check-in found for $employeeId on $checkInDate.")
            false
        }
    }

    fun deleteCheckIn(employeeId: String, date: LocalDate): Boolean {
        val record = attendanceList.find {
            it.employeeId == employeeId && it.dateTimeOfCheckIn.toLocalDate() == date
        }

        return if (record != null) {
            attendanceList.remove(record)
            println("Check-in deleted for $employeeId on $date.")
            true
        } else {
            println(" No check-in found for $employeeId on $date.")
            false
        }
    }

    fun checkOut(employeeId: String, dateTime: LocalDateTime): Boolean {
        val date = dateTime.toLocalDate()

        if (attendanceList.hasAlreadyCheckedOut(employeeId, date)) {
            println("Employee $employeeId has already checked out on $date.")
            return false
        }

        val record = attendanceList.find {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date &&
                    it.dateTimeOfCheckOut == null
        }

        return if (record != null) {
            record.dateTimeOfCheckOut = dateTime
            record.calculateWorkedHours()
            true
        } else {
            println("Check-out failed: No check-in found for Employee $employeeId on $date.")
            false
        }
    }

    fun updateCheckOut(employeeId: String, newCheckOutTime: LocalDateTime): Boolean {
        val checkOutDate = newCheckOutTime.toLocalDate()

        val record = attendanceList.find {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == checkOutDate
        }

        return if (record != null) {
            record.dateTimeOfCheckOut = newCheckOutTime
            record.calculateWorkedHours()

            println(" Check-in time updated for $employeeId on $checkOutDate.")
            true
        } else {
            println(" No existing check-in found for $employeeId on $checkOutDate.")
            false
        }
    }

    fun deleteCheckOut(employeeId: String, date: LocalDate): Boolean {
        val record = attendanceList.find {
            it.employeeId == employeeId && it.dateTimeOfCheckIn.toLocalDate() == date
        }

        return if (record != null && record.dateTimeOfCheckOut != null) {
            record.dateTimeOfCheckOut = null
            record.workedHours = null
            println("Check-out time deleted for $employeeId on $date.")
            true
        } else {
            println(" No check-out found for $employeeId on $date.")
            false
        }
    }

    fun getAllAttendance(): List<Attendance> = attendanceList

}

