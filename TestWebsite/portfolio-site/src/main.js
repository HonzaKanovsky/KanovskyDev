import './assets/main.css'
import 'primeicons/primeicons.css'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import Router from './router'

import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App)
app.use(Router)
app.use(Toast)
app.mount('#app')
