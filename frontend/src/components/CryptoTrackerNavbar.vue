<script lang="ts" setup>
import { RouterLink, useRoute, useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import { ref, computed } from "vue";
import axios from "axios";
import { PhMagnifyingGlass } from "@phosphor-icons/vue"; // Import search icon
import TokenRefresh from "./TokenRefresh.vue";

const route = useRoute();
const router = useRouter();

const isActiveLink = (routePath: string) => {
    return route.path === routePath;
};

const availableLocales = ["en", "cs"];
const { locale, t } = useI18n();

const switchLocale = (newLocale: string) => {
    if (availableLocales.includes(newLocale)) {
        locale.value = newLocale as typeof locale.value;
        localStorage.setItem("locale", newLocale);
    }
};

// Computed property to check if user is logged in
const isAuthenticated = computed(() => !!localStorage.getItem("accessToken"));

// Logout function
const logout = () => {
    localStorage.removeItem("accessToken"); // Remove token
    router.push("/crypto-tracker/login"); // Redirect to login
};

// Search bar state
const searchQuery = ref("");
const searchResults = ref<{ id: number; symbol: string; name: string }[]>([]);

// Fetch search results dynamically
const searchCryptos = async () => {
    if (searchQuery.value.length < 2) {
        searchResults.value = [];
        return;
    }
    try {
        const response = await axios.get(`http://localhost:8080/api/cryptos/search`, {
            params: { query: searchQuery.value }
        });
        searchResults.value = response.data; // Expected format: [{id: 1, symbol: "BTC", name: "Bitcoin"}, ...]
    } catch (error) {
        console.error("Failed to search cryptos", error);
    }
};

// Redirect to Crypto History Page
const goToCryptoHistory = (crypto: { id: number; symbol: string; name: string }) => {
    router.push(`/crypto-tracker/${crypto.id}/history`);
    searchQuery.value = ""; // Clear search input
    searchResults.value = []; // Hide dropdown
};
</script>

<template>
    <nav class="bg-white">
        <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
            <div class="flex flex-col mt-3">
                <div class="flex justify-end">
                    <div class="flex flex-row">
                        <!-- Token Refresh Section -->
                        <div v-if="isAuthenticated" class="mx-4">
                            <TokenRefresh />
                        </div>
                        <!-- Show Logout button if logged in -->
                        <button v-if="isAuthenticated" @click="logout"
                            class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-700 transition">
                            Logout
                        </button>
                    </div>
                </div>

                <div class="flex  h-20 items-center justify-between">
                    <div class="flex flex-1 items-center justify-center md:items-stretch md:justify-start">
                        <!-- Logo -->
                        <RouterLink class="flex flex-shrink-0 items-center mr-4" to="/">
                            <span class="hidden md:block text-blue-800 text-2xl font-bold ml-2">Jan Kanovsky</span>
                        </RouterLink>

                        <!-- Search Bar -->
                        <div class="relative w-64 md:w-80 mx-4">
                            <input v-model="searchQuery" @input="searchCryptos" type="text"
                                placeholder="Search Crypto (e.g., Bitcoin, BTC)"
                                class="border p-2 rounded-md w-full pl-10" />
                            <PhMagnifyingGlass class="absolute top-3 left-3 text-gray-500 w-5 h-5" />

                            <!-- Search Results Dropdown -->
                            <ul v-if="searchResults.length"
                                class="absolute z-10 w-full bg-white border mt-1 rounded-md shadow-lg max-h-60 overflow-y-auto">
                                <li v-for="crypto in searchResults" :key="crypto.id" @click="goToCryptoHistory(crypto)"
                                    class="p-2 hover:bg-gray-100 cursor-pointer">
                                    <span class="font-semibold">{{ crypto.symbol }}</span> - {{ crypto.name }}
                                </li>
                            </ul>
                        </div>


                        <div class="md:ml-auto">
                            <div class="flex space-x-2">
                                <div>
                                    <!-- Listings -->
                                    <RouterLink to="/crypto-tracker"
                                        :class="[isActiveLink('/crypto-tracker') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'px-3', 'py-2', 'rounded-md']">
                                        Listings
                                    </RouterLink>
                                    <RouterLink v-if="isAuthenticated" to="/crypto-tracker/dashboard"
                                        :class="[isActiveLink('/crypto-tracker/dashboard') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'px-3', 'py-2', 'rounded-md']">
                                        Dashboard
                                    </RouterLink>

                                    <span class="px-3">
                                        <!-- Show Login/Register if NOT logged in -->
                                        <template v-if="!isAuthenticated">
                                            <RouterLink to="/crypto-tracker/login"
                                                :class="[isActiveLink('/crypto-tracker/login') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'py-2', 'rounded-md']">
                                                Login
                                            </RouterLink>
                                            <span>/</span>
                                            <RouterLink to="/crypto-tracker/register"
                                                :class="[isActiveLink('/crypto-tracker/register') ? ['text-gray-800'] : ['hover:text-gray-700'], 'text-gray-500', 'py-2', 'rounded-md']">
                                                Register
                                            </RouterLink>
                                        </template>

                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </nav>
</template>
