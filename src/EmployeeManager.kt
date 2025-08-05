import data.Role
import data.Department
import java.time.LocalDate
import java.time.Duration
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
        reportingTo: String?
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
        reportingTo: String?
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

        if (dateTime.isAfter(LocalDateTime.now())) {
            println("Check-in time cannot be in the future.")
            return false
        }
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

        if (newCheckInTime.isAfter(LocalDateTime.now())) {
            println("Check-in time cannot be in the future.")
            return false
        }


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

        val attendanceRecord = attendanceList.find {
            it.employeeId == employeeId &&
                    it.dateTimeOfCheckIn.toLocalDate() == date &&
                    it.dateTimeOfCheckOut == null
        }

        if (attendanceRecord == null) {
            println("Check-out failed: No check-in found for Employee $employeeId on $date.")
            return false
        }

        if (dateTime.isBefore(attendanceRecord.dateTimeOfCheckIn)) {
            println("Check-out time cannot be before check-in time.")
            return false
        }



        if (dateTime.isAfter(LocalDateTime.now())) {
            println("Check-out time cannot be in the future.")
            return false
        }


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
            println("Employee Checked Out")

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

        if (record == null) {
            println("No existing check-in found for $employeeId on $checkOutDate.")
            return false
        }

        if (newCheckOutTime.isAfter(LocalDateTime.now())) {
            println("Check-out time cannot be in the future.")
            return false
        }

        if (newCheckOutTime.isBefore(record.dateTimeOfCheckIn)) {
            println("Check-out time cannot be before check-in time.")
            return false
        }

        record.dateTimeOfCheckOut = newCheckOutTime
        record.calculateWorkedHours()

        println("Check-out time updated for $employeeId on $checkOutDate.")
        return true
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

    fun showWorkSummary(from: LocalDate, to: LocalDate) {
        // Step 1: Filter attendance records in range and with check-out
        val filteredRecords = attendanceList
            .filter {
                val date = it.dateTimeOfCheckIn.toLocalDate()
                date in from..to && it.dateTimeOfCheckOut != null
            }
            .sortedWith(compareBy({ it.employeeId }, { it.dateTimeOfCheckIn })) // Step 2: Sort list by ID and date

        if (filteredRecords.isEmpty()) {
            println("No attendance records found between $from and $to.")
            return
        }

        println("Summary from $from to $to:")
        println("ID      | Name                  | Total Worked Hours")
        println("--------|-----------------------|-------------------")

        var tempEmpId: String? = null
        var totalDuration = Duration.ZERO

        for ((index, record) in filteredRecords.withIndex()) {
            val id = record.employeeId
            val duration = Duration.between(record.dateTimeOfCheckIn, record.dateTimeOfCheckOut)

            if (tempEmpId == null) {
                tempEmpId = id
            }

            if (id == tempEmpId) {
                totalDuration = totalDuration.plus(duration)
            }

            // Check if the record is the last one for this employee and next id is not for this employee
            val isLastForThisEmp = (index == filteredRecords.lastIndex) ||
                    (filteredRecords[index + 1].employeeId != tempEmpId)

            if (isLastForThisEmp) {
                val emp = employeeList.find { it.employeeId == tempEmpId }
                val name = emp?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"

                val hours = totalDuration.toHours()
                val minutes = totalDuration.toMinutes() % 60
                val formatted = String.format("%02d:%02d", hours, minutes)

                println("${tempEmpId.padEnd(8)}| ${name.padEnd(23)}| $formatted")

                // Reset for next employee
                totalDuration = Duration.ZERO
                tempEmpId = if (index != filteredRecords.lastIndex) {
                    filteredRecords[index + 1].employeeId
                } else null
            }
        }
    }


}

