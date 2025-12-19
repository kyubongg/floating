<template>
  <div class="calendar-body">
    <Transition name="slide-up" mode="out-in">
      <div v-if="!selectedDate" class="month-grid" key="month-view">
        <div v-for="n in startDayOfWeek" :key="'empty-cell' + n" class="day-cell empty"></div>
        <div 
          v-for="date in daysInMonth" 
          :key="date" 
          class="day-cell"
          :class="getDayClass(date)"
          @click="$emit('selectDate', date)"
        >
          <span class="date-number">{{ date }}</span>
        </div>
      </div>

      <div v-else class="week-view-container" key="week-view">
        <div class="week-strip">
          <div 
            v-for="dayObj in currentWeekDays" 
            :key="dayObj.fullDateStr"
            class="day-cell week-cell"
            :class="{ 'active': dayObj.isSelected }"
            @click="$emit('selectDateFromWeek', dayObj.dayjsInstance)"
          >
            <span class="day-name-small">{{ dayObj.dayName }}</span>
            <span class="date-number">{{ dayObj.dateNum }}</span>
          </div>
        </div>
        
        <slot name="review-section"></slot>
      </div>
    </Transition>
  </div>
</template>

<script setup>
defineProps({
  selectedDate: Object,
  startDayOfWeek: Number,
  daysInMonth: Number,
  currentWeekDays: Array,
  getDayClass: Function // 부모의 함수를 직접 전달받음
});

defineEmits(['selectDate', 'selectDateFromWeek']);
</script>

<style scoped>
/* 1. 월간 그리드 레이아웃 */
.month-grid { 
  display: grid; 
  grid-template-columns: repeat(7, 1fr); 
  gap: 10px; 
}

/* 2. 개별 날짜 셀 스타일 (공통) */
.day-cell { 
  aspect-ratio: 1/1; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  background: #eee; 
  border-radius: 12px; 
  cursor: pointer; 
  transition: 0.2s;
}
.day-cell:hover { background: #ddd; }
.day-cell.empty { background: transparent; cursor: default; }

/* 3. 주간 뷰 컨테이너 및 날짜 스트립 */
.week-view-container { 
    padding: 10px 0;
}
.week-strip { 
    display: flex; 
    justify-content: space-between; 
    margin-bottom: 20px; 
    height: 60px; 
}

/* 4. 주간 개별 셀 스타일 */
.week-cell { 
    flex: 1; 
    margin: 0 2px; 
    flex-direction: column; 
    background: #eee;
    border-radius: 8px; 
    aspect-ratio: auto; 
    cursor: pointer;
    padding: 5px 0;
    transition: background 0.2s;
}
.week-cell.active { 
    background: #769BEF; 
    color: white; 
    font-weight: bold; 
}
.day-name-small {
    font-size: 0.75rem;
    margin-bottom: 3px;
    opacity: 0.8;
}

/* 애니메이션 */
.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.4s ease-in-out;
}
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}
.slide-up-enter-from {
  opacity: 0;
  transform: translateY(30px);
}
</style>