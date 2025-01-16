<script>
export default {
  data() {
    return {
      name: "John Doe",
      status: "active",

      activeMsg: "user is active",
      pendingMsg: "user is pending",
      inactiveMsg: "user is inactive",

      tasks: ['task1', 'task2', 'task3'],

      linkToGoogle: "https://www.google.com/"
    }
  },
  methods: {
    changeStatus() {
      console.log(this.status)
      if (this.status === "active") {
        this.status = "pending"
      } else if (this.status === "pending") {
        this.status = "inactive"
      } else {
        this.status = "active"
      }
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