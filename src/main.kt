import data.Role
import data.Department
import data.Manager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseDateTime(input: String): LocalDateTime? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        LocalDateTime.parse(input, formatter)
    } catch (_: Exception) {
        null
    }
}

fun main() {
    val manager = EmployeeManager()

    while (true) {
        println(
            """
            |========= EMPLOYEE SYSTEM =========
            |1. Add Employee
            |2. Delete Employee
            |3. Show All Employees
            |4. Check In
            |5. Check Out
            |6. Show All Attendance
            |7. Delete Attendance Record
            |8. Exit
            |==================================
        """.trimMargin()
        )

        print("Enter choice: ")
        when (readln().trim()) {
            "1" -> {
                print("First Name: ")
                val fName = readln()
                print("Last Name: ")
                val lName = readln()
                print("Role (INTERN, DEVELOPER, MANAGER): ")
                val role = Role.valueOf(readln().uppercase())
                print("Department (Marketing, IT, FINANCE): ")
                val dept = Department.valueOf(readln().uppercase())

                Manager.printAll()
                print("Enter Reporting Manager ID (or press enter if none): ")
                val mgrId = readln()
                val managerObj = if (mgrId.isNotBlank()) {
                    Manager.entries.find { it.id.equals(mgrId, ignoreCase = true) }
                } else null

                val empId = manager.addEmployee(fName, lName, role, dept, managerObj)
                if (empId != null) println("Employee added with ID: $empId")
                else println("Invalid input. Try again.")
            }

            "2" -> {
                print("Enter Employee ID to delete: ")
                val id = readln()
                val exists = manager.employeeExists(id)
                if(exists) {
                    manager.deleteEmployee(id)
                    println("Employee deleted.")
                }else{
                    println("employee not found")
                }
            }

            "3" -> {
                println("=== All Employees ===")
                println(manager.getAllEmployees())
            }

            "4" -> {
                print("Enter Employee ID to Check-In: ")
                val id = readln()
                val exists = manager.employeeExists(id)
                if(exists) {
                    print("Enter Check-In Date-Time (yyyy-MM-dd HH:mm): or press enter for currentDateTime")
                    val dateInput = readln()
                    val date = parseDateTime(dateInput)
                    if ( manager.checkIn(id, date)) {
                        println("Checked in successfully.")
                    } else {
                        println("Check-in failed. Invalid input.")
                    }
                }else{
                    println("employee not found")
                }
            }

            "5" -> {
                print("Enter Employee ID to Check-Out: ")
                val id = readln()
                val exists = manager.employeeExists(id)
                if(exists) {
                print("Enter Check-Out Date-Time (yyyy-MM-dd HH:mm): or press enter for currentDateTime ")
                val dateInput = readln()
                val dateTime = parseDateTime(dateInput)
                if (manager.checkOut(id, dateTime)) {
                    println("Checked out successfully.")
                } else {
                    println("Check-out failed. Please check input.")
                }}
                else{
                    println("employee not found")
                }
            }

            "6" -> {
                println("=== Attendance Records ===")
                println(manager.getAllAttendance())
            }

            "7" -> {
                print("Enter Employee ID: ")
                val id = readln()
                val exists = manager.employeeExists(id)
                if(exists) {
                print("Enter Check-In Date-Time (yyyy-MM-dd HH:mm): ")
                val dateInput = readln()
                val dateTime = parseDateTime(dateInput)
                if (dateTime != null && manager.deleteAttendance(id, dateTime.toLocalDate())) {
                    println("Record deleted.")
                } else {
                    println("Invalid input.")
                }}
                else{
                    print("employee not found")
                }
            }

            "8" -> {
                println("Exiting... Goodbye!")
                return
            }

            else -> println("Invalid option. Try again.")
        }

        println("\n----------------------------------\n")
    }
}
