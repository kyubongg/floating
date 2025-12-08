<template>

  <div>
    <AppHeader/>
    <!-- <StartView/>
    <HomeView/>  -->
    <router-view/>
  </div>
</template>


<script setup>
import StartView from './views/StartView.vue';
import HomeView from './views/HomeView.vue';  
</script>


<style scoped></style>