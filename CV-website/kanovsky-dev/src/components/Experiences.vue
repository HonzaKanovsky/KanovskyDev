<script lang="ts" setup>
import { Experience } from '@/models/Resume';
import { defineProps } from 'vue';

defineProps({
    experience: Experience,
})

</script>

<template>
    <section>
        <div>
            <h3>{{ experience?.position }}</h3>
            <p>
                <strong>Company:</strong>
                {{ experience?.company.name }} ({{ experience?.company.location }})
            </p>
            <p><strong>Period:</strong> {{ experience?.period }}</p>
            <p>{{ experience?.description }}</p>
        </div>
    </section>
</template>