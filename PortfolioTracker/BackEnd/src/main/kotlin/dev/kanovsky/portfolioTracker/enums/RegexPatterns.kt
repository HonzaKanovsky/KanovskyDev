package dev.kanovsky.portfolioTracker.enums

import java.util.regex.Pattern

enum class RegexPatterns(val pattern: String) {
    USERNAME("^[A-Za-z\\d]+$"),
    EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
    PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{6,}$");

    fun matches(input: String): Boolean {
        return Pattern.compile(pattern).matcher(input).matches()
    }
}