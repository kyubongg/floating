<!-- src/views/HomeView.vue -->
<template>
  <!--
    HomeView (메인 페이지)
    - 이 페이지는 로그인한 사용자만 접근 가능하도록 Router에서 보호됨.
    - 로그인 여부는 Pinia 스토어(auth.user)로 판단.
    - 사용자 정보는 백엔드 세션을 통해 유지되며,
      Vue 앱이 시작될 때 fetchMe()로 동기화된다.
  -->
  <div class="home-container">
    <!-- 상단: 캐릭터 성장 영역 -->
    <div class="top-section">
      <!-- 왼쪽: 사용자 정보 & 캐릭터 카드 -->
      <div class="character-card">
        <h2 class="user-info">{{ auth.user.name }}님 | {{ auth.user.mbtiName }} 운동러</h2>

        <!-- 레벨 & 경험치 바 -->
        <div class="level-exp-container">
          <div class="level-badge">
            <img src="../assets/imgs/trophy-icon.png" alt="trophy" class="trophy-icon" />
            <span class="level-text">Lv.{{ level }}</span>
          </div>

          <div class="exp-bar-wrapper">
            <div class="exp-bar">
              <div class="exp-filled" :style="{ width: expPercentage + '%' }"></div>
            </div>
            <span class="exp-text">{{ currentExp }}/100</span>
          </div>
        </div>

        <!-- 캐릭터 이미지들 -->
        <div class="character-images">
          <div class="character-item">
            <img src="../assets/imgs/haedal-workout1.png" alt="연속운동" class="character-img" />
            <p class="character-label">연속운동<br />{{ consecutiveDays }}일</p>
          </div>

          <div class="character-item">
            <img src="../assets/imgs/haedal-workout2.png" alt="총운동" class="character-img main" />
            <p class="character-label">총 운동<br />{{ totalDays }}일</p>
          </div>

          <div class="character-item">
            <img src="../assets/imgs/haedal-workout3.png" alt="주간운동" class="character-img" />
            <p class="character-label">이번주 운동<br />{{ weeklyCount }} / 7회</p>
          </div>
        </div>
      </div>

      <!-- 오른쪽: 캐릭터 피드백 -->
      <div class="feedback-section">
        <div class="feedback-bubble">
          <img :src="currentPetSrc" class="current-pet" />
          <div class="speech-bubble">
            <p>{{ feedbackMessage }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 하단: 이번주 계획 -->
    <div class="weekly-plan-card">
      <h2 class="plan-title">이번주 계획</h2>

      <div class="plan-content">
        <p v-if="!hasWeeklyPlan">
          <button class="plan-button" @click="createWeeklyPlan">
            주간 계획 세우기
          </button>
        </p>
        <div v-else class="plan-list">
          <!-- 계획 목록 표시 -->
        </div>
      </div>
    </div>

  </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from "../stores/auth";
import haedalMainImg from '../assets/imgs/haedal-main.png'
import haedalSadImg1 from '../assets/imgs/haedal-sad1.png'
import haedalSadImg2 from '../assets/imgs/haedal-sad2.png'
import haedalSadImg3 from '../assets/imgs/haedal-sad3.png'
import haedalHappyImg1 from '../assets/imgs/haedal-happy1.png'
import haedalHappyImg2 from '../assets/imgs/haedal-happy2.png'
import haedalHappyImg3 from '../assets/imgs/haedal-happy3.png'


const router = useRouter();

// 현재 로그인한 사용자 상태 접근
const auth = useAuthStore();
onMounted(() => {
  auth.fetchMe();
});

// 레벨 & 경험치
const currentPetSrc = ref(haedalSadImg2);
const currentExp = ref(0);
const maxExp = ref(100);

const expPercentage = computed(() => {
  return (currentExp.value / maxExp.value) * 100;
});

const level = computed(() => {
  return (currentExp.value % 100) + 1;
})

// 운동 기록
const consecutiveDays = ref(0);
const totalDays = ref(0);
const weeklyCount = ref(0);

// 피드백 메시지
const feedbackMessage = ref('조금만 더 열심히 해줘!');

// 주간 계획
const hasWeeklyPlan = ref(false);

const createWeeklyPlan = () => {
  console.log('주간 계획 세우기');
  // router.push('/weekly-plan');
};
</script>


<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  gap: 2rem;
  min-height: 100vh;
}

/* ===== 상단 섹션 ===== */
.top-section {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: flex-start;
  gap: 1.5rem;
  width: 100%;
  max-width: 51.125rem;
  /* 1136px */
  margin: 0 -2rem;
}

/* 캐릭터 카드 */
.character-card {
  flex: 1;
  max-width: 27rem;
  /* 416px */
  background: rgba(118, 155, 239, 0.5);
  border: 1px solid #FFFFFF;
  border-radius: 3.4375rem;
  /* 55px */
  padding: 1.5rem;
  box-sizing: border-box;
}

.user-info {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  /* 16px */
  line-height: 1.1875rem;
  /* 19px */
  color: #000000;
  margin: 0 0 1.5rem 0;
}

/* 레벨 & 경험치 */
.level-exp-container {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: #FFFFFF;
  border-radius: 1.125rem;
  /* 18px */
  padding: 0.5rem 1rem;
  margin-bottom: 1.5rem;
}

.level-badge {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  background: #FFFFFF;
  border-radius: 1.125rem;
  /* 18px */
  padding: 0.25rem 0.75rem;
}

.trophy-icon {
  width: 0.8375rem;
  /* 13.38px */
  height: 0.8375rem;
}

.level-text {
  font-family: 'Roboto', sans-serif;
  font-size: 0.625rem;
  /* 10px */
  line-height: 0.75rem;
  /* 12px */
  color: #000000;
}

.exp-bar-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.exp-bar {
  flex: 1;
  height: 0.375rem;
  /* 6px */
  background: rgba(120, 120, 120, 0.2);
  border-radius: 0.1875rem;
  /* 3px */
  overflow: hidden;
}

.exp-filled {
  height: 100%;
  background: #769BEF;
  border-radius: 0.1875rem;
  transition: width 0.3s ease;
}

.exp-text {
  font-family: 'Roboto', sans-serif;
  font-size: 0.625rem;
  /* 10px */
  line-height: 0.75rem;
  color: #000000;
  white-space: nowrap;
}

/* 캐릭터 이미지들 */
.character-images {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  gap: 1rem;
}

.character-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.character-img {
  width: 4.125rem;
  /* 66px */
  height: 4.8125rem;
  /* 77px */
  object-fit: contain;
  border-radius: 1.125rem;
}

.character-label {
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.75rem;
  /* 12px */
  line-height: 0.875rem;
  /* 14px */
  text-align: center;
  color: #000000;
  margin: 0;
}

/* 피드백 섹션 */
.feedback-section {
  flex: 1;
  max-width: 21.875rem;
  /* 350px */
  display: flex;
  align-items: center;
  justify-content: center;
}

.feedback-bubble {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.current-pet {
  width: 14.875rem;
  /* 238px */
  height: 16rem;
  /* 256px */
  object-fit: contain;
  margin-bottom: 1rem;
}

.speech-bubble {
  position: absolute;
  top: 1rem;
  right: -5rem;
  background: #FFFFFF;
  border: 2px solid #000000;
  border-radius: 1.5rem;
  padding: 0.75rem 1.25rem;
  max-width: 12.5rem;
  /* 200px */
}

.speech-bubble::before {
  content: '';
  position: absolute;
  bottom: -0.5rem;
  left: 2rem;
  width: 0;
  height: 0;
  border-left: 0.5rem solid transparent;
  border-right: 0.5rem solid transparent;
  border-top: 0.5rem solid #000000;
}

.speech-bubble p {
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.9375rem;
  /* 15px */
  line-height: 1.125rem;
  /* 18px */
  text-align: center;
  color: #000000;
  margin: 0;
}

/* ===== 주간 계획 카드 ===== */
.weekly-plan-card {
  width: 100%;
  max-width: 51.125rem;
  /* 818px */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;
  /* 55px */
  padding: 2rem;
  box-sizing: border-box;
}

.plan-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  /* 16px */
  line-height: 1.1875rem;
  /* 19px */
  /* text-align: center; */
  color: #000000;
  margin: 0 0 2rem 0;
}

.plan-content {
  min-height: 9rem;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 2rem;
}

.no-plan {
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.9375rem;
  color: #7D7D7D;
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
  font-size: 0.75rem;
  /* 12px */
  line-height: 0.875rem;
  /* 14px */
  text-align: center;
  color: #FFFFFF;
  cursor: pointer;
  display: block;
  margin: 0 auto;
  transition: background 0.3s;
}

.plan-button:hover {
  background: #5D5D5D;
}

/* 반응형 */
@media (max-width: 768px) {
  .top-section {
    flex-direction: column;
  }
}
</style>
