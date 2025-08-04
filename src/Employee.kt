import data.Role
import data.Department

class Employee(
    val employeeId : String ="",
    val firstName: String,
    val lastName: String,
    val role: Role,
    val department: Department,
    val reportingTo: String
)

{
    fun validateEmployee(): Boolean {
            val nameRegex = Regex("^[A-Za-z]+$")

            return firstName.isNotBlank() &&
                    lastName.isNotBlank() &&
                    reportingTo.isNotBlank() &&
                    nameRegex.matches(firstName) &&
                    nameRegex.matches(lastName)


    }




}
