<template>
  <div class="weekly-plan-card">
    <div class="plan-card-header">
      <h2 class="plan-title">이번주 계획</h2>
      <div v-if="planStore.loading">로딩 중...</div>
    </div>
    
    <div class="plan-content">
      <!-- 계획 없을 때 -->
      <div v-if="!planStore.hasWeeklyPlan" class="no-plan-container">
        <button class="plan-button" @click="showCreateModal = true">
          주간 계획 세우기
        </button>
      </div>

      <!-- 계획 있을 때 -->
      <div v-else class="plan-week">
        <div class="week-days">
          <template v-for="(day, index) in planStore.weekDaysWithPlans" :key="day.name">
            <!-- 요일 아이템 -->
            <div class="day-item">
              <div class="day-header">
                <div class="day-indicator" :class="{
                  'completed': day.completed,
                  'pending': !day.completed && !day.hasExercise,
                  'planned': day.hasExercise && !day.completed
                }" @click="handleDayClick(day)"></div>
                <span class="day-name">{{ day.name }}</span>
              </div>
              <div class="exerciseText">
                <div v-if="day.exercise" class="exercise-category">{{ day.exerciseCategory }}</div>
                <div v-if="day.exercise" class="exercise-detail">{{ day.exerciseDetail }}</div>
                <div v-if="day.exercise" class="exercise-time">{{ day.exerciseTime }}</div>
              </div>
              <div v-if="day.date && dayjs().isToday()" class="today-container">
                <p v-if="day.postponed > 0" class="postponed-text">➜ {{ day.postponed }}번 미룸!</p>

                <!-- 오늘 계획 변경하기 버튼 -->
                <button v-if="day.isToday && hasTodayPlan && !day.completed" class="change-button" @click="showChangeModal = true">
                  오늘 계획<br/>변경하기
                </button>
              </div>
              <div v-else></div>          
            </div>

            <!-- 구분선 (마지막 제외) -->
            <div v-if="index < 6" class="divider"></div>
          </template>
        </div>
      </div>
    </div>

    <!-- 주간 계획 생성 모달 -->
    <PlanModal :isOpen="showCreateModal" 
      :isAiLoading="isAiLoading"
      title="어떤 방식으로 이번주 운동 계획을 작성할까요?" 
      primaryText="지난 주 계획 
      다시 
      사용하기"
      secondaryText="AI 기반 
      새로운 계획 
      추천받기" 
      @close="showCreateModal = false" 
      @primary-click="handlePostponePreviousWeek"
      @secondary-click="handleAIRecommendation" />

    <!-- 오늘 계획 변경 모달 -->
    <PlanModal :isOpen="showChangeModal" 
      :isAiLoading="isAiLoading"
      :title="changeModalTitle" 
      primaryText="다음 날로 
      미루기"
      secondaryText="AI기반 
      대체 운동 
      추천받기"  
      :single-button="hasTomorrowPlan"
      @close="showChangeModal = false" 
      @primary-click="handlePostponeToday"
      @secondary-click="handleAIAlternative" />
  </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { usePlanStore } from '@/stores/plan';
import PlanModal from './PlanModal.vue';
import dayjs from 'dayjs';
import isToday from 'dayjs/plugin/isToday'

const router = useRouter();
const auth = useAuthStore();
const planStore = usePlanStore();

const showCreateModal = ref(false);
const showChangeModal = ref(false);
const isAiLoading = ref(false); 

dayjs.extend(isToday);

const hasTodayPlan = computed(() => {
  return planStore.todayPlans && planStore.todayPlans.length > 0;
});

// 날짜 인디케이터 클릭 (완료 토글)
const handleDayClick = async (day) => {
  if (!day.hasExercise) return; // 계획 없으면 무시
  if (day.date !== dayjs().format('YYYY-MM-DD')) return;

  // 이미 완료된 경우 -> 완료 취소
  // 완료 안 된 경우 -> 완료 처리
  try {
    await planStore.togglePlanComplete(day.date);
  } catch (error) {
    console.error('계획 완료 토글 실패:', error);
  }
};

// 내일 계획 유무 확인
const hasTomorrowPlan = computed(() => {
  return planStore.tomorrowPlans && planStore.tomorrowPlans.length > 0;
});

// 내일 계획 유무에 따른 모달 설정
const changeModalTitle = computed(() => {
  return hasTomorrowPlan.value 
    ? "오늘 운동 계획을 수정할까요?"
    : "어떤 방식으로\n오늘 운동 계획을 수정할까요?";
});

const handlePostponePreviousWeek = async () => {
  await planStore.createWeeklyPlan('postpone');
  showCreateModal.value = false;
};

const handleAIRecommendation = async () => {
  try {
    isAiLoading.value = true; // 로딩 시작
    await planStore.createWeeklyPlan('ai'); // AI 호출 및 저장
    showCreateModal.value = false;
  } catch (error) {
    console.error("AI 추천 실패:", error);
  } finally {
    isAiLoading.value = false; // 완료 후 로딩 종료
  }
};

const handlePostponeToday = async () => {
  await planStore.updateTodayPlan('postpone');
  showChangeModal.value = false;
};

const handleAIAlternative = async () => {
  try {
    isAiLoading.value = true; // 로딩 시작
    await planStore.updateTodayPlan('alternative'); // AI 대체 운동 호출
    showChangeModal.value = false;
  } catch (error) {
    console.error("AI 대체 추천 실패:", error);
  } finally {
    isAiLoading.value = false; // 완료 후 로딩 종료
  }
};
</script>


<style scoped>

.plan-card-header {
  display: flex;
  gap: 20px;
}

.weekly-plan-card {
  width: 100%;
  max-width: 51.125rem;
  /* 818px */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;
  /* 55px */
  padding: 2rem 2.5rem;
  /* 32px 40px */
  box-sizing: border-box;
}

.plan-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1.1rem;
  /* 16px */
  line-height: 1.1875rem;
  /* 19px */
  color: #000000;
  margin: 0 0 2.5rem 0;
  /* 40px */
}

.plan-content {
  min-height: 6.1875rem;
  /* 99px */
  display: flex;
  flex-direction: column;
  align-items: center;
}

.no-plan-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
}

.plan-button {
  width: 7.9375rem;
  /* 127px */
  height: 2.5rem;
  /* 40px */
  background: #7D7D7D;
  border: 1px solid #7D7D7D;
  border-radius: 1.875rem;
  /* 30px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.8rem;
  /* 12px */
  line-height: 0.875rem;
  /* 14px */
  text-align: center;
  color: #FFFFFF;
  cursor: pointer;
  transition: background 0.3s;
}

.plan-button:hover {
  background: #5D5D5D;
}

/* 주간 계획 표시 */
.plan-week {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.week-days {
  display: flex;
  justify-content: space-between;
  align-items: stretch;
  width: 97%;
  position: relative;
}

.day-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  /* 12px */
  flex: 1;
  max-width: 3.5rem;
  /* 52px */
}

/* 요일 헤더 */
.day-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.1875rem;
  /* 3px */
}

.day-indicator {
  width: 1.61rem;
  /* 25.76px */
  height: 1.5625rem;
  /* 25px */
  border-radius: 0.625rem;
  /* 10px */
  cursor: pointer;
  transition: all 0.2s;
}

/* 완료됨 - 파란색 */
.day-indicator.completed {
  background: #769BEF;
}

/* 계획있음 - 회색 */
.day-indicator.planned {
  background: #D9D9D9;
}

.day-indicator.planned:hover {
  background: #CACACA;
}

/* 계획없음 - 테두리만 */
.day-indicator.pending {
  border: 1px solid #D9D9D9;
  background: transparent;
  cursor: default;
}

.day-name {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.9375rem;
  /* 15px */
  line-height: 1.125rem;
  /* 18px */
  text-align: center;
  color: #000000;
}

/* 운동 정보 */
.exercise-info {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.75rem;
  /* 12px */
  line-height: 1.25rem;
  /* 20px */
  text-align: center;
  color: #000000;
  white-space: pre-line;
  /* min-height: 3.4375rem; */
  /* 55px */
  width: 20rem;
}

/* 구분선 */
.divider {
  width: 1px;
  align-self: stretch;
  background: #ececec;
  flex-shrink: 0;
  margin: 0 10px;
}

.today-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.postponed-text {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.75rem;
  /* 12px */
  line-height: 0.875rem;
  /* 14px */
  color: #7D7D7D;
  margin: 0;
  white-space: nowrap;
}

/* 오늘 계획 변경하기 버튼 */
.change-button {
  width: 6.1875rem;
  /* 99px */
  height: 2.5rem;
  /* 40px */
  background: #7D7D7D;
  border: 1px solid #7D7D7D;
  border-radius: 1.875rem;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.75rem;
  line-height: 0.875rem;
  text-align: center;
  color: #FFFFFF;
  cursor: pointer;
  transition: background 0.3s;
  margin-top: 0.5rem;
  /* 운동 정보와의 간격 */
}

.change-button:hover {
  background: #5D5D5D;
}
/* 운동 정보 컨테이너 */
.exerciseText {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px; /* 글자 그룹 간의 간격 */
  width: 100%;
  min-height: 5.5rem; /* 높이를 고정하여 요일간 수평 정렬 유지 */
  margin-top: 0.5rem;
}

/* 1. 카테고리 (유산소, 근력 등) */
.exercise-category {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.8rem;
  letter-spacing: -0.02em; /* 자간을 좁혀 세련되게 */
  color: #769BEF; /* 포인트 컬러로 시선 분산 방지 */
  margin-bottom: 2px;
}

/* 2. 상세 내용 (조깅, 스트레칭 등) */
.exercise-detail {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
  font-size: 0.75rem;
  line-height: 1.4; /* 행간을 넓혀 가독성 확보 */
  color: #333333;
  width: 100%;
  word-break: keep-all; /* 한글이 단어 단위로 예쁘게 줄바꿈됨 */
  text-align: center;
}

/* 3. 운동 시간 (50분 등) */
.exercise-time {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.7rem;
  color: #999999; /* 부가 정보는 연하게 처리 */
  margin-top: auto; /* 시간 정보를 항상 하단에 배치 */
}

/* 구분선 (디자인에 맞춰 더 연하게 조정) */
.divider {
  width: 1px;
  height: 8rem;
  background: #dcdcdc; /* 선이 너무 진하면 글자가 묻히므로 연하게 변경 */
  flex-shrink: 0;
}
/* 반응형 */
@media (max-width: 768px) {
  .week-days {
    overflow-x: auto;
    justify-content: flex-start;
    gap: 1.5rem;
  }

  .day-item {
    min-width: 3.25rem;
  }
}
</style>