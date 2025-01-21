<script setup>
import { defineProps } from 'vue'
import photo from '@/assets/img/Photo-placeholder-hd.png'
import { RouterLink } from 'vue-router';

defineProps({
    title: {
        type: String,
        default: "Build software effortlessly and effectively"
    },
    subtitle: {
        type: String,
        default: "I craft intelligent and scalable solutions to help you"
    }
})
</script>

<template>
    <section class="max-w-6xl mx-auto px-4 mt-5 sm:px-6 lg:px-8 h-[55vh] flex items-center">
        <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between lg:space-y-0 w-full py-4">
            <div class="lg:w-6/12 text-left">
                <div
                    class="text-xs font-bold text-white bg-gradient-to-r from-blue-800 to-purple-500 px-2 size-fit rounded-lg">
                    BACK-END · FRONT-END
                </div>
                <p class="mt-4 text-xl text-gray-500">
                    {{ subtitle }}
                </p>
                <h1
                    class="text-5xl font-extrabold bg-gradient-to-r from-blue-800 to-purple-500 text-transparent bg-clip-text pb-2">
                    {{ title }}
                </h1>
                <div class="flex my-8 text-white">
                    <RouterLink to="/resume" class="bg-blue-800 rounded-lg py-4 px-8 mx-8 ml-0">
                        Resume
                    </RouterLink>
                    <RouterLink to="/projects" class="bg-purple-500 rounded-lg py-4 px-8 mx-8">
                        Projects
                    </RouterLink>
                </div>
            </div>
            <div class="lg:w-7/12 rounded-lg flex items-center justify-center">
                <div
                    class="bg-gradient-to-br from-blue-800 to-purple-600 w-[70%] max-w-[35vh] aspect-square rounded-3xl flex items-end justify-center relative overflow-visible">
                    <img :src="photo" class="absolute w-[85%] max-h-[45vh] object-cover">
                </div>
            </div>
        </div>
    </section>
</template>
