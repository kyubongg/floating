<template>
  <div class="calendar-header-wrapper">
    <header class="header">
      <div class="month-display">
        <h2 class="current-month" @click="$emit('resetDate')">{{ currentDate.format('YYYY년 M월') }}</h2>
      </div>
      
      <div class="header-button">
        <button class="nav-btn" @click="$emit('changeMonth', -1)"> &lt; </button>
        <button class="nav-btn" @click="$emit('changeMonth', 1)"> &gt; </button>
      </div>
    </header>
  </div>
</template>

<script setup>

  const props = defineProps({
    currentDate: {
      type: Object,
      required: true
    }
  });
  const emit = defineEmits(['changeMonth', 'resetDate']);
</script>

<style scoped>
/* 전체 폰트 적용 및 패딩 */
.calendar-header-wrapper {
  font-family: 'Noto Sans KR', sans-serif;
  padding: 20px 10px;
}

.header { 
  display: flex; 
  justify-content: space-between;
  align-items: flex-end; 
}

/* 왼쪽 날짜 영역 */
.month-display {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.current-month {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  color: #333;
  cursor: pointer;
}

/* 오른쪽 화살표 버튼 영역 */
.header-button {
  display: flex;
  gap: 20px; 
}

.nav-btn {
  background: none;
  border: none;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  cursor: pointer;
  padding: 5px 10px;
  transition: transform 0.2s, color 0.2s;
}

.nav-btn:hover {
  color: #769BEF;
  transform: scale(1.2);
}

.nav-btn:active {
  transform: scale(0.9);
}
</style>