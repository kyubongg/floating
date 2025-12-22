<template>
    <div class="status-card">
        <h2 class="user-info">{{ authStore.userName }}님 | {{ authStore.user.wbtiCode }} 운동러</h2>

        <!-- 레벨 & 경험치 바 -->
        <div class="level-exp-container">
            <div class="level-badge">
                <img src="../assets/imgs/trophy-icon.png" alt="trophy" class="trophy-icon" />
                <span class="level-text">Lv.{{ userLevel }}</span>
            </div>

            <div class="exp-bar-wrapper">
                <div class="exp-bar">
                    <div class="exp-filled" :style="{ width:scorePercentage + '%' }"></div>
                </div>
                <span class="exp-text">{{ scorePercentage }}/100</span>
            </div>
        </div>

        <!-- 운동 종합 성적 -->
        <div class="character-images">
            <div class="character-item">
                <img src="../assets/imgs/haedal-workout1.png" alt="연속운동" class="character-img" />
                <p class="character-label">연속운동<br />{{ planStore.streakCount }}일</p>
            </div>

            <div class="character-item">
                <img src="../assets/imgs/haedal-workout2.png" alt="총운동" class="character-img" />
                <p class="character-label">총 운동<br />{{ planStore.totalCompletedCount }}일</p>
            </div>

            <div class="character-item">
                <img src="../assets/imgs/haedal-workout3.png" alt="주간운동" class="character-img" />
                <p class="character-label">이번주 운동<br />{{ planStore.weeklyWorkouts }} / 7회</p>
            </div>
        </div>
    </div>
</template>


<script setup>
import { useAuthStore } from '@/stores/auth';
import { usePlanStore } from '@/stores/plan';
import { computed } from 'vue';

const authStore = useAuthStore();
const planStore = usePlanStore();

const userLevel = computed(() => authStore.level);
const scorePercentage = computed(() => authStore.scorePercentage);

</script>


<style scoped> 
.status-card {
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

</style>