<template>
  <div>
    <header class="header">
      <button @click="$emit('changeMonth', -1)"> &lt; </button>
      <h2>{{ currentDate.format('YYYY년 M월') }}</h2>
      <button @click="$emit('changeMonth', 1)"> &gt; </button>
    </header>
  </div>
</template>

<script setup>
  
  defineProps({
    currentDate: {
      type: Object,
      required: true
    }
  });

  defineEmits(['changeMonth']);

</script>

<style scoped>
.header { display: flex; justify-content: space-between; align-items: center; padding: 10px; }
</style>