<script lang="ts" setup>
import { computed, defineComponent } from "vue";
import { useI18n } from "vue-i18n";
import type { SkillGroup } from "@/models/SkillGroup";

import * as PhosphorIcons from "@phosphor-icons/vue";

const IconMap: Record<string, any> = PhosphorIcons;

defineComponent({
  name: "SkillsSection"
});

const { tm } = useI18n();

const skillGroups = computed<SkillGroup[]>(() => tm("resume.skills") as SkillGroup[]);
</script>

<template>
  <section class="my-5">
    <div class="flex flex-col bg-white rounded-3xl shadow-lg lg:w-[100%] w-[85%] mx-auto pt-10">
      <div v-for="(group, index) in skillGroups" :key="index" class="lg:w-[90%] mx-auto">
        <div class="flex flex-row items-center lg:mx-0 text-3xl mb-5">
          <component :is="IconMap[group.icon] || IconMap['PiQuestionBold']"
            class="gradient-background-bottom-right mr-3 text-white text-5xl p-2 lg:ml-0 rounded-xl ml-auto" />
          <h3 class="font-bold gradient-background-right bg-clip-text text-transparent mr-auto">
            {{ group.title }}
          </h3>
        </div>

        <div class="mb-10">
          <ul class="flex flex-wrap justify-center gap-x-15 gap-3">
            <li v-for="(skill, i) in group.items" :key="i"
              class="w-3/4 lg:w-[27%] text-center bg-slate-50 py-3 px-6 rounded-xl">
              {{ skill }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>


</template>
