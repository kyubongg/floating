<template>
  <div class="calendar-container">
    <header class="header">
      <button @click="changeMonth(-1)"> &lt; </button>
      <h2>{{ currentDate.format('YYYY년 M월') }}</h2>
      <button @click="changeMonth(1)"> &gt; </button>
    </header>

    <div class="days-header">
      <div v-for="day in daysOfWeek" :key="day" class="day-name">{{ day }}</div>
    </div>

    <div class="calendar-body">
      <Transition name="slide-up" mode="out-in">
        <div v-if="!selectedDate" class="month-grid" key="month-view">
          <div v-for="n in startDayOfWeek" :key="'empty-'+n" class="day-cell empty"></div>
          
          <div 
            v-for="date in daysInMonth" 
            :key="date" 
            class="day-cell"
            @click="selectDate(date)"
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
              @click="selectDateFromWeek(dayObj.dayjsInstance)"
            >
              <span class="day-name-small">{{ dayObj.dayName }}</span>
              <span class="date-number">{{ dayObj.dateNum }}</span>
            </div>
          </div>
          <div class="review-section">
            <div class="review-card">
              <div class="card-header">
                <h3>하루 기록 | {{ selectedDate.format('M월 D일') }}</h3>
                <button class="close-btn" @click="closeReview">X</button>
              </div>
              
              <div class="card-content">
                
                <p v-if="isLoading" style="color: #6b8af0;">계획을 불러오는 중...</p>

                <div v-else-if="hasReview" class="plan-list">
                  <div v-for="plan in selectedDayCompletedPlans" :key="plan.plan_pk" class="plan-item">
                    <p>
                      <span :title="plan.category">
                        {{ plan.category.substring(0, 3) || '일정' }} 
                      </span> | 
                      {{ plan.detail }} | 
                      {{ plan.time }}분
                    </p>
                  </div>
                </div>
                
                <p v-else style="color: #aaa;">이 날짜에 완료된 계획이 없습니다.</p>

                <div class="review-photo-container">
                  <textarea class="review-input" type="text" placeholder="리뷰를 남겨보세요"></textarea>
                  
                  <div class="photo-placeholder">
                    <span class="plus-icon">+</span>
                    <span>이미지 추가</span>
                  </div>
                </div>
                
              </div>
            </div>
          </div>
        </div>
      </Transition>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import dayjs from 'dayjs';
import 'dayjs/locale/ko'; 
import { usePlanStore } from '@/stores/plan'; // Pinia Store 임포트

// --- 설정 ---
dayjs.locale('ko'); 
const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];

// --- 상태 관리 ---
const currentDate = ref(dayjs()); 
const selectedDate = ref(null);

const planStore = usePlanStore();

// ✅ 수정: Store에서 'loading'으로 노출하고 있으므로, planStore.loading으로 접근해야 합니다.
const plans = computed(() => planStore.plans);
const isLoading = computed(() => planStore.loading);


// --- Computed: 월간 달력 계산 ---
const startDayOfWeek = computed(() => {
  return currentDate.value.startOf('month').day();
});

const daysInMonth = computed(() => {
  return currentDate.value.daysInMonth();
});

// --- Computed: 주간 달력 계산 ---
const currentWeekDays = computed(() => {
  if (!selectedDate.value) return [];

  const startOfWeek = selectedDate.value.startOf('week'); 
  const week = [];
  for (let i = 0; i < 7; i++) {
    const d = startOfWeek.add(i, 'day'); 
    week.push({
      dayjsInstance: d,         
      dateNum: d.date(),        
      dayName: daysOfWeek[i],   
      fullDateStr: d.format('YYYY-MM-DD'), 
      isSelected: d.isSame(selectedDate.value, 'day') 
    });
  }
  return week;
});

// --- Computed: 완료 날짜별로 계획을 그룹화 ---
const completedPlansByDate = computed(() => {
  const plansMap = {};
  if(!plans.value || plans.value.length === 0) return plansMap;

  plans.value.forEach(plan => {
    // Note: Store에서 plans.value = res.data.planList;로 데이터를 가져옴
    if(plan.completeDate) { // Store에서 complete_date가 completeDate로 변경된 것으로 보입니다.
      const dateStr = plan.completeDate; 

      if(!plansMap[dateStr]) {
        plansMap[dateStr] = [];
      }
      plansMap[dateStr].push(plan);
    }
  });
  return plansMap;
})

// --- Computed: 선택된 날짜의 완료된 계획 리스트 ---
const selectedDayCompletedPlans = computed(() => {
  if (!selectedDate.value) return [];

  const dateKey = selectedDate.value.format('YYYY-MM-DD'); 
  return completedPlansByDate.value[dateKey] || [];
})

// --- Computed: 리뷰 유무 ---
const hasReview = computed(() => selectedDayCompletedPlans.value.length > 0);


// --- Actions ---
const changeMonth = (delta) => {
  currentDate.value = currentDate.value.add(delta, 'month');
  selectedDate.value = null; 
};

// 월간 뷰에서 날짜 클릭 시
const selectDate = (dayNum) => {
  selectedDate.value = currentDate.value.date(dayNum);
};

// 주간 뷰에서 날짜 클릭 시 (로직만 유지)
const selectDateFromWeek = (dayjsObj) => {
  selectedDate.value = dayjsObj;
  currentDate.value = dayjsObj; 
};

const closeReview = () => {
  selectedDate.value = null; 
};

// --- Lifecycle ---
onMounted(() => {
  // ✅ 수정: Store의 Action 이름이 fetchPlan으로 변경되었습니다.
  planStore.fetchPlan();
});
</script>

<style scoped>
/* 기존 스타일은 변경 없이 유지됩니다. */
.calendar-container { max-width: 400px; margin: 0 auto; font-family: sans-serif; }
.header { display: flex; justify-content: space-between; align-items: center; padding: 10px; }
.days-header { display: grid; grid-template-columns: repeat(7, 1fr); text-align: center; color: #888; margin-bottom: 10px;}

.month-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 10px; }
.day-cell { 
  aspect-ratio: 1/1; display: flex; align-items: center; justify-content: center; 
  background: #eee; border-radius: 12px; cursor: pointer; transition: 0.2s;
}
.day-cell:hover { background: #ddd; }
.day-cell.empty { background: transparent; cursor: default; }

.week-view-container { 
    padding: 10px 0;
}

.review-card {
  background: white; border-radius: 20px; padding: 20px;
  box-shadow: 0 -5px 20px rgba(0,0,0,0.1); border: 1px solid #f0f0f0;
}
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;}
.close-btn { background: none; border: none; font-size: 1.2rem; cursor: pointer; }

.week-strip { 
    display: flex; 
    justify-content: space-between; 
    margin-bottom: 20px; 
    /* .day-cell.week-cell의 높이를 보정 */
    height: 60px; 
}
.week-cell { 
    flex: 1; 
    margin: 0 2px; 
    flex-direction: column; 
    background: #eee;
    border-radius: 8px; /* 일반 셀보다 덜 둥글게 */
    aspect-ratio: auto; /* 높이 고정 */
    cursor: pointer;
    padding: 5px 0;
    transition: background 0.2s;
}
.week-cell.active { 
    background: #6b8af0; 
    color: white; 
    font-weight: bold; 
}
.day-name-small {
    font-size: 0.75rem;
    margin-bottom: 3px;
    opacity: 0.8;
}

.review-photo-container{
  width: 100%; 
  gap: 10px;
  display: flex; 
  justify-content: space-between;
  margin-top: 15px;
}

.review-input {
  width: 50%; 
  height: 300px; 
  padding: 15px;
  background: #f5f5f5;
  border: 1px solid #ddd; 
  border-radius: 12px;
  resize: none;
  font-size: 14px;
  color: #333;
}

.review-input::placeholder{
  color: #aaa;
}

.photo-placeholder {
  width: 50%; 
  height: 300px; 
  background: #f5f5f5; 
  border-radius: 12px;
  cursor: pointer;
  transition: background 0.2s;

  display: flex; 
  flex-direction: column; 
  align-items: center; 
  justify-content: center; 
  color: #aaa;
}

.photo-placeholder:hover{
  background: #ebebeb;
}

.plus-icon{
  font-size: 2rem;
  line-height: 1;
  margin-bottom: 5px;
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
.plan-list {
  margin-bottom: 20px;
  padding: 10px;
  border: 1px dashed #eee;
  border-radius: 8px;
}
.plan-item {
  margin-bottom: 5px;
  font-size: 0.95rem;
  line-height: 1.4;
  color: #444;
}
.plan-item p {
  margin: 0;
}
</style>