<script setup>
import Navbar from '@/components/CryptoTrackerNavbar.vue';
import Cryptodetail from '@/components/Cryptodetail.vue';
import Footer from '@/components/Footer.vue';
</script>

<template>
    <Navbar :isCryptoApp="true"/>
    <Cryptodetail />
    <Footer />
</template>