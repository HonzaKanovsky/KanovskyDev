<script setup>
import { defineProps } from 'vue'

defineProps({
    title: {
        type: String,
        default: "About Me"
    },
    subtitle: {
        type: String,
        default: "My name is Jan Kanovsky and i help deliver smart software solutions"
    },
    aboutMeParagraphs: {
        type: Array,
        default: () => [
            "I am a dedicated software developer with a strong background in backend development and software testing, supported by a Bachelor's degree in Informatics from the Faculty of Economics and Management at CZU Prague. Currently pursuing a Master's degree in Informatics at the Czech University of Life Sciences, I am committed to advancing my expertise in technology and development.",
            "With over three years of experience as a Backend Developer at Seyfor, I have honed my skills in building robust software solutions. My prior roles include working as a Java Developer and Software Tester at MOPET CZ, where I gained valuable experience in software quality assurance and development. I have also contributed to customer support and testing roles at organizations like Seznam.cz and Česká spořitelna, showcasing my ability to adapt to various aspects of the tech industry.",
            "Driven by a passion for technology, I continuously seek opportunities to learn and grow. My skill set includes proficiency in C# and Java, alongside a solid foundation in testing methodologies and problem-solving. I am enthusiastic about leveraging my skills to develop innovative solutions and contribute to impactful projects."
        ]
    }
})
</script>

<template>
    <section class="bg-gray-50">
        <div class="container mx-auto max-w-7xl px-2 sm:px-6 lg:px-8 text-center">
            <div class="row py-7">
                <div class="row justify-self-center items-center">
                    <h1
                        class="text-5xl font-extrabold bg-gradient-to-r from-blue-800 to-purple-500 text-transparent bg-clip-text pb-2 max-w-fit mx-auto">
                        {{ title }}
                    </h1>
                    <h3 class="text-xl font-light pb-2 text-center text-gray-500">
                        {{ subtitle }}
                    </h3>

                    <div class="text-gray-500 text-lg my-6">
                        <p class="py-1" v-for="(paragraph, index) in aboutMeParagraphs" :key="index">
                            {{ paragraph }}
                        </p>
                    </div>

                </div>
            </div>
        </div>
    </section>

</template>