<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { PhPencil, PhTrash, PhCheck, PhPlus, PhX, PhMagnifyingGlass } from "@phosphor-icons/vue"; // Import Phosphor Icons

const userId = localStorage.getItem("userId");
const accessToken = localStorage.getItem("accessToken");
const portfolioData = ref(null);
const errorMessage = ref("");
const loading = ref(true);
const editingCrypto = ref(null);
const editedAmount = ref(null);

// Popup Form State
const showAddForm = ref(false);
const searchQuery = ref(""); // User input for search
const searchResults = ref([]); // Stores filtered results
const selectedCrypto = ref(null); // Stores the selected crypto
const newAmount = ref("");

// Fetch Portfolio Data
const fetchPortfolio = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/portfolios/${userId}`, {
            headers: { Authorization: `Bearer ${accessToken}` }
        });
        portfolioData.value = response.data;
    } catch (error) {
        errorMessage.value = "Failed to fetch portfolio data.";
    } finally {
        loading.value = false;
    }
};

// Enable Editing for a Specific Crypto
const enableEditing = (cryptoId, currentAmount) => {
    editingCrypto.value = cryptoId;
    editedAmount.value = currentAmount;
};

// Save Edited Amount
const saveEditedAmount = async (cryptoId) => {
    if (!editedAmount.value) return;

    try {
        await axios.patch(`http://localhost:8080/api/portfolios`, null, {
            params: { userId, cryptoId, amount: Number(editedAmount.value) },
            headers: { Authorization: `Bearer ${accessToken}` }
        });
        editingCrypto.value = null; // Exit edit mode
        fetchPortfolio(); // Refresh portfolio
    } catch (error) {
        errorMessage.value = "Failed to update entry.";
    }
};


// Remove Portfolio Entry
const removeEntry = async (cryptoId) => {
    try {
        await axios.delete(`http://localhost:8080/api/portfolios`, {
            params: { userId, cryptoId },
            headers: { Authorization: `Bearer ${accessToken}` }
        });
        fetchPortfolio(); // Refresh after removing
    } catch (error) {
        errorMessage.value = "Failed to remove entry.";
    }
};

// Open the Add Currency Popup
const openAddForm = () => {
    showAddForm.value = true;
};

// Close the Add Currency Popup
const closeAddForm = () => {
    showAddForm.value = false;
    searchQuery.value = "";
    searchResults.value = [];
    selectedCrypto.value = null;
    newAmount.value = "";
};

// Search Cryptos by Name or Symbol
const searchCryptos = async () => {
    if (searchQuery.value.length < 2) {
        searchResults.value = [];
        return;
    }
    try {
        const response = await axios.get(`http://localhost:8080/api/cryptos/search`, {
            params: { query: searchQuery.value },
            headers: { Authorization: `Bearer ${accessToken}` }
        });
        searchResults.value = response.data; // Expected response: [{id: 1, symbol: "BTC", name: "Bitcoin"}, ...]
    } catch (error) {
        console.error("Failed to search cryptos", error);
    }
};

// Select Crypto from Search Results
const selectCrypto = (crypto) => {
    selectedCrypto.value = crypto;
    searchQuery.value = `${crypto.symbol} - ${crypto.name}`; // Show "Symbol - Name"
    searchResults.value = [];
};

// Add New Currency
const addEntry = async () => {
    if (!selectedCrypto.value || !newAmount.value) {
        alert("Please select a cryptocurrency and enter an amount.");
        return;
    }

    try {
        await axios.post(`http://localhost:8080/api/portfolios`, null, {
            params: { userId, cryptoId: selectedCrypto.value.id, amount: newAmount.value },
            headers: { Authorization: `Bearer ${accessToken}` }
        });

        closeAddForm();
        fetchPortfolio(); // Refresh after adding
    } catch (error) {
        errorMessage.value = "Failed to add entry.";
    }
};

// Fetch portfolio on component mount
onMounted(fetchPortfolio);
</script>

<template>
    <div class="bg-slate-50 pb-10 pt-10">
        <section class="max-w-7xl mx-auto bg-white shadow-lg rounded-xl p-6 relative">

            <p v-if="loading" class="text-gray-500">Loading portfolio...</p>
            <p v-if="errorMessage" class="text-red-500">{{ errorMessage }}</p>

            <div class="flex flex-row items-center mb-4">
                <!-- Total Portfolio Value -->
                <div v-if="!loading && portfolioData" class="text-2xl font-semibold text-center">
                    {{ portfolioData.user.username }}'s Portfolio
                    <span class="text-blue-600">${{ Number(portfolioData.portfolioValue).toLocaleString() }}</span>
                </div>


                <!-- Add Currency Button -->
                <div class="mr-0 ml-auto ">
                    <button @click="openAddForm"
                        class="bg-green-500 text-white px-4 py-2 rounded-md flex items-center gap-2 hover:bg-green-700">
                        <PhPlus class="w-5 h-5" />
                        Add Currency
                    </button>
                </div>
            </div>


            <div v-if="!loading && portfolioData">
                <!-- Portfolio Table -->
                <table class="w-full border-collapse border">
                    <thead>
                        <tr class="bg-blue-500 text-white border-b">
                            <th class=" px-4 py-2">Crypto</th>
                            <th class=" px-4 py-2">Amount</th>
                            <th class=" px-4 py-2">Current Value</th>
                            <th class=" px-4 py-2">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="entry in portfolioData.portfolio.content" :key="entry.crypto.id" class="border-b">
                            <td class=" px-4 py-2 text-center">
                                {{ entry.crypto.symbol }} - {{ entry.crypto.name }}
                            </td>

                            <!-- Editable Amount Cell -->
                            <td class=" px-4 py-2 text-center">
                                <template v-if="editingCrypto === entry.crypto.id">
                                    <input v-model="editedAmount" type="number"
                                        class="border p-1 rounded-md w-20 text-center"
                                        @blur="saveEditedAmount(entry.crypto.id)"
                                        @keyup.enter="saveEditedAmount(entry.crypto.id)" autofocus />
                                </template>
                                <template v-else>
                                    {{ Number(entry.amount).toLocaleString() }} <!-- ✅ Format with commas -->
                                </template>
                            </td>


                            <td class=" px-4 py-2 text-center">${{ entry.currentPrice.toFixed(2) }}
                            </td>

                            <!-- Edit & Delete Buttons -->
                            <td class=" px-4 py-2 text-center flex gap-2 justify-center">
                                <button v-if="editingCrypto !== entry.crypto.id"
                                    @click="enableEditing(entry.crypto.id, entry.amount)"
                                    class="bg-yellow-500 text-white px-2 py-1 rounded-md hover:bg-yellow-700 flex items-center">
                                    <PhPencil class="w-5 h-5" />
                                </button>
                                <button v-if="editingCrypto === entry.crypto.id"
                                    @click="saveEditedAmount(entry.crypto.id)"
                                    class="bg-green-500 text-white px-2 py-1 rounded-md hover:bg-green-700 flex items-center">
                                    <PhCheck class="w-5 h-5" />
                                </button>
                                <button @click="removeEntry(entry.crypto.id)"
                                    class="bg-red-500 text-white px-2 py-1 rounded-md hover:bg-red-700 flex items-center">
                                    <PhTrash class="w-5 h-5" />
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>


            <!-- Popup Form -->
            <div v-if="showAddForm"
                class="fixed top-0 left-0 w-full h-full flex justify-center items-center bg-gray-900 bg-opacity-50">
                <div class="bg-white p-6 rounded-md shadow-lg">
                    <h3 class="text-xl font-semibold mb-4">Add New Currency</h3>

                    <!-- Crypto Search -->
                    <div class="relative mb-2">
                        <input v-model="searchQuery" @input="searchCryptos" type="text"
                            placeholder="Search Crypto (e.g., Bitcoin, BTC)" class="border p-2 rounded-md w-full" />
                        <PhMagnifyingGlass class="absolute top-3 right-3 text-gray-500 w-5 h-5" />

                        <!-- Search Results Dropdown -->
                        <ul v-if="searchResults.length"
                            class="absolute z-10 w-full bg-white border mt-1 rounded-md shadow-lg max-h-60 overflow-y-auto">
                            <li v-for="crypto in searchResults" :key="crypto.id" @click="selectCrypto(crypto)"
                                class="p-2 hover:bg-gray-100 cursor-pointer">
                                <span class="font-semibold">{{ crypto.symbol }}</span> - {{ crypto.name }}
                            </li>
                        </ul>
                    </div>

                    <!-- Selected Crypto -->
                    <p v-if="selectedCrypto" class="mb-2 text-blue-600">
                        Selected: <strong>{{ selectedCrypto.symbol }} - {{ selectedCrypto.name }}</strong>
                    </p>

                    <!-- Amount Input -->
                    <input v-model="newAmount" type="number" placeholder="Amount"
                        class="border p-2 rounded-md w-full mb-2" />

                    <div class="flex justify-between">
                        <button @click="addEntry" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700">
                            Save
                        </button>
                        <button @click="closeAddForm"
                            class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-700">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>
