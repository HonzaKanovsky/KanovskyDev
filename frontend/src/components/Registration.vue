<script setup>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();
const form = ref({
    username: "",
    email: "",
    password: ""
});
const errorMessage = ref("");

const registerUser = async () => {
    try {
        const response = await axios.post("http://localhost:8080/api/auth/register", form.value, {
            headers: { "Content-Type": "application/json" }
        });

        alert("Registration successful! You can now log in.");
        router.push("/crypto-tracker/login");
    } catch (error) {
        errorMessage.value = error.response?.data?.error || "Failed to register.";
    }
};
</script>

<template>
    <div class="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md mt-10">
        <h2 class="text-2xl font-semibold text-center mb-4">Register</h2>

        <p v-if="errorMessage" class="text-red-500 text-center">{{ errorMessage }}</p>

        <form @submit.prevent="registerUser" class="space-y-4">
            <input v-model="form.username" type="text" placeholder="Username"
                class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required />
            <input v-model="form.email" type="email" placeholder="Email"
                class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required />
            <input v-model="form.password" type="password" placeholder="Password"
                class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required />

            <button type="submit"
                class="w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-700 transition">Register</button>
        </form>

        <p class="text-center mt-4">Already have an account? 
            <RouterLink to="/crypto-tracker/login" class="text-blue-600 hover:underline">Login here</RouterLink>
        </p>
    </div>
</template>
