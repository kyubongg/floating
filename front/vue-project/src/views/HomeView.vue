<!-- src/views/HomeView.vue -->
<template>
  <!--
    HomeView (메인 페이지)
    - 이 페이지는 로그인한 사용자만 접근 가능하도록 Router에서 보호됨.
    - 로그인 여부는 Pinia 스토어(auth.user)로 판단.
    - 사용자 정보는 백엔드 세션을 통해 유지되며,
      Vue 앱이 시작될 때 fetchMe()로 동기화된다.
  -->
  <AppHeader/>
  <div class="home-container">
    <div class="top-section">
      <UserStatusCard />
      <PetFeedback />
    </div>

    <WeeklyPlanCard />
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { usePlanStore } from '@/stores/plan';
import AppHeader from '../components/AppHeader.vue';
import UserStatusCard from '../components/UserStatusCard.vue';
import PetFeedback from '../components/PetFeedback.vue';
import WeeklyPlanCard from '../components/WeeklyPlanCard.vue';
import { useWbtiStore } from '@/stores/wbti';

const planStore = usePlanStore();
const wbtiStore = useWbtiStore();

onMounted(() => {
  planStore.fetchPlan();
  wbtiStore.getUserWbti();
});

</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  gap: 2rem;
  margin-top: 100px;
}

.top-section {
  display: flex;
  gap: 1.5rem;
  width: 100%;
  max-width: 51.125rem;
}
</style>