<script setup>
import { ref } from "vue";
import axios from "axios";
import { PhEnvelope } from "@phosphor-icons/vue";

// Reactive form state
const form = ref({
    name: "",
    email: "",
    phoneNumber: "",
    message: "",
});

// Function to send form data
const sendContactForm = async () => {
    try {
        console.log("fsafas")
        const response = await axios.post("http://localhost:8080/api/contact", form.value, {
            headers: { "Content-Type": "application/json" },
        });

        alert("Message sent successfully!");
        console.log("Response:", response.data);

        // Reset form after successful submission
        form.value = { name: "", email: "", phoneNumber: "", message: "" };
    } catch (error) {
        console.error("Error sending message:", error.response ? error.response.data : error);
        alert("Failed to send message. Please try again.");
    }
};
</script>

<template>
    <section class="lg:my-5 h-[80vh] mb-20">
        <div class="flex flex-col max-w-3xl bg-slate-50 rounded-3xl shadow-lg mx-auto items-center p-6">
            <PhEnvelope  class="gradient-background-bottom-right text-white text-5xl p-2 rounded-xl" />
            <h2 class="text-3xl font-semibold my-4 mx-auto">Contact Me</h2>
            <div class="flex flex-col w-[80%] items-center">
                <form @submit.prevent="sendContactForm" class="flex flex-col w-[80%] space-y-6">

                    <!-- Floating Label Input -->
                    <div class="relative bg-white">
                        <input type="text" id="name" v-model="form.name" required
                            class="peer w-full px-3 pt-5 pb-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-transparent text-gray-900 placeholder-transparent text-lg" 
                            placeholder="Name"/>
                        <label for="name"
                            class="absolute left-3 text-gray-500 transition-all 
                                   peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-lg 
                                   peer-focus:top-1 peer-focus:-translate-y-0 peer-focus:text-sm peer-focus:text-blue-500"
                            :class="{ 'top-1 -translate-y-0 text-sm text-blue-500': form.name }">
                            Name
                        </label>
                    </div>

                    <div class="relative bg-white">
                        <input type="email" id="email" v-model="form.email" required
                            class="peer w-full px-3 pt-5 pb-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-transparent text-gray-900 placeholder-transparent text-lg" 
                            placeholder="Email"/>
                        <label for="email"
                            class="absolute left-3 text-gray-500 transition-all 
                                   peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-lg 
                                   peer-focus:top-1 peer-focus:-translate-y-0 peer-focus:text-sm peer-focus:text-blue-500"
                            :class="{ 'top-1 -translate-y-0 text-sm text-blue-500': form.email }">
                            Email
                        </label>
                    </div>

                    <div class="relative bg-white">
                        <input type="tel" id="phone" v-model="form.phoneNumber" required
                            class="peer w-full px-3 pt-5 pb-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-transparent text-gray-900 placeholder-transparent text-lg" 
                            placeholder="Phone Number"/>
                        <label for="phone"
                            class="absolute left-3 text-gray-500 transition-all 
                                   peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-lg 
                                   peer-focus:top-1 peer-focus:-translate-y-0 peer-focus:text-sm peer-focus:text-blue-500"
                            :class="{ 'top-1 -translate-y-0 text-sm text-blue-500': form.phoneNumber }">
                            Phone Number
                        </label>
                    </div>

                    <div class="relative bg-white">
                        <textarea id="message" v-model="form.message" required
                            class="peer w-full px-3 pt-5 pb-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-transparent text-gray-900 h-28 placeholder-transparent text-lg" 
                            placeholder="Message"></textarea>
                        <label for="message"
                            class="absolute left-3 text-gray-500 transition-all 
                                   peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-lg 
                                   peer-focus:top-1 peer-focus:-translate-y-0 peer-focus:text-sm peer-focus:text-blue-500"
                            :class="{ 'top-1 -translate-y-0 text-sm text-blue-500': form.message }">
                            Message
                        </label>
                    </div>

                    <button type="submit"
                        class="bg-blue-600 text-white font-semibold py-2 rounded-md hover:bg-blue-700 transition w-[30%] mx-auto">
                        Send
                    </button>
                </form>
            </div>
        </div>
    </section>
</template>
