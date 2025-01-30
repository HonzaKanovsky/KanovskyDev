<script lang="ts" setup>
    import { RouterLink } from 'vue-router';
    import { useI18n } from 'vue-i18n';

    const { locale, t } = useI18n()
</script>

<template>
    <section class="text-center flex flex-col justify-center items-center h-[80vh]">
        <i class="pi pi-exclamation-triangle text-yellow-500 text-7xl mb-5"></i>
        <h1 class="text-6xl font-bold mb-4">{{ t('notFound.title') }}</h1>
        <p class="text-xl mb-5">{{ t('notFound.subtitle') }}</p>
        <RouterLink to="/" class="text-white bg-blue-500 hover:bg-blue-900 rounded-md px-3 py-2 mt-4">
            {{ t('notFound.backButton') }}
        </RouterLink>
    </section>
</template>