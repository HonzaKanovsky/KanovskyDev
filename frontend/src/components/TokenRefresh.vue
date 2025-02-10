<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import axios from "axios";

const accessToken = ref(localStorage.getItem("accessToken"));
const expirationTime = ref(null); // Expiration time in UNIX timestamp
const remainingTime = ref(0);
const interval = ref(null);

// Function to parse token expiration
const parseTokenExpiration = () => {
    if (!accessToken.value) return;
    try {
        const tokenParts = accessToken.value.split(".")[1]; // Extract payload
        const decoded = JSON.parse(atob(tokenParts)); // Decode JWT
        expirationTime.value = decoded.exp * 1000; // Convert to milliseconds
    } catch (error) {
        console.error("Error parsing token:", error);
    }
};

// Function to update countdown
const updateRemainingTime = () => {
    if (!expirationTime.value) return;
    remainingTime.value = Math.max(0, Math.floor((expirationTime.value - Date.now()) / 1000)); // Seconds left
};

// Function to refresh the token
const refreshToken = async () => {
    try {
        const response = await axios.post("http://localhost:8080/api/auth/refresh", null, {
            withCredentials: true, // Important if using HTTP-only cookies
        });

        accessToken.value = response.data; // Save new token
        localStorage.setItem("accessToken", response.data);
        parseTokenExpiration(); // Reset expiration time
        updateRemainingTime(); // Update countdown
    } catch (error) {
        console.error("Token refresh failed:", error);
    }
};

// Start countdown when component mounts
onMounted(() => {
    parseTokenExpiration(); // Parse token
    updateRemainingTime(); // Initialize countdown

    interval.value = setInterval(() => {
        updateRemainingTime();

        // Auto-refresh if token is about to expire (e.g., within 30 seconds)
        if (remainingTime.value <= 30) {
            refreshToken();
        }
    }, 1000); // Update every second
});

// Clean up interval on unmount
onUnmounted(() => {
    clearInterval(interval.value);
});

// Computed property to format time
const formattedTime = computed(() => {
    const minutes = Math.floor(remainingTime.value / 60);
    const seconds = remainingTime.value % 60;
    return `${minutes}m ${seconds}s`;
});
</script>

<template>
    <div v-if="accessToken" class="flex items-center bg-gray-200 px-4 py-2 rounded-md text-gray-700">
        <span>Session expires in: <strong>{{ formattedTime }}</strong></span>
        <button @click="refreshToken"
            class="ml-4 bg-blue-500 text-white px-3 py-1 rounded-md hover:bg-blue-700 transition">
            Extend Session
        </button>
    </div>
</template>
