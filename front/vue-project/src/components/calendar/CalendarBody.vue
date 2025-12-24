<template>
  <div class="calendar-body">
    <Transition name="slide-up" mode="out-in">
      <div v-if="!selectedDate" class="month-grid" key="month-view">
        <div v-for="n in startDayOfWeek" :key="'empty-cell' + n" class="day-cell empty"></div>
        
        <div 
          v-for="dateNum in daysInMonth" 
          :key="dateNum" 
          class="day-cell"
          :class="[getDayClass(dateNum), getDayStatusClass(dateNum)]"
          @click="$emit('selectDate', dateNum)"
        >
          <span class="date-number">{{ dateNum }}</span>
        </div>
        </div>
      
      <div v-else class="week-view-container" key="week-view">
        <div class="week-strip">
          <div 
            v-for="dayObj in currentWeekDays" 
            :key="dayObj.fullDateStr"
            class="day-cell week-cell"
            :class="{ 
              'active': dayObj.isSelected,
              [getStatusByStr(dayObj.fullDateStr)]: true 
            }"
            @click="$emit('selectDateFromWeek', dayObj.dayjsInstance)"
          >
            <span class="date-number">{{ dayObj.dateNum }}</span>
          </div>
        </div>
        <slot name="review-section"></slot>
      </div>
    </Transition>
  </div>
</template>

<script setup>
  import { usePlanStore } from '@/stores/plan';
  import dayjs from 'dayjs';
  
  const props = defineProps({
    selectedDate: Object,
    startDayOfWeek: Number,
    currentDate: Object,
    daysInMonth: Number,
    currentWeekDays: Array, // 부모로부터 정상적으로 전달받아야 함
    getDayClass: Function,
    plans: Object,
  });

  const planStore = usePlanStore();

  // 월간 뷰용: 날짜 숫자를 받아 상태 클래스 반환
  const getDayStatusClass = (dateNum) => {
    if (!props.currentDate) return '';
    const targetDate = props.currentDate.date(dateNum).format('YYYY-MM-DD');
    return getStatusByStr(targetDate);
  };

  // 공통 로직: 날짜 문자열(YYYY-MM-DD)을 받아 상태 클래스 반환
  const getStatusByStr = (dateStr) => {
    if (!planStore.plans || planStore.plans.length === 0) return '';

    // 해당 날짜에 완료된 계획이 있는지 먼저 확인 (상태 구분을 위해)
    const plan = planStore.plans.find(p => p.date === dateStr || p.completeDate === dateStr);

    if (!plan) return ''; 

    // 완료일(completeDate) 존재 여부에 따라 클래스 분기
    return plan.completeDate ? 'status-completed' : 'status-planned';
  };

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
    color: black; 
    font-weight: bold;
    border: 2px solid #D9D9D9; 
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

/* 1. 계획이 없는 날 (기본) */
.day-cell {
  background: #fff; /* 아주 연한 회색 또는 흰색 */
  border: 1.3px solid #d9d9d9;
  color: #333;
}

/* 2. 계획이 있고 완료하지 않은 날 (Planned) */
.day-cell.status-planned {
  background-color: #D9D9D9 !important; /* 중간 회색 */
  border: 1.3px solid #d9d9d9;
}

/* 3. 계획이 있고 완료한 날 (Completed) */
.day-cell.status-completed {
  background-color: #769BEF !important; /* 해달이 테마 파란색 */
  border: 1.3px solid #d9d9d9;
  color: white;
  border: none;
}

/* 호버 효과 유지 */
.day-cell:hover:not(.empty) {
  filter: brightness(0.9);
}
</style>