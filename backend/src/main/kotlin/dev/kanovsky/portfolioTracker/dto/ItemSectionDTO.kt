package dev.kanovsky.portfolioTracker.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class ItemSectionDTO @JsonCreator constructor(
    @JsonProperty("sectionTitle") val sectionTitle: String,
    @JsonProperty("resumeItems") val resumeItems: List<ResumeItemDTO>
)
