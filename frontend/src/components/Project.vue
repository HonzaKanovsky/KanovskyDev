<script lang="ts" setup>
import { Projekt } from '@/models/Projekt';

const props = defineProps({
    projekt: Projekt,
    redirectButtonText: String,
    redirectButtonTextSpecification: String
})


</script>

<template>
    <section class="my-5">
        <div class="flex lg:flex-row flex-col bg-white rounded-3xl shadow-lg lg:w-[100%] w-[85%] mx-auto">
            <div class="ml-10 lg:my-10 mt-10 p-6 lg:w-[66%]">
                <h3 class="text-3xl font-bold mb-3">{{ projekt?.name }}</h3>
                <p class="text-gray-700">{{ projekt?.description }}</p>

            </div>
            <div class="flex flex-col bg-slate-50 rounded-3xl mx-10 lg:my-10 my-10 p-6 lg:w-[33%] items-stretch text-center gap-2" >
                <RouterLink :to="projekt?.redirectLink || '/'"
                class="mt-3 bg-blue-700 text-white lg:px-6 px-12 py-3 border border-black rounded-lg">
                    {{ redirectButtonText }}
                </RouterLink>
                <RouterLink :to="projekt?.redirectLinkSpecification || '/'"
                class="mt-auto bg-white lg:px-6 px-12 py-3 border border-black rounded-lg">
                    {{ redirectButtonTextSpecification }}
                </RouterLink>
            </div>
        </div>
    </section>
</template>