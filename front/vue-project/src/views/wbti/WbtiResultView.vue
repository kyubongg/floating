<template>
  <div v-if="wbtiResult && authStore.user" class="result-page">
    <div class="result-container">
      <header class="result-header">
        <h1 class="wbti-code">{{ wbtiResult.code }}</h1>
        <h2>"{{ persona.name }}"</h2>
      </header>

      <section class="score-section">
        <div v-for="(labels, key) in scoreLabels" :key="key" class="score-row">
           </div>
      </section>

      <section class="ai-section">
        <div class="badge">AI 분석 결과</div>
        <p class="ai-text">{{ resultData.aiAnalysis }}</p>
      </section>

      <section class="tip-section">
        <p>{{ resultData.aiEconomicTip }}</p>
      </section>

      <footer class="action-area">
        <button class="btn primary" @click.prevent="goToHome">
          홈으로 돌아가기
        </button>
        
        <button class="btn secondary" @click="router.push('/wbti-test')">
          테스트 다시하기
        </button>
      </footer>
    </div>

    
  </div>
  <div v-else class="loading-state">
    결과를 불러오는 중입니다...
  </div>
</template>

<script setup>
  import { computed } from 'vue';
  import { useWbtiStore } from '@/stores/wbti';
  import { useAuthStore } from '@/stores/auth';
  import { useRouter } from 'vue-router';

  const wbtiStore = useWbtiStore();
  const authStore = useAuthStore();
  const router = useRouter();

  // 1. 점수 막대 그래프에 표시할 라벨 정의 (scoreLabels 에러 해결)
  const scoreLabels = {
    socialAvg: { left: '개인형(I)', right: '단체형(G)' },
    motivationAvg: { left: '외적동기(E)', right: '내적동기(R)' },
    executionAvg: { left: '계획적(P)', right: '즉흥적(J)' },
    activityAvg: { left: '정적인(C)', right: '활동적인(D)' }
  };

  // 2. WBTI 결과 및 점수 가져오기
  const wbtiResult = computed(() => wbtiStore.wbtiResult);

  // 3. AI 분석 결과 데이터 연결 (resultData 에러 해결)
  // 만약 스토어에 aiResponse 등이 저장되어 있다면 그것을 연결합니다.
  // 여기서는 authStore.user에 저장된 정보를 기반으로 예시를 작성했습니다.
  const resultData = computed(() => {
    console.log(authStore.user);
    return {
      aiAnalysis: authStore.user?.aiAnalysis || "분석 데이터를 찾을 수 없습니다.",
      aiEconomicTip: authStore.user?.aiEconomicTip || "경제적 팁을 불러올 수 없습니다."
    };
  });

  // 4. 페르소나 정보 (코드별 이름 정의)
  const persona = computed(() => {
    const code = wbtiResult.value?.code;
    // 임시 페르소나 매핑 (실제 데이터에 맞게 수정 가능)
    return {
      name: "열정적인 활동가",
      description: "당신은 에너지가 넘치고 함께 운동할 때 빛나는 사람입니다."
    };
  });

  const goToHome = () => {
    router.push({ name: 'home'})
  }
</script>

<style scoped>
/* Noto Sans KR 적용 */
.result-page {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #F8F9FD;
  min-height: 100vh;
  padding: 60px 20px;
  color: #333;
}

.result-container {
  max-width: 600px;
  margin: 0 auto;
}

.result-header {
  text-align: center;
  margin-bottom: 40px;
}

.user-greeting { font-size: 18px; color: #666; font-weight: 400; }
.wbti-code { font-size: 48px; color: #769BEF; font-weight: 900; letter-spacing: 2px; margin: 10px 0; }
.persona-name { font-size: 24px; font-weight: 700; margin-bottom: 10px; }
.persona-desc { font-size: 16px; color: #888; }

/* 공통 카드 스타일 */
.card {
  background: #ffffff;
  border-radius: 24px;
  padding: 30px;
  margin-bottom: 24px;
  box-shadow: 0 10px 20px rgba(0,0,0,0.03);
}

.section-title { font-size: 18px; font-weight: 700; margin-bottom: 20px; }

/* 점수 표시 스타일 */
.score-row { margin-bottom: 20px; }
.label-box { display: flex; justify-content: space-between; margin-bottom: 8px; }
.type-label { font-size: 13px; color: #999; font-weight: 500; }
.progress-container { display: flex; align-items: center; gap: 15px; }
.progress-bar { flex: 1; height: 10px; background: #eee; border-radius: 5px; overflow: hidden; }
.progress-fill { height: 100%; background: #769BEF; border-radius: 5px; }
.score-badge { font-weight: 700; color: #769BEF; font-size: 14px; width: 25px; }

/* AI 분석 스타일 */
.badge {
  display: inline-block;
  background: #769BEF;
  color: white;
  font-size: 12px;
  font-weight: 700;
  padding: 5px 12px;
  border-radius: 50px;
  margin-bottom: 15px;
}
.ai-text { line-height: 1.8; font-size: 16px; color: #444; }

/* 팁 세션 스타일 */
.tip-header { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
.tip-icon { font-size: 20px; }
.tip-text { font-size: 15px; color: #555; background: #f0f4ff; padding: 20px; border-radius: 15px; line-height: 1.6; }

/* 버튼 영역 */
.action-area { display: flex; flex-direction: column; gap: 12px; margin-top: 40px; }
.btn {
  padding: 18px;
  border-radius: 16px;
  font-size: 17px;
  font-weight: 700;
  cursor: pointer;
  border: none;
  transition: 0.2s;
}
.btn.primary { background: #769BEF; color: white; }
.btn.secondary { background: #E8EEFF; color: #769BEF; }
.btn:hover { transform: translateY(-2px); opacity: 0.9; }

/* 간단한 애니메이션 추가 */
.animate-up {
  animation: slideUp 0.6s ease-out forwards;
  opacity: 0;
}
@keyframes slideUp {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>