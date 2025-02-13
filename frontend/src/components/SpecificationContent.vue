<script setup>
import { defineProps, ref, computed, onMounted } from "vue";
import axios from "axios";

const props = defineProps(["selectedController"]);
const expandedEndpoints = ref({});
const dtoList = ref([]);

// Fetch DTOs from backend when the component mounts
onMounted(async () => {
  try {
    const response = await axios.get("/api/dtos");
    dtoList.value = response.data;
  } catch (error) {
    console.error("Error fetching DTOs:", error);
  }
});

// Ensure selectedController is always an object
const controller = computed(() => props.selectedController || { endpoints: [] });

const getUniqueKey = (endpoint) => `${endpoint.method}_${endpoint.path}`;

const toggleDetails = (endpoint) => {
  const key = getUniqueKey(endpoint);
  expandedEndpoints.value[key] = !expandedEndpoints.value[key];
};

const expandAll = () => {
  controller.value.endpoints.forEach(endpoint => {
    expandedEndpoints.value[getUniqueKey(endpoint)] = true;
  });
};

const collapseAll = () => {
  controller.value.endpoints.forEach(endpoint => {
    expandedEndpoints.value[getUniqueKey(endpoint)] = false;
  });
};
</script>

<template>
  <div class="flex-1 p-6 bg-white border rounded-r-xl">
    <div v-if="controller.name">
      <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold">{{ controller.name }}</h1>
        <div>
          <button @click="collapseAll" class="mr-2 text-sm text-gray-600 hover:underline">
            Collapse Operations
          </button>
          <button @click="expandAll" class="text-sm text-gray-600 hover:underline">
            Expand Operations
          </button>
        </div>
      </div>

      <ul class="space-y-4">
        <li v-for="endpoint in controller.endpoints" :key="getUniqueKey(endpoint)">
          <!-- Title Row -->
          <div @click="toggleDetails(endpoint)"
            class="flex justify-between items-center p-4 bg-white shadow-md border cursor-pointer hover:bg-gray-100 transition"
            :class="expandedEndpoints[getUniqueKey(endpoint)] ? 'rounded-t-lg' : 'rounded-lg'">
            <span :class="{
              'bg-red-600': endpoint.method === 'DELETE',
              'bg-blue-600': endpoint.method === 'POST',
              'bg-green-600': endpoint.method === 'GET',
              'bg-yellow-600': endpoint.method === 'PATCH',
            }" class="px-3 py-1 text-white rounded-md uppercase text-sm font-bold">
              {{ endpoint.method }}
            </span>

            <span class="flex-1 mx-4 text-gray-800 font-semibold">
              {{ endpoint.path }}
            </span>

            <span class="text-gray-500 text-sm">{{ endpoint.description }}</span>
          </div>

          <!-- Expanded Details Row -->
          <div v-if="expandedEndpoints[getUniqueKey(endpoint)]" class="p-4 bg-gray-50 border border-t-0 rounded-b-lg">

            <!-- Parameters Table -->
            <div v-if="endpoint.parameters && endpoint.parameters.length > 0" class="mt-3">
              <h3 class="font-semibold mb-2">Parameters</h3>
              <table class="w-full border-collapse border border-gray-300">
                <thead class="bg-gray-200">
                  <tr>
                    <th class="border border-gray-300 px-3 py-2 text-left w-1/2">Parameter</th>
                    <th class="border border-gray-300 px-3 py-2 text-left">Type</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-for="param in endpoint.parameters" :key="param.name">
                    <tr>
                      <td class="border border-gray-300 px-3 py-2 font-semibold">
                        {{ param.name }}
                      </td>
                      <td class="border border-gray-300 px-3 py-2">
                        {{ param.type }}
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>

              <div class="mt-5" v-for="param in endpoint.parameters" :key="param.name">
                <div v-if="param.fields && param.fields.length > 0">
                  <h3 class="font-semibold mb-2">{{ param.type }}</h3>
                  <table class="w-full border-collapse border-t border-b border-gray-300">
                    <thead class="bg-gray-200">
                      <tr>
                        <th class="border border-gray-300 px-3 py-2 text-left">Parameter</th>
                        <th class="border border-gray-300 px-3 py-2 text-left">Type</th>
                      </tr>
                    </thead>
                    <tbody>
                      <template v-for="param in endpoint.parameters" :key="param.name">
                        <tr v-for="field in param.fields" :key="field.name">
                          <td class="border border-gray-300 px-3 py-2 w-1/2">{{ field.name }}</td>
                          <td class="border border-gray-300 px-3 py-2">{{ field.type }}</td>
                        </tr>
                      </template>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <p v-else class="text-gray-500 mt-2">No parameters required.</p>
          </div>
        </li>
      </ul>

    </div>
    <p v-else class="text-gray-500">Select a controller to view endpoints.</p>
  </div>
</template>
