import data.Role
import data.Department
import data.Manager
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
            | 13. View Work Summary
            | 14. Exit
            |===============================
            | Enter your choice: 
            """.trimMargin()
        )

        when (readln().trim()) {
            "1" -> handleAddEmployee(manager)
            "2" -> handleViewEmployees(manager)
            "3" -> handleUpdateEmployee(manager)
            "4" -> handleDeleteEmployee(manager)
            "5" -> handleCheckIn(manager)
            "6" -> handleUpdateCheckIn(manager)
            "7" -> handleDeleteCheckIn(manager)
            "8" -> handleCheckOut(manager)
            "9" -> handleUpdateCheckOut(manager)
            "10" -> handleDeleteCheckOut(manager)
            "11" -> handleViewAttendance(manager)
            "12" -> handleViewWorkedHours(manager)
            "13" -> handleViewWorkSummary(manager)
            "14" -> {
                println("Exiting system. Goodbye!")
                return
            }
            else -> println(" Invalid choice. Please try again.")
        }

        println()
    }
}

fun handleAddEmployee(manager: EmployeeManager) {
    println("Enter First Name:")
    val firstName = readln()

    println("Enter Last Name:")
    val lastName = readln()

    println("Enter Role (INTERN, DEVELOPER, MANAGER):")
    val roleInput = readln().uppercase()
    val role = try {
        Role.valueOf(roleInput)
    } catch (_: IllegalArgumentException) {
        println("Invalid role. Valid options: INTERN, DEVELOPER, MANAGER")
        return
    }

    println("Enter Department (IT, MARKETING, FINANCE):")
    val departmentInput = readln().uppercase()
    val department = try {
        Department.valueOf(departmentInput)
    } catch (_: IllegalArgumentException) {
        println("Invalid role. Valid options: IT, MARKETING, FINANCE")
        return
    }

    val reportingTo: String? = if (role == Role.MANAGER) {
        null
    } else {
        Manager.printAll()
        println("Enter Reporting To (Manager ID):")
        val input = readln()
        if (input.isBlank()) {
            println("Reporting To is required for non-manager roles. Employee not added.")
            return
        }
        input
    }



    val employee = manager.addEmployee(firstName, lastName, role, department, reportingTo)
    if (employee != null) {
        println(" Employee added with ID: ${employee.employeeId}")
    } else {
        println(" Failed to add employee. Invalid data.")
    }
}

fun handleViewEmployees(manager: EmployeeManager) {
    println(" All Employees:")
    println(manager.getAllEmployees())
}

fun handleUpdateEmployee(manager: EmployeeManager) {
    println("Enter Employee ID to Update:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }

    println("Enter New First Name:")
    val firstName = readln()

    println("Enter New Last Name:")
    val lastName = readln()

    println("Enter New Role (INTERN, DEVELOPER, MANAGER):")
    val roleInput = readln().uppercase()
    val role = try {
        Role.valueOf(roleInput)
    } catch (_: IllegalArgumentException) {
        println("Invalid role. Valid options: INTERN, DEVELOPER, MANAGER")
        return
    }

    println("Enter New Department (IT, MARKETING, FINANCE):")
    val departmentInput = readln().uppercase()
    val department = try {
        Department.valueOf(departmentInput)
    } catch (_: IllegalArgumentException) {
        println("Invalid role. Valid options: IT, MARKETING, FINANCE")
        return
    }

    val reportingTo: String? = if (role == Role.MANAGER) {
        null
    } else {
        Manager.printAll()
        println("Enter Reporting To (Manager ID):")
        val input = readln()
        if (input.isBlank()) {
            println("Reporting To is required for non-manager roles.")
            return
        }
        input
    }
    val updated = manager.updateEmployee(id, firstName, lastName, role, department, reportingTo)
    println(if (updated) " Employee updated." else " Update failed.")
}

fun handleDeleteEmployee(manager: EmployeeManager) {
    println("Enter Employee ID to Delete:")
    val id = readln()
    val deleted = manager.deleteEmployee(id)
    println(if (deleted) " Employee deleted." else " Employee not found.")
}

fun handleCheckIn(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }
    println("Enter Check-in Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
    val input = readln()
    val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
    if (time != null) manager.checkIn(id, time)
}

fun handleUpdateCheckIn(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }
    println("Enter New Check-in Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
    val input = readln()
    val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
    if (time != null) manager.updateCheckIn(id, time)
}

fun handleDeleteCheckIn(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }
    println("Enter Check-in Date (yyyy-MM-dd):")
    val date = parseDate(readln())
    if (date != null) manager.deleteCheckIn(id, date)
}

fun handleCheckOut(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }
    println("Enter Check-out Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
    val input = readln()
    val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
    if (time != null) manager.checkOut(id, time)
}

fun handleUpdateCheckOut(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }
    println("Enter New Check-out Time (yyyy-MM-dd HH:mm) or press Enter to use current time:")
    val input = readln()
    val time = if (input.isBlank()) LocalDateTime.now() else parseDateTime(input)
    if (time != null) manager.updateCheckOut(id, time)
}

fun handleDeleteCheckOut(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }
    println("Enter Check-out Date (yyyy-MM-dd):")
    val date = parseDate(readln())
    if (date != null) manager.deleteCheckOut(id, date)
}

fun handleViewAttendance(manager: EmployeeManager) {
    println(" Attendance Records:")
    println(manager.getAllAttendance())
}

fun handleViewWorkedHours(manager: EmployeeManager) {
    println("Enter Employee ID:")
    val id = readln()
    if (manager.getEmployeeIndex(id) == -1) {
        println(" Employee ID not found.")
        return
    }

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

fun handleViewWorkSummary(manager: EmployeeManager) {
    println("Enter from date: (yyyy-MM-dd)")
    val from = parseDate(readln())

    println("Enter to date: (yyyy-MM-dd)")
    val to = parseDate(readln())

    if (from != null && to != null) {
        manager.showWorkSummary(from, to)
    } else {
        println("Invalid date(s). Please try again.")
    }
}

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


//implemet working hours should be greater than zero