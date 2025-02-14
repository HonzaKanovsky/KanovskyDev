<script setup>
import { ref, onBeforeMount, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

// Reactive state
const cryptoPrices = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(0);
const totalPages = ref(1);
const router = useRouter();

// Fetch data function
const fetchPage = async (page) => {
    if (page < 0 || page >= totalPages.value) return; // Prevent out-of-bounds pages

    loading.value = true;
    try {
        const response = await axios.get("http://localhost:8080/api/cryptos/list", {
            params: { page, size: 20 },
        });
        cryptoPrices.value = response.data.content;
        currentPage.value = response.data.number;
        totalPages.value = response.data.totalPages;
    } catch (err) {
        error.value = "Failed to fetch data.";
        console.error(err);
    } finally {
        loading.value = false;
    }
};

// Fetch data on mount
onBeforeMount(() => fetchPage(0));

// Compute pagination numbers
const paginationNumbers = computed(() => {
    const pages = [];
    const maxPagesToShow = 5; // 2 before + current + 2 after
    const lastPage = totalPages.value - 1;

    if (totalPages.value <= maxPagesToShow) {
        for (let i = 0; i < totalPages.value; i++) pages.push(i);
    } else {
        pages.push(0); // Always show first page

        let start = Math.max(1, currentPage.value - 2);
        let end = Math.min(lastPage - 1, currentPage.value + 2);

        if (start > 1) pages.push("..."); // Ellipsis before

        for (let i = start; i <= end; i++) pages.push(i);

        if (end < lastPage - 1) pages.push("..."); // Ellipsis after

        pages.push(lastPage); // Always show last page
    }

    return pages;
});

const formatPrice = (price) => {
    if (price >= 1) return price.toFixed(2);  // Show 2 decimals for values >= 1
    return price.toFixed(5);  // Show 5 decimals for values < 1
};

const goToHistory = (id) => {
    router.push(`/crypto-tracker/${id}/history`);
};
</script>

<template>
    <section class="my-5 bg-stone-50 pt-10 pb-10">

        <div class="max-w-7xl mx-auto bg-white shadow-xl rounded-xl p-6 mt-1">
            <!-- Loading & Error Handling -->
            <p v-if="loading" class="text-center text-gray-500">Loading data...</p>
            <p v-if="error" class="text-center text-red-500">{{ error }}</p>

            <!-- Table -->
            <div v-if="!loading && !error" class="overflow-x-auto">
                <h1 class="text-2xl font-bold mb-3">
                    Today's Cryptocurrency Prices
                </h1>
                <table class="w-full border-collapse">
                    <thead>
                        <tr class="bg-blue-500 cursor-crosshair text-white border-b" @click="console.log('click')">
                            <th class="px-4 py-2">#</th>
                            <th class="px-4 py-2">Name</th>
                            <th class="px-4 py-2">Price</th>
                            <th class="px-4 py-2">Change (%)</th>
                            <th class="px-4 py-2">Market Cap</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(crypto, index) in cryptoPrices" :key="crypto.cryptoDTO.id"
                            class="hover:bg-gray-100 cursor-pointer transition border-b size-12"
                            @click="goToHistory(crypto.cryptoDTO.id)">
                            <!-- Show Row Number Instead of ID -->
                            <td class="px-4 py-2 text-center">
                                {{ index + 1 + (currentPage * 20) }}
                            </td>
                            <td class="px-4 py-2 text-center">
                                {{ crypto.cryptoDTO.name }} <span class="text-gray-500">- {{ crypto.cryptoDTO.symbol
                                    }}</span>
                            </td>
                            <td class="px-4 py-2 text-center">
                                ${{ formatPrice(crypto.price) }}
                            </td>
                            <td class="px-4 py-2 text-center"
                                :class="crypto.priceChangePercentage >= 0 ? 'text-green-600' : 'text-red-600'">
                                {{ Math.abs(crypto.priceChangePercentage).toFixed(2) }}%
                            </td>
                            <td class="px-4 py-2 text-center">
                                {{ crypto.marketCap.toLocaleString() }}
                            </td>
                        </tr>

                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <div v-if="totalPages > 1" class="mt-4 flex justify-center items-center space-x-2">
                <!-- Prev Button -->
                <button @click="fetchPage(currentPage - 1)" :disabled="currentPage === 0"
                    class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50">
                    < </button>

                        <!-- Page Numbers -->
                        <template v-for="page in paginationNumbers" :key="page">
                            <button v-if="page === '...'" disabled class="px-3 py-2 text-gray-500">...</button>
                            <button v-else @click="fetchPage(page)"
                                :class="['px-3 py-2 rounded transition', currentPage === page ? 'bg-blue-600 text-white' : 'bg-gray-200 hover:bg-gray-300']">
                                {{ page + 1 }}
                            </button>
                        </template>

                        <!-- Next Button -->
                        <button @click="fetchPage(currentPage + 1)" :disabled="currentPage >= totalPages - 1"
                            class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50">
                            >
                        </button>
            </div>
        </div>
    </section>
</template>
