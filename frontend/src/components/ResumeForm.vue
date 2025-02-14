<script setup>
import { ref, watch } from "vue";
import axios from "axios";

// Resume Data
const resumeData = ref({
    name: "",
    aboutMe: "",
    phoneNumber: "",
    email: "",
    address: "",
    website: "",
    sidebarItemsDTO: [
        { title: "Skills", sidebarItems: [{ sidebarItemName: "" }] },
        { title: "Languages", sidebarItems: [{ sidebarItemName: "", sidebarItemLevel: "" }] },
    ],
    itemSectionDTO: [
        { sectionTitle: "Experience", resumeItems: [{ itemName: "", period: "", location: "", skillsLearned: [""] }] },
        { sectionTitle: "Education", resumeItems: [{ itemName: "", period: "", location: "" }] },
    ],
});

// Profile Picture
const profilePicture = ref(null);
const profilePicturePreview = ref("");

// Watch for file changes and create an object URL
watch(profilePicture, (newFile) => {
    if (newFile) {
        if (profilePicturePreview.value) {
            URL.revokeObjectURL(profilePicturePreview.value); // Prevent memory leaks
        }
        profilePicturePreview.value = URL.createObjectURL(newFile);
    }
});

// Functions to dynamically add fields
const addSkill = () => resumeData.value.sidebarItemsDTO[0].sidebarItems.push({ sidebarItemName: "" });
const addLanguage = () => resumeData.value.sidebarItemsDTO[1].sidebarItems.push({ sidebarItemName: "", sidebarItemLevel: "" });
const addExperience = () => resumeData.value.itemSectionDTO[0].resumeItems.push({ itemName: "", period: "", location: "", skillsLearned: [""] });
const addEducation = () => resumeData.value.itemSectionDTO[1].resumeItems.push({ itemName: "", period: "", location: "" });

// Handle file selection
const handleFileUpload = (event) => {
    profilePicture.value = event.target.files[0];
};

// Submit form data to Spring Boot API
const submitForm = async () => {
    try {
        const jsonData = JSON.stringify(resumeData.value, null, 2);

        const formData = new FormData();
        formData.append("resumeRequestDTO", new Blob([jsonData], { type: "application/json" }));

        if (profilePicture.value) {
            formData.append("profilePicture", profilePicture.value);
        }

        const response = await axios.post("http://localhost:8080/api/resume", formData, {
            headers: { "Content-Type": "multipart/form-data" },
            responseType: "blob",
        });

        if (response.data.size === 0) {
            console.error("Received empty PDF file");
            return;
        }

        const pdfBlob = new Blob([response.data], { type: "application/pdf" });
        const link = document.createElement("a");
        link.href = window.URL.createObjectURL(pdfBlob);
        link.download = "resume.pdf";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(link.href);
    } catch (error) {
        console.error("Error uploading resume:", error);
    }
};
</script>


<template>
    <section class="flex bg-slate-50 py-10 gap-6">
        <form @submit.prevent="submitForm" class="p-6 ml-[5%] bg-white shadow-md rounded-md max-w-3xl">
            <h2 class="text-2xl font-bold mb-4">Resume Form</h2>

            <!-- Basic Info -->
            <div class="mb-4">
                <label class="block font-semibold">Full Name</label>
                <input v-model="resumeData.name" class="w-full border px-3 py-2 rounded-md" required />
            </div>

            <div class="mb-4">
                <label class="block font-semibold">About Me</label>
                <textarea v-model="resumeData.aboutMe" class="w-full border px-3 py-2 rounded-md" required></textarea>
            </div>

            <div class="mb-4 grid grid-cols-2 gap-4">
                <div>
                    <label class="block font-semibold">Phone</label>
                    <input v-model="resumeData.phoneNumber" class="w-full border px-3 py-2 rounded-md" required />
                </div>
                <div>
                    <label class="block font-semibold">Email</label>
                    <input v-model="resumeData.email" type="email" class="w-full border px-3 py-2 rounded-md"
                        required />
                </div>
            </div>

            <div class="mb-4">
                <label class="block font-semibold">Address</label>
                <input v-model="resumeData.address" class="w-full border px-3 py-2 rounded-md" required />
            </div>

            <div class="mb-4">
                <label class="block font-semibold">Website</label>
                <input v-model="resumeData.website" class="w-full border px-3 py-2 rounded-md" />
            </div>

            <!-- Skills Section -->
            <div class="mb-4">
                <h3 class="text-xl font-bold mb-2">Skills</h3>
                <div v-for="(skill, index) in resumeData.sidebarItemsDTO[0].sidebarItems" :key="'skill-' + index"
                    class="flex gap-2 mb-2">
                    <input v-model="skill.sidebarItemName" class="w-full border px-3 py-2 rounded-md"
                        placeholder="Skill Name" />
                </div>
                <button type="button" @click="addSkill" class="text-blue-500">+ Add Skill</button>
            </div>

            <!-- Languages Section -->
            <div class="mb-4">
                <h3 class="text-xl font-bold mb-2">Languages</h3>
                <div v-for="(lang, index) in resumeData.sidebarItemsDTO[1].sidebarItems" :key="'lang-' + index"
                    class="flex gap-2 mb-2">
                    <input v-model="lang.sidebarItemName" class="border px-3 py-2 rounded-md w-1/2"
                        placeholder="Language" />
                    <input v-model="lang.sidebarItemLevel" class="border px-3 py-2 rounded-md w-1/2"
                        placeholder="Proficiency Level" />
                </div>
                <button type="button" @click="addLanguage" class="text-blue-500">+ Add Language</button>
            </div>

            <!-- Experience Section -->
            <div class="mb-4">
                <h3 class="text-xl font-bold mb-2">Experience</h3>
                <div v-for="(exp, index) in resumeData.itemSectionDTO[0].resumeItems" :key="'exp-' + index"
                    class="mb-3 border p-3 rounded-md">
                    <input v-model="exp.itemName" class="w-full border px-3 py-2 rounded-md mb-2"
                        placeholder="Job Title" required />
                    <input v-model="exp.period" class="w-full border px-3 py-2 rounded-md mb-2"
                        placeholder="Period (e.g., 2020 - 2023)" required />
                    <input v-model="exp.location" class="w-full border px-3 py-2 rounded-md mb-2"
                        placeholder="Company Name" required />

                    <label class="block font-semibold">Skills Learned</label>
                    <textarea v-model="exp.skillsLearned" class="w-full border px-3 py-2 rounded-md"
                        placeholder="Enter skills, separated by commas"
                        @input="exp.skillsLearned = exp.skillsLearned.split(',').map(skill => skill.trim())"></textarea>
                </div>
                <button type="button" @click="addExperience" class="text-blue-500">+ Add Experience</button>
            </div>


            <!-- Education Section -->
            <div class="mb-4">
                <h3 class="text-xl font-bold mb-2">Education</h3>
                <div v-for="(edu, index) in resumeData.itemSectionDTO[1].resumeItems" :key="'edu-' + index"
                    class="mb-3 border p-3 rounded-md">
                    <input v-model="edu.itemName" class="w-full border px-3 py-2 rounded-md mb-2" placeholder="Degree"
                        required />
                    <input v-model="edu.period" class="w-full border px-3 py-2 rounded-md mb-2"
                        placeholder="Period (e.g., 2020 - 2023)" required />
                    <input v-model="edu.location" class="w-full border px-3 py-2 rounded-md"
                        placeholder="University/Institution" required />
                </div>
                <button type="button" @click="addEducation" class="text-blue-500">+ Add Education</button>
            </div>

            <!-- Profile Picture Upload -->
            <div class="mb-4">
                <label class="block font-semibold">Upload Profile Picture</label>
                <input type="file" @change="handleFileUpload" class="border px-3 py-2 rounded-md" />
            </div>

            <!-- Submit Button -->
            <button type="submit" class="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
                Submit Resume
            </button>
        </form>

        <!-- Live Resume Preview -->
        <div class="w-1/2 p-6 min-h-screen border-l mr-[5%] bg-white shadow-md rounded-md">
            <h2 class="text-2xl font-bold mb-4 bg">Resume Preview</h2>
            <div class="h-auto">
                <div class="container border p-6 shadow-md">
                    <!-- Sidebar -->
                    <div class="sidebar">
                        <img v-if="profilePicturePreview" class="profile-pic" :src="profilePicturePreview" />
                        <h1 class="person-name">{{ resumeData.name || "Your Name" }}</h1>

                        <!-- Contact Info -->
                        <div class="sidebar-section">
                            <h2>Contact</h2>
                            <hr class="sidebar-separator" />
                            <p v-if="resumeData.phoneNumber">📞 {{ resumeData.phoneNumber }}</p>
                            <p v-if="resumeData.email">✉️ {{ resumeData.email }}</p>
                            <p v-if="resumeData.address">📍 {{ resumeData.address }}</p>
                            <p v-if="resumeData.website">🌍 <a :href="resumeData.website" class="text-blue-500">{{
                                resumeData.website }}</a></p>
                        </div>

                        <!-- Skills -->
                        <div class="sidebar-section">
                            <h2>Skills</h2>
                            <hr class="sidebar-separator" />
                            <ul>
                                <li class="skill" v-for="(skill, index) in resumeData.sidebarItemsDTO[0].sidebarItems"
                                    :key="index">
                                    {{ skill.sidebarItemName }}
                                </li>
                            </ul>
                        </div>

                        <!-- Languages -->
                        <div class="sidebar-section">
                            <h2>Languages</h2>
                            <hr class="sidebar-separator" />
                            <ul>
                                <li v-for="(lang, index) in resumeData.sidebarItemsDTO[1].sidebarItems" :key="index">
                                    {{ lang.sidebarItemName }} <span v-if="lang.sidebarItemLevel">- {{
                                        lang.sidebarItemLevel }}</span>
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
                            <div class="job" v-for="(item, i) in section.resumeItems" :key="i">
                                <h4 v-if="item.period">{{ item.period }}</h4>
                                <h3 v-if="item.itemName">{{ item.itemName }}</h3>
                                <h5 v-if="item.location">{{ item.location }}</h5>

                                <!-- Skills Learned Section -->
                                <ul v-if="item.skillsLearned && item.skillsLearned.length">
                                    <li v-for="(skill, j) in item.skillsLearned" :key="j">
                                        {{ skill }}
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </section>

</template>

<style>
@page {
    size: A4;
    margin: 0;
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
    font-size: x-large;
    margin-top: 5px;
}

h3 {
    margin: 0;
    font-weight: bold;
    font-size: large;

}

h4 {
    margin-bottom: 3px;
    color: #06548c;
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
    list-style-type: disc;
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
    margin: 5px 20px 10px 0px;
}

.sidebar-separator {
    border: none;
    height: 1px;
    background-color: white;
    margin: 5px 0px 10px 0px;
}

.sidebar-section {
    margin: 40px 0px 0px 0px;
}
.job{
    margin-top: 10px;
}

</style>
