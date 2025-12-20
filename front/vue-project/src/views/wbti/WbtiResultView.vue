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

      <section class="ai-section card">
        <div class="badge">AI 분석 결과</div>
        <p class="ai-text">{{ resultData.analysis }}</p>
      </section>

      <section class="activity-section card">
        <h3 class="section-title">🚀 추천 활동</h3>
        <ul class="activity-list">
          <li v-for="(item, index) in resultData.activities" :key="index" class="activity-item">
            {{ item }}
          </li>
        </ul>
      </section>

      <section class="motivation-section card">
        <div class="quote-container">
          <span class="quote-icon">"</span>
          <p class="motivation-text">{{ resultData.motivationMessage }}</p>
          <span class="quote-icon">"</span>
        </div>
      </section>

      <section class="tip-section card">
        <div class="tip-header">
          <span class="tip-icon">💡</span>
          <span class="section-title">경제적 팁</span>
        </div>
        <p class="tip-text">{{ resultData.economicTip }}</p>
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
  import { computed, onMounted, ref } from 'vue';
  import { useWbtiStore } from '@/stores/wbti';
  import { useAuthStore } from '@/stores/auth';
  import { useRouter } from 'vue-router';

  const wbtiStore = useWbtiStore();
  const authStore = useAuthStore();
  const router = useRouter();

  const isLoading = ref(true);

  // 점수 막대 그래프에 표시할 라벨 정의 (scoreLabels 에러 해결)
  const scoreLabels = {
    socialAvg: { left: '개인형(I)', right: '단체형(G)' },
    motivationAvg: { left: '외적동기(E)', right: '내적동기(R)' },
    executionAvg: { left: '계획적(P)', right: '즉흥적(J)' },
    activityAvg: { left: '정적인(C)', right: '활동적인(D)' }
  };

  // WBTI 결과 및 점수 가져오기
  const wbtiResult = computed(() => wbtiStore.wbtiResult);

  // AI 분석 결과 데이터 연결 
  const resultData = computed(() => {

    const aiResponse = wbtiStore.aiResponse || {};
    const recommendation = aiResponse.recommendation || {};

    return {
      analysis: aiResponse.analysis || "분석 데이터를 찾을 수 없습니다.",
      economicTip: recommendation.economic_tip || "경제적 팁을 불러올 수 없습니다.",
      motivationMessage: aiResponse.motivation_message || "동기부여 메세지를 불러올 수 없습니다.",
      activities: Array.isArray(recommendation.activities) ? recommendation.activities : [recommendation.activities || "추천 활동이 없습니다."],
    };
  });

  // 페르소나 정보 (코드별 이름 정의)
  const persona = computed(() => {
    const code = wbtiResult.value?.code;
    
    return {
      name: wbtiStore.aiResponse.persona_name,
    };
  });

  const goToHome = () => {
    router.push({ name: 'home'})
  }

  onMounted(async () => {
    if (!wbtiStore.aiResponse.analysis) {
      await wbtiStore.getUserWbti();
    }
    isLoading.value = false;
  })
</script>

<style scoped>
/* 기본 레이아웃 */
.result-page {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #F8F9FD;
  min-height: 100vh;
  padding: 40px 20px;
  color: #333;
}

.result-container {
  max-width: 600px;
  margin: 0 auto;
}

.result-header {
  text-align: center;
  margin-bottom: 30px;
}

.wbti-code {
  font-size: 42px;
  color: #769BEF;
  font-weight: 900;
  margin: 0;
}

/* 카드 공통 디자인 */
.card {
  background: #ffffff;
  border-radius: 20px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 16px;
  display: block;
}

/* AI 분석 & 텍스트 */
.badge {
  display: inline-block;
  background: #769BEF;
  color: white;
  font-size: 12px;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 50px;
  margin-bottom: 12px;
}

.ai-text, .tip-text {
  line-height: 1.7;
  font-size: 15px;
  color: #444;
  margin: 0;
}

/* 추천 활동 리스트 */
.activity-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.activity-item {
  background: #f8f9fd;
  margin-bottom: 8px;
  padding: 12px 16px;
  border-radius: 10px;
  font-size: 14px;
  border-left: 4px solid #769BEF;
}

/* 동기부여 섹션 (강조형) */
.motivation-section {
  text-align: center;
  background: linear-gradient(135deg, #769BEF 0%, #5d81d6 100%);
  color: white;
}

.motivation-text {
  font-size: 16px;
  font-weight: 500;
  font-style: italic;
  margin: 0;
}

.quote-icon {
  font-size: 24px;
  opacity: 0.6;
}

/* 버튼 영역 */
.action-area {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 30px;
}

.btn {
  padding: 16px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  border: none;
  transition: 0.2s;
}

.btn.primary { background: #769BEF; color: white; }
.btn.secondary { background: #E8EEFF; color: #769BEF; }
.btn:hover { opacity: 0.9; }

/* 로딩 상태 */
.loading-state {
  text-align: center;
  padding: 100px;
  color: #999;
}
</style>