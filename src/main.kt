import data.Role
import data.Department
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun main() {
    val manager = EmployeeManager()
    mainMenu(manager)
}

fun mainMenu(manager: EmployeeManager) {
    while (true) {
        println(
            """
            |===============================
            | Employee & Attendance System
            |===============================
            | 1. Add Employee
            | 2. View Employees
            | 3. Update Employee
            | 4. Delete Employee
            | 5. Check In
            | 6. Update Check In
            | 7. Delete Check In
            | 8. Check Out
            | 9. Update Check Out
            | 10. Delete Check Out
            | 11. View Attendance
            | 12. View Worked Hours by Employee
            | 13. Exit
            |===============================
            | Enter your choice: 
            """.trimMargin()
        )

        when (readln().trim()) {
            "1" -> {
                println("Enter First Name:")
                val firstName = readln()

                println("Enter Last Name:")
                val lastName = readln()

                println("Enter Role (INTERN, DEVELOPER, MANAGER):")
                val role = Role.valueOf(readln().uppercase())

                println("Enter Department (IT, MARKETING, FINANCE):")
                val department = Department.valueOf(readln().uppercase())

                println("Enter Reporting To (Manager ID):")
                val reportingTo = readln()

                val employee = manager.addEmployee(firstName, lastName, role, department, reportingTo)
                if (employee != null) {
                    println(" Employee added with ID: ${employee.employeeId}")
                } else {
                    println(" Failed to add employee. Invalid data.")
                }
            }

            "2" -> {
                println(" All Employees:")
                println(manager.getAllEmployees())
            }

            "3" -> {
                println("Enter Employee ID to Update:")
                val id = readln()
                if (manager.getEmployeeIndex(id) == -1) {
                    println(" Employee ID not found.")
                    continue
                }

                println("Enter New First Name:")
                val firstName = readln()

                println("Enter New Last Name:")
                val lastName = readln()

                println("Enter New Role (INTERN, DEVELOPER, MANAGER):")
                val role = Role.valueOf(readln().uppercase())

                println("Enter New Department (IT, MARKETING, FINANCE):")
                val department = Department.valueOf(readln().uppercase())

                println("Enter New Reporting To (Manager ID):")
                val reportingTo = readln()

                val updated = manager.updateEmployee(id, firstName, lastName, role, department, reportingTo)
                println(if (updated) " Employee updated." else " Update failed.")
            }

            "4" -> {
                println("Enter Employee ID to Delete:")
                val id = readln()
                val deleted = manager.deleteEmployee(id)
                println(if (deleted) " Employee deleted." else " Employee not found.")
            }

            "5" -> {
                println("Enter Employee ID:")
                val id = readln()
                println("Enter Check-in Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
                val input = readln()
                val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
                if (time != null) manager.checkIn(id, time)
            }

            "6" -> {
                println("Enter Employee ID:")
                val id = readln()
                println("Enter New Check-in Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
                val input = readln()
                val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
                if (time != null) manager.updateCheckIn(id, time)
            }

            "7" -> {
                println("Enter Employee ID:")
                val id = readln()
                println("Enter Check-in Date (yyyy-MM-dd):")
                val date = parseDate(readln())
                if (date != null) manager.deleteCheckIn(id, date)
            }

            "8" -> {
                println("Enter Employee ID:")
                val id = readln()
                println("Enter Check-out Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
                val input = readln()
                val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
                if (time != null) manager.checkOut(id, time)
            }

            "9" -> {
                println("Enter Employee ID:")
                val id = readln()
                println("Enter New Check-out Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
                val input = readln()
                val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
                if (time != null) manager.updateCheckOut(id, time)
            }

            "10" -> {
                println("Enter Employee ID:")
                val id = readln()
                println("Enter Check-out Date (yyyy-MM-dd):")
                val date = parseDate(readln())
                if (date != null) manager.deleteCheckOut(id, date)
            }

            "11" -> {
                println(" Attendance Records:")
                println(manager.getAllAttendance())
            }

            "12" -> {
                println("Enter Employee ID:")
                val id = readln()

                val records = manager.getAllAttendance().filter { it.employeeId == id }

                if (records.isEmpty()) {
                    println("No attendance records found for $id.")
                } else {
                    println("Worked Hours for $id:")
                    records.forEach {
                        val date = it.dateTimeOfCheckIn.toLocalDate()
                        val hours = it.workedHours ?: "N/A"
                        println("$date: $hours")
                    }
                }
            }

            "13" -> {
                println(" Exiting system. Goodbye!")
                return
            }

            else -> println(" Invalid choice. Please try again.")
        }

        println()
    }
}

// Strict date-time parsing
fun parseDateTime(input: String?): LocalDateTime? {
    if (input.isNullOrBlank()) return null
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return try {
        LocalDateTime.parse(input.trim(), formatter)
    } catch (_: DateTimeParseException) {
        println(" Invalid date-time format. Use yyyy-MM-dd HH:mm")
        null
    }
}

// Strict date-only parsing
fun parseDate(input: String?): LocalDate? {
    if (input.isNullOrBlank()) return null
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return try {
        LocalDate.parse(input.trim(), formatter)
    } catch (_: DateTimeParseException) {
        println(" Invalid date format. Use yyyy-MM-dd")
        null
    }
}
