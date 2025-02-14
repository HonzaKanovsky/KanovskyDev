<script setup>
import { computed } from "vue";

// Props
const props = defineProps(["resumeData", "profilePicture"]);

// Profile Picture Preview
const profilePicturePreview = computed(() =>
    props.profilePicture ? URL.createObjectURL(props.profilePicture) : ""
);
</script>

<template>
    <div class="w-1/2 p-6 min-h-screen border-l bg-white">
        <div class="container border p-6 shadow-md bg-gray-50">
            <!-- Sidebar -->
            <div class="sidebar">
                <img v-if="profilePicturePreview" class="profile-pic" :src="profilePicturePreview" />

                <h1 class="person-name">{{ resumeData.name || "Your Name" }}</h1>

                <div class="sidebar-section">
                    <h2>Contact</h2>
                    <hr class="sidebar-separator" />
                    <p v-if="resumeData.phoneNumber">📞 {{ resumeData.phoneNumber }}</p>
                    <p v-if="resumeData.email">✉️ {{ resumeData.email }}</p>
                    <p v-if="resumeData.address">📍 {{ resumeData.address }}</p>
                    <p v-if="resumeData.website">🌍 <a :href="resumeData.website" class="text-blue-500">{{
                            resumeData.website }}</a></p>
                </div>

                <div v-for="(section, index) in resumeData.sidebarItemsDTO" :key="index" class="sidebar-section">
                    <h2>{{ section.title }}</h2>
                    <hr class="sidebar-separator" />
                    <ul>
                        <li v-for="(item, i) in section.sidebarItems" :key="i" class="skill">
                            ✔ {{ item.sidebarItemName }} <span v-if="item.sidebarItemLevel"> - {{ item.sidebarItemLevel
                                }}</span>
                        </li>
                    </ul>
                </div>
            </div>

            <!-- Main Content -->
            <div class="main-content">
                <div v-if="resumeData.aboutMe">
                    <h2>Professional Summary</h2>
                    <hr class="main-section-separator" />
                    <p>{{ resumeData.aboutMe }}</p>
                </div>

                <div v-for="(section, index) in resumeData.itemSectionDTO" :key="index">
                    <h2>{{ section.sectionTitle }}</h2>
                    <hr class="main-section-separator" />
                    <div v-for="(item, i) in section.resumeItems" :key="i" class="job">
                        <h4 v-if="item.period">{{ item.period }}</h4>
                        <h3 v-if="item.itemName">{{ item.itemName }}</h3>
                        <h5 v-if="item.location">{{ item.location }}</h5>
                        <ul v-if="item.skillsLearned">
                            <li v-for="(skill, j) in item.skillsLearned" :key="j">{{ skill }}</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style>
@page {
    size: A4;
    margin: 0;
}

@font-face {
    font-family: 'TitilliumWeb-Light';
    src: url('/fonts/TitilliumWeb-Light.ttf') format('truetype');
}

body {
    font-family: 'TitilliumWeb-Light', sans-serif;
    margin: 0;
    padding: 0;
    width: 210mm;
    height: 297mm;
}

.container {
    display: table;
    table-layout: fixed;
    width: 100%;
    height: 100%;
    border-collapse: collapse;
}

.sidebar {
    display: table-cell;
    width: 40%;
    background: #06548c;
    color: white;
    padding: 20px;
    vertical-align: top;
}

.main-content {
    display: table-cell;
    width: 70%;
    padding: 30px 20px 20px 20px;
    vertical-align: top;
}

h2 {
    margin: 0;
    font-weight: lighter;
}

h3 {
    margin: 0;
    font-weight: bold;
}

h4 {
    margin-bottom: 3px;
    color: #06548c;
    font-weight: lighter;
}

h5 {
    margin-top: 3px;
    margin-bottom: 3px;
    font-weight: normal;
    font-size: medium;
}

.profile-pic {
    width: 60%;
    object-fit: cover;
    border: white solid;
}

.skill,
.language {
    font-weight: bold;
    /*
                        background: rgba(255, 255, 255, 0.2);
                        padding: 5px 10px;
                        border-radius: 5px;
                        display: inline-block;
                        font-size: 14px;
                        margin: 5px 0;
            */
}


.contact-info p {
    margin: 10px 0;
}

ul {
    padding-left: 20px;
}

a {
    color: #06548c;
    text-decoration: none;
}

.person-name {
    font-weight: lighter;
    font-size: 50px;
    max-width: 100%;
    /* Ensure it does not overflow */
    word-wrap: break-word;
    overflow-wrap: break-word;
    line-height: 1.2;
    white-space: normal;
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-top: 10px;
}

.main-section-separator {
    border: none;
    height: 1px;
    background-color: #06548c;
    margin: 10px 20px 0px 0px;
}

.sidebar-separator {
    border: none;
    height: 1px;
    background-color: white;
    margin: 15px 0px 20px 0px;
}

.sidebar-section {
    margin: 40px 0px 0px 0px;
}
</style>
