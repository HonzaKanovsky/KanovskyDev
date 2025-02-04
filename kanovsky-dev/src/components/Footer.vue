<script lang="ts" setup>
import { RouterLink, useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'


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
    <footer class="my-3">
        <div class="max-w-7xl mx-auto flex flex-column py-2">
            <div class="mx-auto">
                © 2025 Jan Kanovsky, Built with Vue.js.
            </div>
        </div>
    </footer>
</template>