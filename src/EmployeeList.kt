class EmployeeList : ArrayList<Employee>() {

    override fun add(element: Employee): Boolean {

        return super.add(element)
    }

    override fun toString(): String {
        if (this.isEmpty()) return "No employees found."

        return this.joinToString(separator = "\n") {
            "${it.employeeId}: ${it.firstName} ${it.lastName} - ${it.role} (${it.department}) reporting to - ${it.reportingTo}"
        }
    }


}
