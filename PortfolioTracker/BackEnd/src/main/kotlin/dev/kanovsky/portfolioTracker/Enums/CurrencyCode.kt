package dev.kanovsky.portfolioTracker.Enums

enum class CurrencyCode(val code: String) {
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    JPY("JPY"),
    KRW("KRW");

    companion object {
        fun fromCode(code: String): CurrencyCode? {
            return entries.find { it.code == code }
        }
    }
}