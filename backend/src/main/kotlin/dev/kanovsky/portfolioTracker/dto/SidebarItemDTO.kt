package dev.kanovsky.portfolioTracker.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SidebarItemDTO @JsonCreator constructor(
    @JsonProperty("sidebarItemName") val sidebarItemName: String,
    @JsonProperty("sidebarItemLevel") val sidebarItemLevel: String?,
    @JsonProperty("sidebarItemDescription") val sidebarItemDescription: String?
)