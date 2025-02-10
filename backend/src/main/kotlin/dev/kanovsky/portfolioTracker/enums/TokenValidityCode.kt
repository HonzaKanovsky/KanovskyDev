package dev.kanovsky.portfolioTracker.enums

enum class TokenValidityCode(val seconds: Int) {
    ONE_DAY(24 * 60 * 60),
    ONE_HOUR(60 * 60),
    ZERO(0),
}