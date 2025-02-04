package dev.kanovsky.portfolioTracker.dto

import org.springframework.web.multipart.MultipartFile

data class ResumeRequestDTO(
    val name: String,
    val aboutMe: String?,

    val phoneNumber: String,
    val email: String,
    val address: String,
    val website: String,

    val sidebarItemsDTO: String,

    val itemSectionDTO: String,

    var profilePicture: MultipartFile? = null
)

