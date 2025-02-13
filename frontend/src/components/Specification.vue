<script setup>
import { ref, onMounted } from "vue";
import SpecificationSidebar from "@/components/SpecificationSidebar.vue";
import SpecificationMainContent from "@/components/SpecificationContent.vue";
import axios from "axios";

const selectedController = ref(null);
const controllers = ref([]);

const fetchApiDocs = async () => {
  try {
    const response = await axios.get("http://localhost:8080/api/endpoints");

    // Convert flat endpoint list into controller groups
    const groupedControllers = {};
    response.data.forEach(endpoint => {
      var controllerName = endpoint.path.split("/")[2] + " Controller";
      controllerName = controllerName.charAt(0).toUpperCase() + controllerName.slice(1);
      if (!groupedControllers[controllerName]) {
        groupedControllers[controllerName] = {
          name: controllerName,
          endpoints: []
        };
      }
      groupedControllers[controllerName].endpoints.push(endpoint);
    });

    controllers.value = Object.values(groupedControllers);
  } catch (error) {
    console.error("Failed to fetch API documentation", error);
  }
};

onMounted(fetchApiDocs);

const setController = (controller) => {
  selectedController.value = controller;
};
</script>

<template >
  <section class="bg-slate-50 pt-10">
    <div class="flex min-h-screen h-fit max-w-7xl mx-auto rounded-3xl shadow-lg">
    <SpecificationSidebar :controllers="controllers" @controller-selected="setController" />
    <SpecificationMainContent :selectedController="selectedController" />
  </div>
  </section>
</template>
