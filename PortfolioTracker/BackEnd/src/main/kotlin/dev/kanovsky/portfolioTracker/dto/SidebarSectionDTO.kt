package dev.kanovsky.portfolioTracker.dto


class SidebarSectionDTO(
    val title: String,
    val sidebarItems: List<SidebarItemDTO>? = emptyList()
)