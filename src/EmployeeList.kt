

class EmployeeList : ArrayList<Employee>() {

    override fun add(element: Employee): Boolean {

        return super.add(element)
    }

    fun employeeExists(id: String): Boolean {
        return this.any { it.employeeId == id }
    }

    fun deleteEmployee(employeeId: String): Boolean {
        if (!employeeExists(employeeId)) {
            return false
        }
        val employee = this.find { it.employeeId == employeeId }!!
        this.remove(employee)
        return true
    }

    override fun toString(): String {
        if (this.isEmpty()) return "No employees found."

        return this.joinToString(separator = "\n")
    }
}




