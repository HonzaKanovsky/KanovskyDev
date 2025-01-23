<script lang="ts" setup>
import {onBeforeMount, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

interface AboutMeMessages {
    aboutMe: {
        title: string
        subtitle: string
        paragraphs: string[]
    }
}

const { t, locale } = useI18n<AboutMeMessages>()

const aboutMeParagraphs = ref<string[]>([])

const loadAboutMeDescription = () => {

    const paragraphs: string[] = []
    //TODO - get dynamicaly size of array
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
    <section>
        <div>
            <div>
                <div>
                    <h1>
                        {{ t('aboutMe.title') }}
                    </h1>
                    <h3>
                        {{ t('aboutMe.subtitle') }}
                    </h3>
                    <div>
                        <p v-for="(paragraph, index) in aboutMeParagraphs" :key="index">
                            {{ paragraph }}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>
