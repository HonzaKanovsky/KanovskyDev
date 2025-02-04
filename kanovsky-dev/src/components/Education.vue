<script lang="ts" setup>
import { Education } from '@/models/Education';

defineProps({
    education: Education,
    sectionTitle: String
})
</script>

<template>
    <section class="my-5">
        <div class="flex lg:flex-row flex-col bg-white rounded-3xl shadow-lg lg:w-[100%] w-[85%] mx-auto">
            <div class="bg-slate-50 rounded-3xl mx-10 lg:my-10 mt-10 p-6 lg:w-[33%]">
                <div class="text-pink-600 font-bold">
                    {{ education?.period }}
                </div>
                <h4 class="mt-2 font-bold">
                    {{ education?.degree }}
                </h4>
                <div class="flex flex-col text-gray-500">
                    <div>
                        {{ education?.university.name }}

                    </div>
                    <div>
                        {{ education?.university.location }}
                    </div>
                </div>
            </div>
            <div class="mx-10 mb-10 mt-5 lg:m-10 lg:w-[67%] flex items-center">
                {{ education?.description }}
            </div>
        </div>
    </section>
</template>