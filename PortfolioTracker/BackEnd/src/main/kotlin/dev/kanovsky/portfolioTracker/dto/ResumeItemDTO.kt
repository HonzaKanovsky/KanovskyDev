package dev.kanovsky.portfolioTracker.dto

class ResumeItemDTO(
    val itemName: String,
    val period: String,
    val location: String,
    val skillsLearned: List<String>?
)