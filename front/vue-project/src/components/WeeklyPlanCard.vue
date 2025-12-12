<template>
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
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const router = useRouter();
const auth = useAuthStore();
const hasWeeklyPlan = ref(false);
const planList = ref([]);

onMounted(async () => {
    // 주간 계획 가져오기
});

const handleCreatePlan = () => {
    router.push('/weekly-plan');
};
</script>


<style scoped>
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