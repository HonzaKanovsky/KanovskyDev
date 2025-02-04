package dev.kanovsky.portfolioTracker.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ResumeRequestDTO @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("aboutMe") val aboutMe: String,
    @JsonProperty("phoneNumber") val phoneNumber: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("address") val address: String,
    @JsonProperty("website") val website: String,
    @JsonProperty("sidebarItemsDTO") val sidebarItemsDTO: List<SidebarSectionDTO>,
    @JsonProperty("itemSectionDTO") val itemSectionDTO: List<ItemSectionDTO>
)


