<!-- src/views/CalendarView.vue -->
<template>
  <!-- 
    CalendarView (월간 계획 페이지)
    - 이 페이지는 로그인한 사용자만 접근 가능하도록 Router에서 보호됨.
    - 계획 정보와 리뷰 정보는 백엔드 세션을 통해 유지되며,
      Vue 앱이 시작될 때 fetchPlan(), getReviews()로 동기화된다.
  -->
  <AppHeader/>
  <div class="calendar-container">
    <CalendarHeader :currentDate="currentDate" @changeMonth="changeMonth" />

    <div class="days-header">
      <div v-for="day in daysOfWeek" :key="day" class="day-name">{{ day }}</div>
    </div>

    <CalendarBody
      :selectedDate="selectedDate"
      :startDayOfWeek="startDayOfWeek"
      :daysInMonth="daysInMonth"
      :currentDate="currentDate"
      :getDayClass="getDayClass"
      :currentWeekDays="currentWeekDays"
      @selectDate="selectDate"
      @selectDateFromWeek="selectDateFromWeek"
    >
      <template #review-section>
        <ReviewCard
          v-model="reviewContent"
          :formattedDate="selectedDate.format('M월 D일')"
          :plans="selectedDayPlans"
          :initialImages="initialImages"
          :getCategoryColor="getCategoryColor"
          :isSaving="isSaving"
          @close="closeReview"
          @save="saveReview" 
          @image-upload="handleImageUpload"
        />
      </template>
    </CalendarBody>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import dayjs from 'dayjs';
import 'dayjs/locale/ko'; 
import updateLocale from 'dayjs/plugin/updateLocale';
import { usePlanStore } from '@/stores/plan';
import { useCalenderStore } from '@/stores/calendar';
import AppHeader from '@/components/AppHeader.vue';
import CalendarHeader from '@/components/calendar/CalendarHeader.vue';
import CalendarBody from '@/components/calendar/CalendarBody.vue';
import ReviewCard from '@/components/calendar/ReviewCard.vue';

// --- 설정 ---
dayjs.extend(updateLocale);
dayjs.locale('ko');
dayjs.updateLocale('ko', { weekStart: 1 });
const daysOfWeek = ['월', '화', '수', '목', '금', '토', '일'];

const planStore = usePlanStore();
const calendarStore = useCalenderStore();

// --- 상태 관리 ---
const currentDate = ref(dayjs()); 
const selectedDate = ref(null);
const reviewContent = ref(''); 
const lastSavedTime = ref(0); 
const selectedFiles = ref([]);
const initialImages = ref([]);
const isSaving = ref(false);
const MIN_SAVE_INTERVAL = 60000; // 1분

// --- Computed ---
const plans = computed(() => planStore.plans);
const isLoading = computed(() => planStore.loading);

const startDayOfWeek = computed(() => {
  const startOfMonth = currentDate.value.startOf('month');
  return (startOfMonth.day() - 1 + 7) % 7; 
});

const daysInMonth = computed(() => currentDate.value.daysInMonth());

const getDayClass = (dayNum) => {
    const day = currentDate.value.date(dayNum).day();
    if (day === 6) return 'saturday'; 
    if (day === 0) return 'sunday';   
    return '';
}

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

const allPlansByDate = computed(() => {
  const plansMap = {};
  if (!plans.value) return plansMap;
  plans.value.forEach(plan => {
    const dateStr = plan.date || plan.completeDate;
    if (dateStr) {
      if (!plansMap[dateStr]) plansMap[dateStr] = [];
      plansMap[dateStr].push(plan);
    }
  });
  return plansMap;
});

const selectedDayPlans = computed(() => {
  if (!selectedDate.value) return [];
  const dateKey = selectedDate.value.format('YYYY-MM-DD'); 
  return allPlansByDate.value[dateKey] || [];
});

const selectedDayReview = computed(() => {
  if (!selectedDate.value || !calendarStore.reviews) return null;
  const dateKey = selectedDate.value.format('YYYY-MM-DD');
  return calendarStore.reviews.find(r => r.completeDate === dateKey) || null;
});

// 🎯 데이터 동기화: 날짜 선택 시 기존 리뷰 불러오기
watch(selectedDayReview, (newReview) => {
  // 텍스트 내용 초기화
  reviewContent.value = newReview?.content || '';
  
  // 기존 이미지 URL 초기화
  if (newReview?.imageUrls && newReview.imageUrls.length > 0){
    initialImages.value = newReview.imageUrls;
    
  } else {
    initialImages.value = [];
  }

  selectedFiles.value = [];
}, { immediate: true });

// --- Actions ---
const saveReview = (content) => {
    if (!selectedDate.value || !selectedDayReview.value) return; 
    
    const currentTime = Date.now();
    if (currentTime - lastSavedTime.value < MIN_SAVE_INTERVAL) {
        console.log(`[Throttle] 1분 내 중복 저장 방지`);
        return; 
    }

    isSaving.value = true;

    // 리뷰, 이미지 데이터 형태
    const reviewData = new FormData();

    reviewData.append('reviewPk', selectedDayReview.value.reviewPk);
    reviewData.append('content', content);

    // 기존 이미지 유지 정보 추가
    if (initialImages.value && initialImages.value.length > 0) {
        initialImages.value.forEach((img, index) => {
            reviewData.append(`imgPaths[${index}].imgPk`, img.imgPk);
            reviewData.append(`imgPaths[${index}].imgPath`, img.imgPath);
        });
    }

    // 새로 추가할 이미지 파일 추가
    if (selectedFiles.value && selectedFiles.value.length > 0) {
        selectedFiles.value.forEach(file => {
            reviewData.append('images', file); 
        });
    }
    
    console.log(reviewData);
    try{
      console.log(`[Autosave] 서버 저장 실행:`, reviewData);
      calendarStore.updateReview(reviewData); // API 호출

      lastSavedTime.value = currentTime;

      setTimeout(() => {
        isSaving.value = false;
      }, 1500);

    } catch (error) {
      isSaving.value = false;
    }
    
};

const changeMonth = (delta) => {
  currentDate.value = currentDate.value.add(delta, 'month');
  selectedDate.value = null; 
};

const selectDate = (dayNum) => {
  selectedDate.value = currentDate.value.date(dayNum);
};

const selectDateFromWeek = (dayjsObj) => {
  selectedDate.value = dayjsObj;
  currentDate.value = dayjsObj; 

  console.log(selectDate.value)
};

const closeReview = () => {
  selectedDate.value = null; 
};

const getCategoryColor = (completeDate) => completeDate ? '#769BEF' : '#D9D9D9';

const handleImageUpload = (file) => {
  selectedFiles.value = file;
  console.log("선택된 파일: ", file);
}



onMounted(() => {
  planStore.fetchPlan();
  calendarStore.getReviews();
});
</script>

<style scoped>
/* 전체 컨테이너 및 기본 설정 */
.calendar-container { max-width: 505px; margin: 0 auto; font-family: 'Noto Sans KR', sans-serif; padding-top: 5rem;}
.header { display: flex; justify-content: space-between; align-items: center; padding: 10px;}

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
</style>