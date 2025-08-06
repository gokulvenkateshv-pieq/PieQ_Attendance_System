import data.Role
import data.Department
import data.Manager

class Employee(
    val firstName: String,
    val lastName: String,
    val role: Role,
    val department: Department,
    val reportingTo: Manager?
) {
     var employeeId: String=""

    companion object {
        private var idCounter = 100
    }

    fun validate(): Boolean {
        val nameRegex = Regex("^[A-Za-z]+$")
        val isValid = firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                nameRegex.matches(firstName) &&
                nameRegex.matches(lastName)

        if (isValid) {
            employeeId = "${firstName.first().uppercase()}${lastName.first().uppercase()}$idCounter"
            idCounter++
        }

        return isValid
    }

    override fun toString(): String {
        val name = "$firstName $lastName"
        val reportsToValue = reportingTo ?: "N/A"
        return "ID: $employeeId, Name: $name, Role: $role ($department), Reports To: $reportsToValue"
    }
}

