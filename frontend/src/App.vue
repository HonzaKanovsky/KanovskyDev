<script setup>
import Navbar from './components/Navbar.vue';
import { RouterView } from 'vue-router';
</script>

<template>
    <RouterView :key="$route.fullPath" />
</template>

