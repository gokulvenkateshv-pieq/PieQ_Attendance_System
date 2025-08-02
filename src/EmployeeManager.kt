import data.Role
import data.Department

class EmployeeManager {

    // Declared as static (companion object) so it's shared across all instances.
    // If not, every new instance would create its own counter and generate duplicate IDs.
    companion object {
        private var idCounter = 1
        private val employeeList = EmployeeList()

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


}

