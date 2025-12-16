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
          <div v-for="n in startDayOfWeek" :key="'empty-cell' + n" class="day-cell empty"></div>
          
          <div 
            v-for="date in daysInMonth" 
            :key="date" 
            class="day-cell"
            :class="getDayClass(date)"
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
                    <div class="plan-icon-wrapper">
                      <div class="plan-icon-placeholder" :style="{ backgroundColor: getCategoryColor(plan.completeDate) }"></div>
                    </div>
                    <p>
                      <span class="plan-detail-text">
                        {{ plan.category || '일정' }} | 
                        {{ plan.detail }} | 
                        {{ plan.time }}분
                      </span>
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
import updateLocale from 'dayjs/plugin/updateLocale';
import { usePlanStore } from '@/stores/plan'; // Pinia Store 임포트

// --- 설정 ---
dayjs.extend(updateLocale);
dayjs.locale('ko');
dayjs.updateLocale('ko', {
  weekStart: 1,
}) 
const daysOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

// 임시 카테고리 색상 함수 (Figma의 아이콘/색상 디자인을 대체)
const getCategoryColor = (completeDate) => {
  
  if (completeDate) {
    return '#769BEF';
  } else {
    return '#D9D9D9';
  }
}

// --- 상태 관리 ---
const currentDate = ref(dayjs()); 
const selectedDate = ref(null);

const planStore = usePlanStore();

// ✅ 수정: Store에서 'loading'으로 노출하고 있으므로, planStore.loading으로 접근해야 합니다.
const plans = computed(() => planStore.plans);
const isLoading = computed(() => planStore.loading);


// --- Computed: 월간 달력 계산 ---
const startDayOfWeek = computed(() => {
  const startOfMonth = currentDate.value.startOf('month');
  
  let weekday = (startOfMonth.day() - 1 + 7) % 7; 
  return weekday;
});


const daysInMonth = computed(() => {
  const startOfMonth = currentDate.value.daysInMonth();

  return startOfMonth;
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

// 특정 날짜가 토/일인지 판단
const getDayClass = (dayNum) => {
  // 0(일) ~ 6(토)
  const day = currentDate.value.date(dayNum).day();

  if(day === 6) {
    return 'saturday';
  } else if (day === 0) {
    return 'sunday';
  }

  return '';
}

// 월간 뷰에서 다음/이전 달 클릭 시
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
.calendar-container { max-width: 505px; margin: 0 auto; font-family: sans-serif; }
.header { display: flex; justify-content: space-between; align-items: center; padding: 10px; }
.header { display: flex; justify-content: space-between; align-items: center; padding: 10px; }

/* 🎨 요일 헤더 수정 */
.days-header { 
    display: grid; 
    grid-template-columns: repeat(7, 1fr); 
    text-align: center; 
    color: #888; 
    margin-bottom: 10px;
}

.days-header .day-name {
    color: #333; 
    font-weight: 500;
}

.days-header .day-name:nth-child(6) {
    color: #007bff; /* 파란색 */
    font-weight: 700;
}

.days-header .day-name:nth-child(7) {
    color: #dc3545; /* 빨간색 */
    font-weight: 700;
}

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

/* 🎨 Figma CSS 반영: review-card (Group 2998, Rectangle 17) */
.review-card {
  /* Figma의 width: 505px, height: 483px 에 가깝게 max-width, min-height 설정 */
  /* Figma의 left/top 위치는 absolute 포지셔닝이므로, relative 포지셔닝인 Vue 컴포넌트에서는 제외 */
  max-width: 505px; /* 컨테이너의 max-width 550px 내에서 제한 */
  min-height: 483px; /* 높이 설정 */
  
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 55px; /* Figma 값 그대로 */
  
  padding: 30px; /* 기존 padding 유지 */
}

/* 🎨 Figma CSS 반영: card-header (하루 기록 | 11월 23일) */
.card-header { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 15px;
}
.card-header h3 {
  /* 하루 기록 | 11월 23일 스타일 */
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 16px;
  line-height: 19px;
  color: #000000;
  margin: 0;
}

/* 🎨 Figma CSS 반영: close-btn (X) */
.close-btn { 
  background: none; 
  border: none; 
  font-family: 'Noto Sans KR';
  font-weight: 400;
  font-size: 13px; /* Figma 값에 가깝게 조정 */
  line-height: 16px;
  cursor: pointer; 
  color: #000000;
}

.week-strip { 
    display: flex; 
    justify-content: space-between; 
    margin-bottom: 20px; 
    height: 60px; 
}
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

/* 🎨 Figma CSS 반영: review-photo-container */
.review-photo-container{
  width: 100%; 
  gap: 10px;
  display: flex; 
  justify-content: space-between;
  margin-top: 15px;
}

/* 🎨 Figma CSS 반영: review-input (Rectangle 61) */
.review-input {
  width: 50%; 
  height: 350.4px; /* Figma 값 반영 */
  padding: 30px;
  background: #D9D9D9; /* Figma 값 반영 */
  border: none; /* Figma에서 border 없음 */
  border-radius: 40px; /* Figma 값 반영 */
  resize: none;
  font-size: 14px;
  color: #333;
}

/* 🎨 Figma CSS 반영: photo-placeholder (image 13) */
.photo-placeholder {
  width: 50%; 
  height: 350.4px; /* review-input과 동일하게 높이 조정 */
  
  /* Figma 값 반영 */
  background: #FFFFFF; 
  border: 1px solid #ECECEC;
  border-radius: 20px; 
  box-sizing: border-box; /* padding/border가 width/height에 포함되도록 */

  cursor: pointer;
  transition: background 0.2s;

  display: flex; 
  flex-direction: column; 
  align-items: center; 
  justify-content: center; 
  color: #000000; /* 텍스트 색상 변경 */
  font-weight: 700; /* 이미지 추가 텍스트 굵기 반영 */
}

/* 🎨 Figma CSS 반영: plus-icon (Ellipse, +) */
.plus-icon{
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 20px; /* Figma 값 반영 */
  line-height: 1;
  margin-bottom: 5px;
  color: #7D7D7D; /* Figma 값 반영 */
  
  /* 배경 타원 스타일 (Figma: Ellipse) */
  display: flex;
  align-items: center;
  justify-content: center;
  width: 19.53px;
  height: 17.68px;
  border-radius: 50%;
  background: rgba(125, 125, 125, 0.33);
  margin-bottom: 10px; /* 텍스트와의 간격 조정 */
}

.photo-placeholder:hover{
  background: #f5f5f5; /* hover 시 배경색 변경 */
}
.photo-placeholder span:last-child {
  /* 이미지 추가 텍스트 스타일 */
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 8px; /* Figma 값 반영 */
  line-height: 10px;
  text-align: center;
  color: #000000;
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
  /* border: 1px dashed #eee; 제거 및 padding 조정 */
  padding: 0; 
  border-radius: 8px;
}

/* 🎨 Figma CSS 반영: plan-item (러닝, 인터벌, 30분) */
.plan-item {
  display: flex; /* 아이콘과 텍스트를 인라인으로 배치 */
  align-items: center;
  margin-bottom: 10px;
  font-size: 0.95rem;
  line-height: 1.4;
  color: #444;
}

.plan-item p {
  margin: 0;
}

/* 카테고리 아이콘/색상 블록 (Figma의 Rectangle 59 대체) */
.plan-icon-wrapper {
  margin-right: 8px;
}
.plan-icon-placeholder {
  width: 22.19px; /* Figma 값 반영 */
  height: 20.09px; /* Figma 값 반영 */
  background: #769BEF; /* 기본값 */
  border-radius: 10px; /* Figma 값 반영 */
}

/* 계획 상세 텍스트 스타일 */
.plan-detail-text {
  font-family: 'Noto Sans KR';
  font-style: normal;
  font-weight: 400;
  font-size: 12px; /* Figma 값 반영 */
  line-height: 14px;
  color: #000000;
}
</style>