<script setup>
import { RouterLink, useRoute } from 'vue-router';

const isActiveLink = (routePath) => {
    const route = useRoute();
    return route.path === routePath
}
</script>

<template>
    <nav class="bg-white ">
        <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
            <div class="flex h-20 items-center justify-between">
                <div class="flex flex-1 items-center justify-center md:items-stretch md:justify-start">
                    <!-- Logo -->
                    <RouterLink class="flex flex-shrink-0 items-center mr-4" to="/">
                        <span class="hidden md:block text-blue-800 text-2xl font-bold ml-2">Jan Kanovsky</span>
                    </RouterLink>
                    <div class="md:ml-auto">
                        <div class="flex space-x-2">
                            <RouterLink to="/"
                            :class="[isActiveLink('/') ? ['text-gray-800'] : ['hover:text-gray-700'],'text-gray-500', 'px-3', 'py-2', 'rounded-md']">Home</RouterLink>
                            <RouterLink to="/resume"
                            :class="[isActiveLink('/resume') ? ['text-gray-800'] : ['hover:text-gray-700'],'text-gray-500', 'px-3', 'py-2', 'rounded-md']">Resume</RouterLink>
                            <RouterLink to="/projects"
                            :class="[isActiveLink('/projects') ? ['text-gray-800'] : ['hover:text-gray-700'],'text-gray-500', 'px-3', 'py-2', 'rounded-md']">Projects</RouterLink>
                            <RouterLink to="/contact"
                            :class="[isActiveLink('/contact') ? ['text-gray-800'] : ['hover:text-gray-700'],'text-gray-500', 'px-3', 'py-2', 'rounded-md']">Contact</RouterLink>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</template>