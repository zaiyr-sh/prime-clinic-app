package kg.iaau.diploma.data

data class Authorization(
    val username: String,
    val password: String,
    val role: Type = Type.USER
)

enum class Type {
    USER, CUSTOMER, ADMIN
}