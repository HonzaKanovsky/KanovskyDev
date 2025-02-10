<script setup>
import axios from "axios";
import { PhDownloadSimple } from "@phosphor-icons/vue"

const submitResume = async () => {
    const resumeData = {
        name: "Jan Kanovsky",
        aboutMe:
            "Software Developer with 4+ years of experience in backend development, specializing in Spring Boot web applications. Passionate about building efficient, user-friendly solutions that enhance everyday experiences.",
        phoneNumber: "+420 739 898 648",
        email: "honza@kanovsky.net",
        address: "Prague, Czech Republic",
        website: "www.kanovsky.dev",
        sidebarItemsDTO: [
            {
                title: "Skills",
                sidebarItems: [
                    { sidebarItemName: "Java" },
                    { sidebarItemName: "Kotlin" },
                    { sidebarItemName: "Spring Boot" },
                    { sidebarItemName: "Docker" },
                ],
            },
            {
                title: "Languages",
                sidebarItems: [
                    { sidebarItemName: "Czech", sidebarItemLevel: "Native" },
                    {
                        sidebarItemName: "English",
                        sidebarItemLevel: "Advanced",
                        sidebarItemDescription: "TOEFL iBT Score: 100, 20.01.2024",
                    },
                ],
            },
        ],
        itemSectionDTO: [
            {
                sectionTitle: "Experience",
                resumeItems: [
                    {
                        itemName: "Backend Developer",
                        period: "April 2021 - August 2024",
                        location: "Seyfor a.s.",
                        skillsLearned: [
                            "Development of web application for customs",
                            "Experience with Microsoft Azure",
                            "Creating system specification",
                            "Business analysis",
                        ],
                    },
                    {
                        itemName: "Backend Developer",
                        period: "February 2020 - April 2021",
                        location: "MOPET CZ",
                        skillsLearned: [
                            "Java Development of Spring Boot application",
                            "SQL (Postgres, Oracle)",
                        ],
                    },
                    {
                        itemName: "Customer Support / Tester",
                        period: "September 2018 - February 2020",
                        location: "Seznam.cz, a.s.",
                        skillsLearned: [
                            "Creating and maintaining user documentation",
                            "Customer support for mapy.cz",
                        ],
                    },
                    {
                        itemName: "Software Tester",
                        period: "July 2018 - September 2018",
                        location: "Ceska sporitelna",
                        skillsLearned: [
                            "Testing of Android application",
                            "Preparation of test cases",
                        ],
                    },
                ],
            },
            {
                sectionTitle: "Education",
                resumeItems: [
                    {
                        itemName: "Exchange Program - Information Technologies",
                        period: "2024 - Present",
                        location: "INHA University, KR",
                    },
                    {
                        itemName: "Master's Degree in Informatics",
                        period: "2024 - Present",
                        location: "Czech University of Life Sciences, CZ",
                    },
                    {
                        itemName: "Bachelor's Degree in Informatics",
                        period: "2020 - 2023",
                        location: "Czech University of Life Sciences, CZ",
                    },
                ],
            },
        ],
    };

    // Create FormData object
    const formData = new FormData();

    const imageUrl = new URL('@/assets/img/IMG_8561_cropped.jpg', import.meta.url).href;
    const jsonBlob = new Blob([JSON.stringify(resumeData)], { type: "application/json" });

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

    // ✅ Create a Blob and trigger download
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