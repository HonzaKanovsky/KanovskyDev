<script setup>
import { ref, onBeforeMount, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import { Chart as ChartJS, LineElement, PointElement, LinearScale, Title, Tooltip, CategoryScale } from 'chart.js';
import { Line } from 'vue-chartjs';

// Register Chart.js components
ChartJS.register(LineElement, PointElement, LinearScale, Title, Tooltip, CategoryScale);

const router = useRouter();
const route = useRoute();
const cryptoId = route.params.id;
const cryptoDetails = ref(null);
const loading = ref(true);
const error = ref(null);

const props = defineProps({
    id: [String, Number]
});

const fetchCryptoHistory = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/cryptos/${cryptoId}/history`);
        cryptoDetails.value = response.data;
    } catch (err) {
        error.value = "Failed to fetch historical data.";
        console.error(err);
    } finally {
        loading.value = false;
    }
}

// Fetch historical data
onBeforeMount(fetchCryptoHistory);

// Format price function
const formatPrice = (price) => {
    if (price == null || isNaN(price)) return "N/A";  // Handle missing values
    return price >= 1 ? price.toFixed(2) : price.toFixed(5);
};


// Get colors based on `priceChangePercentage`
const getSegmentColors = (data) => {
    return data.map((point, index) => {
        if (index === 0) return "gray"; // First point has no previous change
        return point.priceChangePercentage >= 0 ? "green" : "red";
    });
};



// Prepare data for the chart (REVERSED ORDER)
const chartData = computed(() => {
    if (!cryptoDetails.value) return null;

    // Reverse data order so oldest data comes first
    const reversedData = [...cryptoDetails.value.historisationCryptoPrices].reverse();

    const timestamps = reversedData.map(h => h.timestamp);
    const prices = reversedData.map(h => h.price);
    const colors = getSegmentColors(reversedData);

    return {
        labels: timestamps,
        datasets: [{
            label: `${cryptoDetails.value.crypto.name} Price`,
            data: prices,
            segment: {
                borderColor: (ctx) => colors[ctx.p1DataIndex], // Color each segment dynamically
                backgroundColor: "rgba(0, 0, 0, 0.0)", // Transparent fill
            },
            borderWidth: 2,
            pointRadius: 3, // Small dots on each data point
            tension: 0.3, // Smooth curve
        }]
    };
});

const goBack = () => {
    router.push("/crypto-tracker");
};

watch(() => route.params.id, fetchCryptoHistory);


</script>



<template>
    <section class="bg-slate-50 my-5 py-10">
        <div class="max-w-7xl mx-auto bg-white shadow-lg rounded-lg p-6">
            <div class="flex flex-row text-4xl font-bold text mb-4 items-center">
                <h2>{{ cryptoDetails?.crypto.name }} ({{ cryptoDetails?.crypto.symbol }})</h2>
                <span class="">: ${{ formatPrice(cryptoDetails?.historisationCryptoPrices[0].price) }}</span>
                <button @click="goBack"
                    class="text-2xl font-normal text-white bg-blue-500 hover:bg-blue-900 rounded-md px-3 py-2 ml-auto mr-0">
                    Go back
            </button>
            </div>
            <hr>

            <p v-if="loading" class="text-center text-gray-500">Loading data...</p>
            <p v-if="error" class="text-center text-red-500">{{ error }}</p>

            <div v-if="!loading && !error">
                <!-- Chart -->
                <div class="my-6">
                    <Line v-if="chartData" :data="chartData" :options="{ responsive: true, maintainAspectRatio: false }"
                        class="h-80"></Line>
                </div>

                <!-- Historical Data Table -->
                <table class="w-full border-collapse ">
                    <thead>
                        <tr class="bg-blue-500 text-white text-center">
                            <th class=" px-4 py-2">Date</th>
                            <th class=" px-4 py-2">Price</th>
                            <th class=" px-4 py-2">Change (%)</th>
                            <th class=" px-4 py-2">Market Cap</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="history in cryptoDetails?.historisationCryptoPrices" :key="history.timestamp"
                            class="hover:bg-gray-100">
                            <td class=" px-4 py-2 text-center">{{ history.timestamp }}</td>

                            <td class=" px-4 py-2 text-center">${{ formatPrice(history.price) }}
                            </td>
                            <td class=" px-4 py-2 text-center"
                                :class="history.priceChangePercentage > 0 ? 'text-green-600' : (history.priceChangePercentage == 0 ? 'text-black' : 'text-red-600')">
                                {{ Math.abs(history.priceChangePercentage).toFixed(2) }}%
                            </td>
                            <td class=" px-4 py-2 text-center">${{
                                history.marketCap.toLocaleString() }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</template>
