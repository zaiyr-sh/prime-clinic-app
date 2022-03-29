package kg.iaau.diploma.core.constants

enum class UserType {
    ADMIN, DOCTOR
}

enum class MessageType(val type: String) {
    TEXT("text"), IMAGE("image")
}