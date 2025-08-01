import data.Department
import data.Role

fun main()
{
    val emp = Employee("","venkat", Role.INTERN, Department.IT,"sa001")
    val boole = emp.validateEmployee()
    print(boole)
}