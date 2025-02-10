<script lang="ts" setup>
import { RouterLink, useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'

const isActiveLink = (routePath: string) => {
    const route = useRoute()
    return route.path === routePath
};

const availableLocales = ['en', 'cs']

const { locale, t } = useI18n();

const switchLocale = (newLocale: string) => {
    if (availableLocales.includes(newLocale)) {
        locale.value = newLocale as typeof locale.value
        localStorage.setItem('locale', newLocale)
    }
};
</script>

<template>
    <nav class="bg-white">
        <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
            <div class="flex h-20 items-center justify-between">
                <div class="flex flex-1 items-center justify-center md:items-stretch md:justify-start">
                    <!-- Logo -->
                    <RouterLink class="flex flex-shrink-0 items-center mr-4" to="/">
                        <span class="hidden md:block text-blue-800 text-2xl font-bold ml-2">Jan Kanovsky</span>
                    </RouterLink>
                    <div class="md:ml-auto">
                        <div class="flex space-x-2">
                            <!-- Reactive Translations -->
                            <div>
                                <RouterLink to="/"
                                    :class="[isActiveLink('/') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'px-3', 'py-2', 'rounded-md']">
                                    {{ t('navbar.home') }}
                                </RouterLink>
                                <RouterLink to="/resume"
                                    :class="[isActiveLink('/resume') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'px-3', 'py-2', 'rounded-md']">
                                    {{ t('navbar.resume') }}
                                </RouterLink>
                                <RouterLink to="/portfolio"
                                    :class="[isActiveLink('/portfolio') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'px-3', 'py-2', 'rounded-md']">
                                    {{ t('navbar.portfolio') }}
                                </RouterLink>
                                <RouterLink to="/contact"
                                    :class="[isActiveLink('/contacts') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'px-3', 'py-2', 'rounded-md']">
                                    {{ t('navbar.contact') }}
                                </RouterLink>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</template>
