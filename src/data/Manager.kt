package data

enum class Manager(val id: String, val fullName: String) {

    TS211("ts211", "Tony Stark"),
    SA112("sa112", "Sam Alex"),
    RK312("rk312", "Ravi Kumar");


    override fun toString(): String = "$fullName ($id)"

    companion object {
        fun fromId(id: String): Manager? {
            return entries.find { it.id.equals(id.trim(), ignoreCase = true) }
        }

        fun printAll() {
            println("Available Managers:")
            entries.forEach { println("- $it") }
        }
    }
}
