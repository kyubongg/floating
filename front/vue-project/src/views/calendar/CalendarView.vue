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


                <div class="photo-placeholder">
                  <span class="plus-icon">+</span>
                  <span>이미지 추가</span>
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

// ✅ Pinia Store의 상태를 computed로 참조 (Store에서 return 해야 함)
const plans = computed(() => planStore.plans);
const isLoading = computed(() => planStore.isLoading);


// --- Computed: 월간 달력 계산 ---
const startDayOfWeek = computed(() => {
  return currentDate.value.startOf('month').day();
});

const daysInMonth = computed(() => {
  return currentDate.value.daysInMonth();
});

// --- Computed: 주간 달력 계산 (템플릿에서 사용되지 않지만 로직은 유지) ---
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
    if(plan.complete_date) {
      const dateStr = plan.complete_date; // 'YYYY-MM-DD' 형식 가정

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

  // ✅ 오류 수정: selectDate 대신 selectedDate (ref) 사용
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

// 주간 뷰에서 날짜 클릭 시 (현재 템플릿에 week-strip이 없지만 로직은 유지)
const selectDateFromWeek = (dayjsObj) => {
  selectedDate.value = dayjsObj;
  currentDate.value = dayjsObj; 
};

const closeReview = () => {
  selectedDate.value = null; 
};

// --- Lifecycle ---
onMounted(() => {
  // ✅ Store의 Action만 호출합니다.
  planStore.getPlans();
});
</script>

<style scoped>
/* 기존 스타일 그대로 유지 */
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
    padding: 10px 0; /* 주간 뷰 컨테이너 padding */
}

/* week-strip 관련 스타일은 현재 템플릿에서 사용되지 않으므로 주석 처리 */
/*
.week-strip { display: flex; justify-content: space-between; margin-bottom: 20px; }
.week-cell { 
  flex: 1; margin: 0 2px; flex-direction: column; background: #eee;
}
.week-cell.active { background: #6b8af0; color: white; font-weight: bold; }
*/

.review-card {
  background: white; border-radius: 20px; padding: 20px;
  box-shadow: 0 -5px 20px rgba(0,0,0,0.1); border: 1px solid #f0f0f0;
}
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;}
.close-btn { background: none; border: none; font-size: 1.2rem; cursor: pointer; }
.photo-placeholder {
  width: 100%; height: 150px; background: #f5f5f5; border-radius: 12px;
  display: flex; flex-direction: column; align-items: center; justify-content: center; color: #aaa;
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