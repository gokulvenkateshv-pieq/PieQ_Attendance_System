package data

enum class Department (val displayName : String){
    MARKETING("Marketing"),
    FINANCE("Finance"),
    IT("Developer"),
    ADMINISTRATION("Administration"),
    QA("Quality Analyst");

    override fun toString(): String = displayName

}