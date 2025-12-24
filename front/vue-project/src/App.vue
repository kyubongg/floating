<template>
  <div>
    <router-view/>
  </div>

  <CustomAlert
    :isOpen="alert.isOpen.value"
    :message="alert.message.value"
    :type="alert.type.value"
    @close="alert.close"
    @confirm="alert.close"
  />
</template>


<script setup>
import CustomAlert from './components/CustomAlert.vue';
import { useAlert } from './composables/useAlert';

const alert = useAlert();

import { provide } from 'vue';
provide('alert', alert);
</script>


<style scoped></style>