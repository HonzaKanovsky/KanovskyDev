<script setup>
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();
const form = ref({
    loginMethod: "",
    password: ""
});
const errorMessage = ref("");

const loginUser = async () => {
    try {
        const response = await axios.post("http://localhost:8080/api/auth/login", form.value, {
            headers: { "Content-Type": "application/json" },
            withCredentials: true
        });

        alert("Login successful!");

        // Store userId and accessToken for future API requests
        localStorage.setItem("userId", response.data.userId);
        localStorage.setItem("accessToken", response.data.accessToken);

        router.push("/crypto-tracker/dashboard"); // Redirect to dashboard
    } catch (error) {
        errorMessage.value = error.response?.data?.error || "Invalid login credentials.";
    }
};
</script>


<template>
    <div class="bg-slate-50 h-[80vh] flex flex-row">
        <div class="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md self-center">
            <h2 class="text-2xl font-semibold text-center mb-4">Login</h2>

            <p v-if="errorMessage" class="text-red-500 text-center">{{ errorMessage }}</p>

            <form @submit.prevent="loginUser" class="space-y-4">
                <input v-model="form.loginMethod" type="text" placeholder="Username or Email"
                    class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required />
                <input v-model="form.password" type="password" placeholder="Password"
                    class="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required />

                <button type="submit"
                    class="w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-700 transition">Login</button>
            </form>

            <p class="text-center mt-4">Don't have an account?
                <RouterLink to="/crypto-tracker/register" class="text-blue-600 hover:underline">Register here
                </RouterLink>
            </p>
        </div>
    </div>

</template>
