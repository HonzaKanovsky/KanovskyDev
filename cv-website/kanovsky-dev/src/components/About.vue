<script lang="ts" setup>
import {onBeforeMount, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

const { t, locale } = useI18n()

const aboutMeParagraphs = ref<string[]>([])

const loadAboutMeDescription = () => {

    const paragraphs: string[] = []
    //TODO - get real size of array
    for (let i = 0; i < 99; i++) {
        const targetedParagraph = `aboutMe.paragraphs[${i}]`
        const paragraph = t(targetedParagraph)

        if (paragraph === targetedParagraph) break
        paragraphs.push(paragraph)
    }
    aboutMeParagraphs.value = paragraphs
}

watch(locale, () => {
    loadAboutMeDescription()
})

onBeforeMount(() => {
    loadAboutMeDescription()
})
</script>

<template>
    <section class="bg-slate-50">
        <div class="max-w-7xl mx-auto my-[3%] pt-[2vh] px-4 sm:px-6 lg:px-8 flex flex-col lg:flex-row items-center h-[80%]">
            <div class="my-10">
                <div class="text-center">
                    <h1 class="text-5xl font-bold gradient-background-right bg-clip-text text-transparent py-2 w-fit mx-auto">
                        {{ t('aboutMe.title') }}
                    </h1>
                    <h3 class="text-xl text-gry font-extralight mb-6">
                        {{ t('aboutMe.subtitle') }}
                    </h3>
                    <div>
                        <p v-for="(paragraph, index) in aboutMeParagraphs" :key="index" class="text-gray-600 my-2 max-w-[70%] mx-auto ">
                            {{ paragraph }}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>
