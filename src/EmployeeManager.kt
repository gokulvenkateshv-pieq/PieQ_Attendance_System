import data.Role
import data.Department
import data.Manager
import java.time.LocalDate
import java.time.LocalDateTime

class EmployeeManager {
    private val employeeList = EmployeeList()
    private val attendanceList = AttendanceList()

    fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun addEmployee(
        firstName: String,
        lastName: String,
        role: Role,
        department: Department,
        reportingTo: Manager?
    ): String? {
        val employee = Employee(firstName, lastName, role, department, reportingTo)
        return if (employee.validate()) {
            employeeList.add(employee)
            employee.employeeId
        } else {
            null
        }
    }

    fun employeeExists(employeeId: String) : Boolean {
        return employeeList.employeeExists(employeeId)
    }

    fun deleteEmployee(employeeId: String): Boolean {
        return employeeList.deleteEmployee(employeeId)
    }

    fun getAllEmployees(): String {
        return employeeList.toString()
    }

    fun checkIn(employeeId: String, checkInTime: LocalDateTime?): Boolean {
        val actualCheckIn = checkInTime ?: getCurrentDateTime()
        val today = actualCheckIn.toLocalDate()

        if (!employeeList.employeeExists(employeeId)) return false
        if (attendanceList.hasAlreadyCheckedIn(employeeId, today)) return false

        val attendance = Attendance(employeeId, actualCheckIn)
        return attendanceList.add(attendance)
    }

    fun checkOut(employeeId: String, checkOutTime: LocalDateTime?): Boolean {
        val actualCheckOut = checkOutTime ?: getCurrentDateTime()

        if (!employeeList.employeeExists(employeeId)) return false
        val attendance = attendanceList.validateCheckedOut(employeeId, actualCheckOut)
        if (attendance != null) {
            attendance.checkOut(actualCheckOut)
            return true
        } else
            return false

    }

    fun deleteAttendance(employeeId: String, date: LocalDate): Boolean {
        return attendanceList.deleteAttendanceRecord(employeeId, date)
    }

    fun getAllAttendance(): String {
        return attendanceList.toString()
    }


    }
