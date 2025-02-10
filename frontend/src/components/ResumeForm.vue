<script setup>
import { ref } from "vue";
import axios from "axios";

// Resume Data
const resumeData = ref({
    name: "",
    aboutMe: "",
    phoneNumber: "",
    email: "",
    address: "",
    website: "",
    sidebarItemsDTO: [{ title: "Skills", sidebarItems: [] }],
    itemSectionDTO: [
        { sectionTitle: "Experience", resumeItems: [] },
        { sectionTitle: "Education", resumeItems: [] },
    ],
});

const profilePicture = ref(null);
const loading = ref(false);
const errorMessage = ref("");

// Handle File Upload
const handleFileUpload = (event) => {
    profilePicture.value = event.target.files[0];
};

// **SKILLS SECTION**
const newSkill = ref("");
const addSkill = () => {
    if (newSkill.value.trim()) {
        resumeData.value.sidebarItemsDTO[0].sidebarItems.push({ sidebarItemName: newSkill.value });
        newSkill.value = "";
    }
};
const removeSkill = (index) => {
    resumeData.value.sidebarItemsDTO[0].sidebarItems.splice(index, 1);
};

// **EXPERIENCE SECTION**
const newExperience = ref({ itemName: "", period: "", location: "", skillsLearned: [] });
const newExperienceSkill = ref("");
const addExperienceSkill = () => {
    if (newExperienceSkill.value.trim()) {
        newExperience.value.skillsLearned.push(newExperienceSkill.value);
        newExperienceSkill.value = "";
    }
};
const addExperience = () => {
    if (newExperience.value.itemName && newExperience.value.period) {
        resumeData.value.itemSectionDTO[0].resumeItems = [
            ...resumeData.value.itemSectionDTO[0].resumeItems,
            { ...newExperience.value }
        ]; // Create a new array reference
        newExperience.value = { itemName: "", period: "", location: "", skillsLearned: [] };
    }
};
const removeExperience = (index) => {
    resumeData.value.itemSectionDTO[0].resumeItems = resumeData.value.itemSectionDTO[0].resumeItems.filter((_, i) => i !== index);
};

// **EDUCATION SECTION**
const newEducation = ref({ itemName: "", period: "", location: "", skillsLearned: [] });
const newEducationSkill = ref("");
const addEducationSkill = () => {
    if (newEducationSkill.value.trim()) {
        newEducation.value.skillsLearned.push(newEducationSkill.value);
        newEducationSkill.value = "";
    }
};
const addEducation = () => {
    if (newEducation.value.itemName && newEducation.value.period) {
        resumeData.value.itemSectionDTO[1].resumeItems = [
            ...resumeData.value.itemSectionDTO[1].resumeItems,
            { ...newEducation.value }
        ]; // Create a new array reference
        newEducation.value = { itemName: "", period: "", location: "", skillsLearned: [] };
    }
};
const removeEducation = (index) => {
    resumeData.value.itemSectionDTO[1].resumeItems = resumeData.value.itemSectionDTO[1].resumeItems.filter((_, i) => i !== index);
};

// Submit Resume and Download PDF
const generateResume = async () => {
    if (!profilePicture.value) {
        errorMessage.value = "Please upload a profile picture.";
        return;
    }

    loading.value = true;
    errorMessage.value = "";

    try {
        const formData = new FormData();
        formData.append("resumeRequestDTO", new Blob([JSON.stringify(resumeData.value)], { type: "application/json" }));
        formData.append("profilePicture", profilePicture.value);

        const response = await axios.post("http://localhost:8080/api/resume", formData, {
            headers: { "Content-Type": "multipart/form-data" },
            responseType: "blob",
        });

        const url = window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "resume.pdf");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } catch (error) {
        errorMessage.value = "Failed to generate resume. Please try again.";
        console.error(error);
    } finally {
        loading.value = false;
    }
};
</script>

<template>
    <section class="max-w-3xl mx-auto bg-white shadow-lg rounded-lg p-6">
        <h2 class="text-2xl font-semibold mb-4">Resume Generator</h2>

        <!-- Error Message -->
        <p v-if="errorMessage" class="text-red-500 mb-4">{{ errorMessage }}</p>

        <!-- Resume Form -->
        <form @submit.prevent="generateResume">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block text-gray-700">Name:</label>
                    <input type="text" v-model="resumeData.name" class="w-full border p-2 rounded-md" required />
                </div>
                <div>
                    <label class="block text-gray-700">Email:</label>
                    <input type="email" v-model="resumeData.email" class="w-full border p-2 rounded-md" required />
                </div>
                <div>
                    <label class="block text-gray-700">Phone Number:</label>
                    <input type="tel" v-model="resumeData.phoneNumber" class="w-full border p-2 rounded-md" required />
                </div>
                <div>
                    <label class="block text-gray-700">Address:</label>
                    <input type="text" v-model="resumeData.address" class="w-full border p-2 rounded-md" required />
                </div>
                <div class="md:col-span-2">
                    <label class="block text-gray-700">About Me:</label>
                    <textarea v-model="resumeData.aboutMe" class="w-full border p-2 rounded-md h-20"></textarea>
                </div>
            </div>

            <!-- SKILLS SECTION -->
            <div class="mt-4">
                <label class="block text-gray-700">Skills:</label>
                <div class="flex gap-2">
                    <input type="text" v-model="newSkill" class="border p-2 rounded-md flex-grow" />
                    <button @click="addSkill" type="button" class="bg-green-500 text-white px-3 py-1 rounded-md">+</button>
                </div>
                <ul class="mt-2">
                    <li v-for="(skill, index) in resumeData.sidebarItemsDTO[0].sidebarItems" :key="index"
                        class="flex justify-between bg-gray-100 p-2 rounded-md mt-1">
                        {{ skill.sidebarItemName }}
                        <button @click="removeSkill(index)" class="text-red-500">x</button>
                    </li>
                </ul>
            </div>

            <!-- EXPERIENCE SECTION -->
            <div class="mt-4">
                <label class="block text-gray-700">Experience:</label>
                <div class="grid grid-cols-3 gap-2">
                    <input type="text" v-model="newExperience.itemName" placeholder="Job Title" class="border p-2 rounded-md" />
                    <input type="text" v-model="newExperience.period" placeholder="Duration" class="border p-2 rounded-md" />
                    <input type="text" v-model="newExperience.location" placeholder="Company" class="border p-2 rounded-md" />
                    <input type="text" v-model="newExperienceSkill" placeholder="Skill Learned" class="border p-2 rounded-md col-span-2" />
                    <button @click="addExperienceSkill" type="button" class="bg-green-500 text-white px-3 py-1 rounded-md">+</button>
                </div>
                <button @click="addExperience" type="button" class="bg-green-500 text-white px-3 py-1 rounded-md mt-2">Add</button>
            </div>

            <!-- EDUCATION SECTION -->
            <div class="mt-4">
                <label class="block text-gray-700">Education:</label>
                <div class="grid grid-cols-3 gap-2">
                    <input type="text" v-model="newEducation.itemName" placeholder="Degree" class="border p-2 rounded-md" />
                    <input type="text" v-model="newEducation.period" placeholder="Duration" class="border p-2 rounded-md" />
                    <input type="text" v-model="newEducation.location" placeholder="Institution" class="border p-2 rounded-md" />
                    <input type="text" v-model="newEducationSkill" placeholder="Skill Learned" class="border p-2 rounded-md col-span-2" />
                    <button @click="addEducationSkill" type="button" class="bg-green-500 text-white px-3 py-1 rounded-md">+</button>
                </div>
                <button @click="addEducation" type="button" class="bg-green-500 text-white px-3 py-1 rounded-md mt-2">Add</button>
            </div>

            <!-- File Upload -->
            <div class="mt-4">
                <label class="block text-gray-700">Profile Picture:</label>
                <input type="file" @change="handleFileUpload" accept="image/*" class="w-full border p-2 rounded-md" />
            </div>

            <button type="submit" class="mt-4 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition">
                {{ loading ? "Generating..." : "Generate Resume" }}
            </button>
        </form>
    </section>
</template>
