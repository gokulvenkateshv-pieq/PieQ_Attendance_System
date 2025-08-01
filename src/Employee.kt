import data.Role
import data.Department

class Employee(
    val firstName: String,
    val lastName: String,
    val role: Role,
    val department: Department,
    val reportingTo: String
)
{
    fun validateEmployee(): Boolean {
        return firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                reportingTo.isNotBlank()
    }

}
