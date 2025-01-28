<script lang="ts" setup>
import { Projekt } from '@/models/Projekt';

const props = defineProps({
    projekt: Projekt,
    redirectButtonText: String
})


</script>

<template>
    <section>
        <div>
            <h3>{{ projekt?.name }}</h3>
            <p>{{ projekt?.description }}</p>
            <RouterLink :to="projekt?.redirectLink || '/'">
                {{ redirectButtonText }}
            </RouterLink>
        </div>
    </section>
</template>