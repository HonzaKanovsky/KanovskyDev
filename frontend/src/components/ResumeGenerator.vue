<script setup>
import { ref } from "vue";
import ResumeForm from "@/components/ResumeForm.vue";
import ResumePreview from "@/components/ResumePreview.vue";

// Resume Data
const resumeData = ref({
  name: "",
  aboutMe: "",
  phoneNumber: "",
  email: "",
  address: "",
  website: "",
  sidebarItemsDTO: [{ title: "Skills", sidebarItems: [{ sidebarItemName: "" }] }],
  itemSectionDTO: [
    { sectionTitle: "Experience", resumeItems: [{ itemName: "", period: "", location: "", skillsLearned: [""] }] },
    { sectionTitle: "Education", resumeItems: [{ itemName: "", period: "", location: "" }] }
  ]
});

// Profile Picture
const profilePicture = ref(null);
</script>

<template>
  <div>
    <ResumeForm v-model:resumeData="resumeData" v-model:profilePicture="profilePicture" />
    <ResumePreview  :resumeData="resumeData" :profilePicture="profilePicture" />
  </div>
</template>
