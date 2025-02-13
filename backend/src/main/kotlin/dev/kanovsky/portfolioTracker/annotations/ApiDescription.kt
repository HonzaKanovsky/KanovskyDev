package dev.kanovsky.portfolioTracker.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiDescription(val value: String)