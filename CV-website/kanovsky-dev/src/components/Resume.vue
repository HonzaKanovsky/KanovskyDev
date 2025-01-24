<script lang="ts" setup>
import { ref, watch, onBeforeMount } from "vue";
import { Experience, Company, Education, University } from "@/models/Resume";
import { useI18n } from "vue-i18n";
import Experiences from "./Experiences.vue";
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
    <h2>{{ t('resume.experienceSubtitle') }}</h2>
    <Experiences v-for="experience in resumeExperiences" :key="experience.period" :experience="experience" />
    <h2>{{ t('resume.educationSubtitle') }}</h2>
    <EducationComponent v-for="education in resumeEducation" :key="education.period" :education="education" />
</template>