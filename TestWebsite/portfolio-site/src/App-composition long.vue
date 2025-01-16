<script>
import {ref} from 'vue'

export default {
  setup(){
    const name = ref("John Doe")
    const status = ref("active")

    const activeMsg="user is active"
    const pendingMsg="user is pending"
    const inactiveMsg="user is inactive"
    
    const tasks = ['task1', 'task2', 'task3']

    const linkToGoogle = ref("https://www.google.com/")

    const changeStatus = () => {
      console.log(status.value)
      if (status.value === "active") {
        status.value = "pending"
      } else if (status.value === "pending") {
        status.value = "inactive"
      } else {
        status.value = "active"
      }
    }


    return {
      name,
      status,
      activeMsg,
      pendingMsg,
      inactiveMsg,
      tasks,
      linkToGoogle,
      changeStatus
    }
  }
}
</script>

<template>
  <h1>{{ name }}</h1>
  <p v-if="status === 'active'">{{ activeMsg }}</p>
  <p v-else-if="status === 'pending'">{{ pendingMsg }}</p>
  <p v-else>{{ inactiveMsg }}</p>

  <h3>Tasks</h3>

  <ul>
    <li v-for="task in tasks" :key="task">{{ task }}</li>
  </ul>

  <a v-bind:href="linkToGoogle">Click to see google</a>
  <a :href="linkToGoogle">Click to see google short</a>


  <button v-on:click="changeStatus">Change status</button>
  <button @click="changeStatus">Change status</button>

</template>