package data

enum class Department (val displayName : String){
    MARKETING("Marketing"),
    FINANCE("Finance"),
    IT("IT");

    override fun toString(): String = displayName

}