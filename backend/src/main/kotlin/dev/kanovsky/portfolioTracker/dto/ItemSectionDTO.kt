package dev.kanovsky.portfolioTracker.dto


data class ItemSectionDTO(
    val sectionTitle: String,
    val resumeItems: List<ResumeItemDTO>
)
