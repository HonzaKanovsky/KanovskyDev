<script lang="ts" setup>
import { onBeforeMount, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { PhGithubLogo, PhLinkedinLogo } from '@phosphor-icons/vue'

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
        <div class="max-w-7xl mx-auto mt-[3%] pt-[2vh] px-4 sm:px-6 lg:px-8 flex flex-col lg:flex-col items-center">
            <div class="my-10">
                <div class="text-center">
                    <h1
                        class="text-5xl font-bold gradient-background-right bg-clip-text text-transparent py-2 w-fit mx-auto">
                        {{ t('aboutMe.title') }}
                    </h1>
                    <h3 class="text-xl text-gry font-extralight mb-6">
                        {{ t('aboutMe.subtitle') }}
                    </h3>
                    <div>
                        <p v-for="(paragraph, index) in aboutMeParagraphs" :key="index"
                            class="text-gray-600 text-lg my-4 lg:max-w-[70%] max-w-[90%] mx-auto ">
                            {{ paragraph }}
                        </p>
                    </div>
                    <div class="mx-auto text-5xl flex flex-row gap-x-10 text-white mt-6 ">
                        <a href="https://github.com/HonzaKanovsky/KanovskyDev" class="gradient-background-bottom-right ml-auto rounded-md" >
                            <PhGithubLogo />
                        </a>
                        <a href="https://www.linkedin.com/in/jan-ka%C5%88ovsk%C3%BD-470b4014b/" class="gradient-background-bottom-right mr-auto rounded-md" >
                            <PhLinkedinLogo />
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </section>
</template>
