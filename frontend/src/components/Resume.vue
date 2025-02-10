<script lang="ts" setup>
import { ref, watch, onBeforeMount } from "vue";
import { Experience } from "@/models/Experience";
import { Education } from "@/models/Education";
import { Company } from "@/models/Company";
import { University } from "@/models/University";
import { useI18n } from "vue-i18n";
import Experiences from "./Experience.vue";
import ResumeButton from "./StaticResumeButton.vue"
import Skills from "./Skills.vue";
import EducationComponent from "./Education.vue";

const { t, locale } = useI18n()

const resumeExperiences = ref<Experience[]>([])
const resumeEducation = ref<Education[]>([])

const loadExperiences = () => {
    const experiences: Experience[] = []

    for (let i = 0; i < 99; i++) {
        const targetedExperience = `resume.experiences[${i}]`
        const position = t(targetedExperience + '.position')
        const period = t(targetedExperience + '.period')
        const description = t(targetedExperience + '.description')
        const companyName = t(targetedExperience + '.company.name')
        const companyLocation = t(targetedExperience + '.company.location')

        if (position === (targetedExperience + '.position')) break

        const newCompany = new Company(
            companyName,
            companyLocation
        )

        experiences.push(new Experience(
            position,
            period,
            description,
            newCompany
        ))
    }
    resumeExperiences.value = experiences
}

const loadEducation = () => {
    const education: Education[] = []

    for (let i = 0; i < 99; i++) {
        const targetedExperience = `resume.universities[${i}]`
        const degree = t(targetedExperience + '.degree')
        const period = t(targetedExperience + '.period')
        const description = t(targetedExperience + '.description')
        const universityName = t(targetedExperience + '.university.name')
        const universityLocation = t(targetedExperience + '.university.location')

        if (degree === (targetedExperience + '.degree')) break

        const newCompany = new University(
            universityName,
            universityLocation
        )

        education.push(new Education(
            degree,
            period,
            description,
            newCompany
        ))
    }
    resumeEducation.value = education
}

watch(locale, () => {
    loadExperiences()
    loadEducation()
})

onBeforeMount(() => {
    loadExperiences()
    loadEducation()
})
</script>


<template>
    <section class="bg-slate-50">
        <div class="max-w-3xl mx-auto flex flex-col justify-center pt-20">
            <h1 class="text-5xl font-bold gradient-background-right bg-clip-text text-transparent mx-auto">
                {{ t('resume.title') }}
            </h1>

            <div class="mt-5 flex lg:flex-row flex-col lg:mx-0 mx-auto items-center">
                <h3 class="text-4xl font-bold text-blue-800 ml-0 mr-auto">
                {{t('resume.experienceSubtitle') }}
                </h3>
                <ResumeButton class="text-1xl text-white bg-blue-800 mr-0 flex flex-row py-3 px-4 rounded-md my-5" />
            </div>
            <Experiences v-for="experience in resumeExperiences" :key="experience.period" :experience="experience"/>
            
            <h3 class="text-4xl font-bold text-pink-600 lg:ml-0 mx-auto mt-5">
                {{ t('resume.educationSubtitle') }}
            </h3>
            <EducationComponent v-for="education in resumeEducation" :key="education.period" :education="education" />
        
            <Skills />
            
        </div>

    </section>
</template>