<script lang="ts" setup>
import { ref, watch, onBeforeMount } from "vue";
import { Projekt } from "@/models/Projekt";
import { useI18n } from "vue-i18n";
import Project from "./Project.vue";

const { t, locale } = useI18n()

const portfolioProjects = ref<Projekt[]>([])

const loadProjects = () => {
    const projects: Projekt[] = []

    for (let i = 0; i < 99; i++) {
        const targetedExperience = `portfolio.projects[${i}]`
        const projectName = t(targetedExperience + '.name')
        const projectDescription = t(targetedExperience + '.description')
        const projectRedirectLink = t(targetedExperience + '.redirectLink')

        if (projectName === (targetedExperience + '.name')) break


        projects.push(new Projekt(
            projectName,
            projectDescription,
            projectRedirectLink
        ))
    }
    portfolioProjects.value = projects
}

watch(locale, () => {
    loadProjects()
})

onBeforeMount(() => {
    loadProjects()
})
</script>


<template>
    <section>
        <h2>{{ t('portfolio.title') }}</h2>
        <Project v-for="project in portfolioProjects" :key="project.name" :projekt="project" :redirectButtonText="t('portfolio.redirectButtonText')"/>
    </section>
</template>