<script setup>
import {ref, onMounted} from 'vue'


    const name = "John Doe"
    const status = ref("active")

    const activeMsg="user is active"
    const pendingMsg="user is pending"
    const inactiveMsg="user is inactive"


    const tasks = ref(['task1', 'task2', 'task3'])
    const placeHolderText="placeholder"
    const newTask = ref(placeHolderText);

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

    const addTask = () => {
      if(newTask.value.trim() !== ''){
        tasks.value.push(newTask.value)
      }
      newTask.value = placeHolderText;
    }

    const deleteTask = (task,index) => {
      tasks.value.splice(index,1)
    }

    onMounted(async () => {
      try {
        const resonse = await fetch('https://jsonplaceholder.typicode.com/todos');
        const data = await resonse.json();
        tasks.value = data.map((task) => task.title)
      } catch (error) {
        console.log("erorr fetching tasks")
      }
    });
</script>

<template>
  <h1>{{ name }}</h1>
  <p v-if="status === 'active'">{{ activeMsg }}</p>
  <p v-else-if="status === 'pending'">{{ pendingMsg }}</p>
  <p v-else>{{ inactiveMsg }}</p>


  <form @submit.prevent="addTask">
    <label for="newTask">Add Task</label>
    <input type="text" id="newTask" name="newTask" v-model="newTask">
    <button type="submit">Submit</button>
  </form>
  
  <h3>Tasks</h3>
  <ul>
    <li v-for="(task,index) in tasks" :key="task">
      <span>
        {{ task }}
      </span>
      <button @click="deleteTask(task, index)">x</button>
    </li>
  </ul>

  <a v-bind:href="linkToGoogle">Click to see google</a>
  <a :href="linkToGoogle">Click to see google short</a>


  <button v-on:click="changeStatus">Change status</button>
  <button @click="changeStatus">Change status</button>

</template>