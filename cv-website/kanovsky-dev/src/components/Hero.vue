<script lang="ts" setup>
import photo from '@/assets/img/Photo-placeholder-hd.png'
import { RouterLink } from 'vue-router'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

</script>

<template>
    <section>
        <div class="max-w-7xl mx-auto my-[3%] px-4 sm:px-6 lg:px-8 flex flex-col lg:flex-row items-center h-[80%]">
            <div class="lg:w-5/12 flex flex-col mb-4">
                <div class="text-xs font-semibold mr-auto gradient-background-right text-white px-2 rounded-lg mb-4">
                    JAVA · KOTLIN
                </div>
                <h3 class="text-2xl text-gray-500">
                    {{ t('hero.subtitle') }}
                </h3>
                <h1
                    class="text-6xl mr-auto font-bold gradient-background-bottom bg-clip-text text-transparent mb-8 py-2">
                    {{ t('hero.title') }}
                </h1>
                <div class="grid gap-8 lg:grid-cols-2 justify-items-center text-xl font-sans font-bold lg:max-w-[60%]">
                    <RouterLink to="/resume" class="bg-blue-700 text-white lg:px-6 px-12 py-3 border border-blue-700 rounded-lg">
                        {{ t('hero.resumeButton') }}
                    </RouterLink>
                    <RouterLink to="/portfolio" class="bg-white lg:px-6 px-12 py-3 border border-black rounded-lg">
                        {{ t('hero.portfolioButton') }}
                    </RouterLink>
                </div>
            </div>
            <div class="lg:w-7/12 w-4/5 rounded-lg flex items-center justify-center mt-8">
                <div
                    class="gradient-background-bottom-right w-[100%] max-w-[50vh] aspect-square rounded-3xl flex items-end justify-center relative overflow-visible">
                    <img :src="photo" class="absolute w-[85%] max-h-[65vh] object-cover">
                </div>
            </div>

        </div>
    </section>
</template>
