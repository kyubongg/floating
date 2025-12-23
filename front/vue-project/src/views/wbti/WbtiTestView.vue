<template>
  <div class="main-page">
    <WbtiIntro v-if="currentStep === 'INTRO'" @start="currentStep = 'TEST'" />

    <WbtiTest v-else-if="currentStep === 'TEST'" @complete="handleTestComplete" />

    <div v-else-if="currentStep === 'LOADING'" class="loading-view">
       <p>AI가 분석 중입니다...</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useWbtiStore } from '@/stores/wbti';
import WbtiIntro from '@/components/wbti/WbtiIntro.vue';
import WbtiTest from '@/components/wbti/WbtiTest.vue';
import { useAuthStore } from '@/stores/auth';
import { useAlert } from '@/composables/useAlert';

const router = useRouter();
const authStore = useAuthStore();
const wbtiStore = useWbtiStore();
const currentStep = ref('INTRO');
const alert = useAlert();

const handleTestComplete = async () => {
  currentStep.value = 'LOADING';
  try {
    await wbtiStore.submitResults(); // AI 호출 및 DB 저장
    router.replace({ name: 'wbtiResult' });
  } catch (error) {
    alert.show('오류가 발생했습니다.', 'warning');
    currentStep.value = 'TEST';
  }
};

</script>

<style scoped>
.main-page {
  background-color: #6C6C6C;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.loading-view { color: white; text-align: center; }
</style>