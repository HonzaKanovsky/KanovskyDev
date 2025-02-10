<script setup>
import Navbar from '@/components/CryptoTrackerNavbar.vue';
import CryptoList from '@/components/CryptoList.vue';
import Footer from '@/components/Footer.vue';
</script>

<template>
    <Navbar :isCryptoApp="true"/>
    <CryptoList />
    <Footer />
</template>