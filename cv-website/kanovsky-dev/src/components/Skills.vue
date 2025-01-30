<script lang="ts" setup>
import { computed, defineComponent } from "vue";
import { useI18n } from "vue-i18n";
import type { SkillGroup } from "@/models/SkillGroup";

// Import Pi Icons
import * as PhosphorIcons from "@phosphor-icons/vue";

// Cast the imported icons as a record
const IconMap: Record<string, any> = PhosphorIcons;

// Define component
defineComponent({
  name: "SkillsSection"
});

// Get translations
const { tm } = useI18n();

// Compute the skill groups with TypeScript typing
const skillGroups = computed<SkillGroup[]>(() => tm("resume.skills") as SkillGroup[]);
</script>

<template>
  <section >
    <div v-for="(group, index) in skillGroups" :key="index" >
      <!-- Dynamic Icon Rendering with Type Safety -->
      <h3 >
        <component
          :is="IconMap[group.icon] || IconMap['PiQuestionBold']"
          
        />
        {{ group.title }}
      </h3>

      <!-- List of Skills -->
      <ul >
        <li v-for="(skill, i) in group.items" :key="i">{{ skill }}</li>
      </ul>
    </div>
  </section>
</template>
