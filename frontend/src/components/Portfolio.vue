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
        const targetedProject = `portfolio.projects[${i}]`
        const projectName = t(targetedProject + '.name')
        const projectDescription = t(targetedProject + '.description')
        const projectRedirectLink = t(targetedProject + '.redirectLink')
        const projectRedirectLinkSpec = t(targetedProject + '.redirectLinkSpecification')

        if (projectName === (targetedProject + '.name')) break

        projects.push(new Projekt(
            projectName,
            projectDescription,
            projectRedirectLink,
            projectRedirectLinkSpec
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
    <section class="bg-slate-50">
        <div class="max-w-3xl mx-auto flex flex-col justify-center pt-20 pb-20">
            <h1 class="text-5xl font-bold gradient-background-right bg-clip-text text-transparent mx-auto">
                {{ t('portfolio.title') }}
            </h1>
            <Project v-for="project in portfolioProjects" :key="project.name" :projekt="project"
                :redirectButtonText="t('portfolio.redirectButtonText')"
                :redirectButtonTextSpecification="t('portfolio.redirectButtonTextSpecification')" />
        </div>
    </section>
</template>