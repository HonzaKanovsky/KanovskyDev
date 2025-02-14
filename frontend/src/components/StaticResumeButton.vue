<script setup>
import axios from "axios";
import { ref } from "vue";
import { PhDownloadSimple } from "@phosphor-icons/vue"

const resumeData = ref(null);

const loadResumeData = async () => {
    const response = await fetch(new URL("@/assets/resume.json", import.meta.url));
    resumeData.value = await response.json();
};

const submitResume = async () => {

    if (!resumeData.value) {
        await loadResumeData();
    }
    
    // Create FormData object
    const formData = new FormData();

    const imageUrl = new URL('@/assets/img/IMG_8561_cropped.jpg', import.meta.url).href;
    const jsonBlob = new Blob([JSON.stringify(resumeData.value)], { type: "application/json" });

    const response = await fetch(imageUrl);
    const blob = await response.blob();

    const file = new File([blob], "profile.jpg", { type: "image/jpeg" });

    formData.append("resumeRequestDTO", jsonBlob, "resumeRequestDTO.json");
    formData.append("profilePicture", file);

    for (let pair of formData.entries()) {
        if (pair[1] instanceof Blob) {
            console.log(`${pair[0]}: (Blob of type ${pair[1].type}, size: ${pair[1].size} bytes)`);
        } else {
            console.log(`${pair[0]}:`, pair[1]);
        }
    }


    try {
        const response = await axios.post("http://localhost:8080/api/resume", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
            responseType: "blob",
        });
        console.log("Success:", response);

        if (response.data.size === 0) {
            console.error("Received empty PDF file");
            return;
        }

        const blob = new Blob([response.data], { type: "application/pdf" });
        const link = document.createElement("a");
        link.href = window.URL.createObjectURL(blob);
        link.download = "resume.pdf";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(link.href);
    } catch (error) {
        console.error("Error downloading PDF:", error);
    }
};

</script>

<template>
    <section>
        <button @click="submitResume" class="text-1xl text-white bg-blue-800 mr-0 flex flex-row py-1 px-2 rounded-md">
            <PhDownloadSimple class="my-auto mr-2" />
            Download Resume
        </button>
    </section>
</template>