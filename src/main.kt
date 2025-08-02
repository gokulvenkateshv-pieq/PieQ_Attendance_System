import data.Role
import data.Department

fun main() {
    val employeeManager = EmployeeManager()
    employeeMenu(employeeManager)
}



fun employeeMenu(manager: EmployeeManager) {
    while (true) {
        println(
            """
            |===========================
            | Employee Management Menu
            |===========================
            | 1. Add Employee
            | 2. View Employees
            | 3. Update Employee
            | 4. Delete Employee
            | 5. Exit
            |===========================
            | Enter your choice: 
            """.trimMargin()
        )

        when (readln().trim()) {
            "1" -> {
                println("Enter First Name:")
                val firstName = readln()

                println("Enter Last Name:")
                val lastName = readln()

                println("Enter Role (Intern, Developer, Manager):")
                val role = Role.valueOf(readln().uppercase())

                println("Enter Department (IT,Marketing,Finance):")
                val department = Department.valueOf(readln().uppercase())

                println("Enter Reporting To (Manager ID):")
                val reportingTo = readln()

                val employee = manager.addEmployee(firstName, lastName, role, department, reportingTo
                )
                if (employee != null) {
                    println("Employee added: $firstName")
                } else {
                    println(" Failed to add employee. Invalid data.")
                }
            }

            "2" -> {
                println("All Employees:")
                println(manager.getAllEmployees())
            }

            "3" -> {
                println("Enter Employee ID to Update:")
                val id = readln()
                val exists = manager.getEmployeeIndex(id)
                if (exists == -1){
                    println(" Employee ID not found.")
                    continue
                }

                println("Enter New First Name:")
                val firstName = readln()

                println("Enter New Last Name:")
                val lastName = readln()

                println("Enter New Role (INTERN, DEVELOPER, MANAGER):")
                val role = Role.valueOf(readln().uppercase())

                println("Enter New Department (IT,Marketing,Finance):")
                val department = Department.valueOf(readln().uppercase())

                println("Enter New Reporting To (Manager ID):")
                val reportingTo = readln()

                val updated = manager.updateEmployee(
                    id, firstName, lastName, role, department, reportingTo
                )

                if (updated) {
                    println("Employee updated.")
                } else {
                    println("Update failed. Employee not found or invalid data.")
                }
            }

            "4" -> {
                println("Enter Employee ID to Delete:")
                val id = readln()
                val deleted = manager.deleteEmployee(id)

                if (deleted) {
                    println(" Employee deleted successfully.")
                } else {
                    println(" Employee not found.")
                }
            }

            "5" -> {
                println(" Exiting... Goodbye!")
                return
            }

            else -> println(" Invalid choice. Please try again.")
        }

        println()
    }
}
