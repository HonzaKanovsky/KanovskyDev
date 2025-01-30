<script lang="ts" setup>
import { Education } from '@/models/Education';

defineProps({
    education: Education,
    sectionTitle: String
})
</script>

<template>
    <section class="education">
        <div>
            <h3>{{ education?.degree }}</h3>
            <p>
                <strong>University:</strong>
                {{ education?.university.name }} ({{ education?.university.location }})
            </p>
            <p><strong>Period:</strong> {{ education?.period }}</p>
            <p>{{ education?.description }}</p>
        </div>
    </section>
</template>