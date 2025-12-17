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

                <div v-else class="plan-list">
                  <template v-if="selectedDayPlans.length > 0">
                    <div v-for="plan in selectedDayPlans" :key="plan.plan_pk" class="plan-item">
                      <div class="plan-icon-wrapper">
                        <div 
                          class="plan-icon-placeholder" 
                          :style="{ backgroundColor: getCategoryColor(plan.completeDate) }"
                        ></div>
                      </div>
                      <p>
                        <span class="plan-detail-text">
                          {{ plan.category || '일정' }} | 
                          {{ plan.detail }} | 
                          {{ plan.time }}분
                        </span>
                      </p>
                    </div>
                  </template>
                  
                  <p v-else style="color: #aaa;">이 날짜에 등록된 계획이 없습니다.</p>
                </div>

                <div class="review-photo-container">
                  <textarea 
                    class="review-input" 
                    :placeholder="isReviewEditable ? '리뷰를 남겨보세요' : '계획을 완료해야 리뷰 작성이 가능합니다.'"
                    :disabled="!isReviewEditable"      
                    v-model="reviewContent"
                  ></textarea>
                  <!--  @input="handleReviewInput"
                    :disabled="!isReviewEditable"
                    :class="{'disabled-input': !isAnyPlanCompleted}" -->
                  <div class="photo-placeholder" :style="!isReviewEditable ? 'opacity: 0.5; cursor: not-allowed;': ''">
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
import { ref, computed, onMounted, watch } from 'vue';
import dayjs from 'dayjs';
import 'dayjs/locale/ko'; 
import updateLocale from 'dayjs/plugin/updateLocale';
import { usePlanStore } from '@/stores/plan'; // Pinia Store 임포트
import { useCalenderStore } from '@/stores/calendar';

// --- Debounce 헬퍼 함수 정의 ---
const debounce = (fn, delay) => {
  let timeoutId;
  return function(...args) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
      fn.apply(this, args);
    }, delay);
  };
};

// --- 설정 ---
dayjs.extend(updateLocale);
dayjs.locale('ko');
dayjs.updateLocale('ko', {
  weekStart: 1, // 월요일 시작 설정
}) 
const daysOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

// 임시 카테고리 색상 함수 
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
const calendarStore = useCalenderStore();

const plans = computed(() => planStore.plans);
const isLoading = computed(() => planStore.loading);

// 🎯 자동 저장 관련 상태
const reviewContent = ref(''); 
const lastSavedTime = ref(0); // 마지막 저장 시간 (Throttle 기준)
const MIN_SAVE_INTERVAL = 60000; // 최소 저장 간격 (1분 = 60000ms)

// --- Computed: 월간 달력 계산 ---
const startDayOfWeek = computed(() => {
  const startOfMonth = currentDate.value.startOf('month');
  
  // 월요일을 0으로 맞추는 보정 공식
  let weekday = (startOfMonth.day() - 1 + 7) % 7; 
  return weekday;
});

// --- Computed: 각 달의 날짜 계산 ---
const daysInMonth = computed(() => {
  return currentDate.value.daysInMonth();
});

// --- Computed: 요일 색상 계산 로직 ---
const getDayClass = (dayNum) => {
    const day = currentDate.value.date(dayNum).day(); // 0(일) ~ 6(토)
    
    if (day === 6) { 
        return 'saturday'; 
    } else if (day === 0) { 
        return 'sunday';   
    }
    return '';
}

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
    if(plan.completeDate) {
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
const allPlansByDate = computed(() => {
  const plansMap = {};
  if (!plans.value || plans.value.length === 0) return plansMap;
  
  plans.value.forEach(plan => {
    const dateStr = plan.date;

    if (dateStr) {
      if (!plansMap[dateStr]) {
        plansMap[dateStr] = [];
      }
      plansMap[dateStr].push(plan);
    }
  });
  return plansMap;
})

// --- Computed: 선택된 날짜의 계획들 중 하나라도 completeDate가 있는지 확인 ---
const isReviewEditable = computed(() => {
  return selectedDayPlans.value.some(plan => plan.completeDate);
});

// --- Computed: 선택된 날짜의 모든 계획 리스트 (완료 여부 무관) ---
const selectedDayPlans = computed(() => {
  if (!selectedDate.value) return [];
  const dateKey = selectedDate.value.format('YYYY-MM-DD'); 
  return allPlansByDate.value[dateKey] || [];
});

// --- Computed: 선택된 날짜에 해당하는 리뷰 객체 찾기 ---
const selectedDayReview = computed(() => {
  if (!selectedDate.value || !calendarStore.reviews) return null;

  const dateKey = selectedDate.value.format('YYYY-MM-DD');
  return calendarStore.reviews.find(r => r.completeDate === dateKey) || null;
})

watch(selectedDayReview, (newReview) => {
  if (newReview) {
    // 리뷰가 존재하면 내용을 넣고, content가 null이면 빈 문자열 처리
    reviewContent.value = newReview.content || '';
  } else {
    // 해당 날짜에 리뷰 데이터가 없는 경우 빈칸
    reviewContent.value = '';
  }
}, {Immediate: true})

// --- Actions ---

// 🎯 저장 로직 1: Throttle (최소 60초 간격 제한) 적용
const saveReview = (content) => {
    if (!selectedDate.value || !content.trim()) return; 
    
    const currentTime = Date.now();
    if (currentTime - lastSavedTime.value < MIN_SAVE_INTERVAL) {
        console.log(`[Throttle Skip] 60초 이내에 저장했으므로 API 호출을 건너뜁니다.`);
        return; 
    }

    const reviewData = {
      reviewPk: selectedDayReview.value.reviewPk,
      content,
    }

    
    console.log(`[Autosave - Throttle OK] ${reviewData.reviewPk} 리뷰 내용 저장 실행: ${content}`);
    
    // --- 실제 API 호출 위치 ---
    lastSavedTime.value = currentTime;
};

// 🎯 저장 로직 2: Debounce (1.5초 지연) 적용
const debouncedSaveReview = debounce(saveReview, 1500); 

const handleReviewInput = () => {
    debouncedSaveReview(reviewContent.value);
};


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
  planStore.fetchPlan();
  calendarStore.getReviews();
});
</script>

<style scoped>
/* 전체 컨테이너 및 기본 설정 */
.calendar-container { max-width: 505px; margin: 0 auto; font-family: sans-serif; }
.header { display: flex; justify-content: space-between; align-items: center; padding: 10px; }

/* 🎨 요일 헤더 스타일 (월요일 시작, 토/일 색상) */
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
.days-header .day-name:nth-child(6) { /* 토요일 */
    color: #007bff; 
    font-weight: 700;
}
.days-header .day-name:nth-child(7) { /* 일요일 */
    color: #dc3545; 
    font-weight: 700;
}

/* 월간 그리드 스타일 */
.month-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 10px; }
.day-cell { 
  aspect-ratio: 1/1; display: flex; align-items: center; justify-content: center; 
  background: #eee; /* 기본 배경색 유지 */
  border-radius: 12px; 
  cursor: pointer; 
  transition: 0.2s;
}
.day-cell:hover { background: #ddd; }
.day-cell.empty { background: transparent; cursor: default; }

/* 🔴 날짜 셀의 토/일 색상 스타일 모두 제거 🔴 */
/* .day-cell.saturday, .day-cell.sunday, .day-cell.saturday .date-number, .day-cell.sunday .date-number 관련 스타일 모두 제거됨 */


/* 주간 뷰 스타일 */
.week-view-container { 
    padding: 10px 0;
}

/* 리뷰 카드 스타일 (Figma 디자인 반영) */
.review-card {
  max-width: 505px; 
  min-height: 483px; 
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 55px;
  padding: 30px; 
}

.card-header { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 15px;
}
.card-header h3 {
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 16px;
  line-height: 19px;
  color: #000000;
  margin: 0;
}

.close-btn { 
  background: none; 
  border: none; 
  font-family: 'Noto Sans KR';
  font-weight: 400;
  font-size: 13px; 
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

.review-photo-container{
  width: 100%; 
  gap: 10px;
  display: flex; 
  justify-content: space-between;
  margin-top: 15px;
}

/* 리뷰 입력창 스타일 */
.review-input {
  width: 50%; 
  height: 350.4px; 
  padding: 30px;
  background: #D9D9D9; 
  border: none; 
  border-radius: 40px; 
  resize: none;
  font-size: 14px;
  color: #333;
}

/* 이미지 추가 영역 스타일 */
.photo-placeholder {
  width: 50%; 
  height: 350.4px; 
  background: #FFFFFF; 
  border: 1px solid #ECECEC;
  border-radius: 20px; 
  box-sizing: border-box; 
  cursor: pointer;
  transition: background 0.2s;

  display: flex; 
  flex-direction: column; 
  align-items: center; 
  justify-content: center; 
  color: #000000; 
  font-weight: 700; 
}

.plus-icon{
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 20px; 
  line-height: 1;
  margin-bottom: 5px;
  color: #7D7D7D; 
  
  display: flex;
  align-items: center;
  justify-content: center;
  width: 19.53px;
  height: 17.68px;
  border-radius: 50%;
  background: rgba(125, 125, 125, 0.33);
  margin-bottom: 10px; 
}

.photo-placeholder:hover{
  background: #f5f5f5; 
}
.photo-placeholder span:last-child {
  font-family: 'Noto Sans KR';
  font-weight: 700;
  font-size: 8px; 
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
  padding: 0; 
  border-radius: 8px;
}

/* 계획 목록 아이템 스타일 */
.plan-item {
  display: flex; 
  align-items: center;
  margin-bottom: 10px;
  font-size: 0.95rem;
  line-height: 1.4;
  color: #444;
}

.plan-item p {
  margin: 0;
}

.plan-icon-wrapper {
  margin-right: 8px;
}
.plan-icon-placeholder {
  width: 22.19px; 
  height: 20.09px; 
  background: #769BEF; 
  border-radius: 10px; 
}

.plan-detail-text {
  font-family: 'Noto Sans KR';
  font-style: normal;
  font-weight: 400;
  font-size: 12px; 
  line-height: 14px;
  color: #000000;
}
</style>