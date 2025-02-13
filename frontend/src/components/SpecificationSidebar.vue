<script setup>
import { defineProps, defineEmits, ref } from "vue";

const props = defineProps(["controllers"]);
const emit = defineEmits(["controller-selected"]);
const selectedControllerName = ref(null); // Track selected controller

const emitSelection = (controller) => {
  selectedControllerName.value = controller.name; // Update selected controller
  emit("controller-selected", controller);
};
</script>

<template>
  <div class="w-64 rounded-l-xl bg-blue-700 text-white min-h-full py-4">
    <h2 class="text-lg font-bold mb-4 px-4">API Controllers</h2>
    <ul>
      <li v-for="controller in controllers" :key="controller.name" @click="emitSelection(controller)"
        class="cursor-pointer px-4 border border-blue-700 transition text-xl h-12 py-2" :class="{
          'bg-blue-800': selectedControllerName === controller.name,
          'hover:bg-blue-800': selectedControllerName !== controller.name
        }">
        {{ controller.name }}
      </li>
    </ul>
  </div>
</template>
