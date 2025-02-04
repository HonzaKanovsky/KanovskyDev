package dev.kanovsky.portfolioTracker.dto


import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class SidebarSectionDTO @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("sidebarItems") val sidebarItems: List<SidebarItemDTO>
)