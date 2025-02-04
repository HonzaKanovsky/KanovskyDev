package dev.kanovsky.portfolioTracker.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class ResumeItemDTO @JsonCreator constructor(
    @JsonProperty("itemName") val itemName: String,
    @JsonProperty("period") val period: String,
    @JsonProperty("location") val location: String,
    @JsonProperty("skillsLearned") val skillsLearned: List<String>?
)