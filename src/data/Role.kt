package data

enum class Role(val displayName: String) {
    INTERN("Intern"),
    DEVELOPER("Developer"),
    MANAGER("Manager");

    override fun toString(): String = displayName
}