<template>
  <AppHeader/>
  <div class="result-container">
    <div class="result-wrapper">

    
      <div class="result-card">
        <!-- 상단: 캐릭터 + 결과 정보 -->
        <div class="result-header">
          <!-- 캐릭터 이미지 -->
          <div class="character-section">
            <img 
              v-if="personaCode !== '진단 중...'"
              :src="getImageUrl(personaCode)" 
              alt="운동 성향 캐릭터" 
              class="character-image" 
            />
          </div>

          <!-- 결과 정보 -->
          <div class="info-section">
            <div class="result-title">
              <h1>{{ personaName }}</h1>
              <h1>{{ personaCode }}</h1>
            </div>

            <!-- 지표 바 -->
            <div class="indicators">
              <div 
                v-for="(indicator, index) in indicators" 
                :key="index"
                class="indicator-row"
              >
                <span class="indicator-label left" v-html="indicator.leftLabel"></span>
                
                <div class="indicator-bar">
                  <div 
                    class="indicator-fill"
                    :style="{ width: indicator.leftPercent + '%' }"
                  ></div>
                </div>
                
                <span class="indicator-label right" v-html="indicator.rightLabel"></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 설명 텍스트 -->
        <div class="description">
          {{ description }}
        </div>
      </div>

      <!-- 하단: 추천 운동 & 경제적 팁 -->
      <div class="tips-section">
        <!-- 추천 운동 -->
        <div class="tip-card">
          <h2 class="tip-title">추천 운동</h2>
          <ul class="tip-list">
            <li v-for="(exercise, index) in recommendedExercises" :key="index">
              {{ exercise }}
            </li>
          </ul>
        </div>

        <!-- 경제적 팁 -->
        <div class="tip-card">
          <h2 class="tip-title">경제적 팁</h2>
          <p class="tip-content">
            {{ economicTip }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import AppHeader from '@/components/AppHeader.vue';
  import { useWbtiStore } from '@/stores/wbti';
  import { onMounted, computed, ref } from 'vue';

  const wbtiStore = useWbtiStore();

  const aiData = computed(() => wbtiStore.aiResponse || {});

  const personaName = computed(() => {
    if (!aiData.value.persona_name) return '진단 중...';
    return `${aiData.value.persona_name}`;
  });

  const personaCode = computed(() => {
    if (!wbtiStore.wbtiResult.code) return '진단 중...';
    return `${wbtiStore.wbtiResult.code}`;
  });

  const description = computed(() => aiData.value.analysis|| '결과를 불러오는 중입니다.');

  const recommendedExercises = computed(() => aiData.value.recommendation?.activities || []);

  // economic_tip 도 마찬가지로 depth를 맞춰줍니다.
  const economicTip = computed(() => aiData.value.recommendation?.economic_tip || '팁을 불러오는 중입니다.');
  
  const indicators = computed(() => {
    const avgs = wbtiStore.wbtiResult.averages;
    return [
        { 
          leftLabel: `${Math.round(avgs.socialAvg * 14.2)}%<br> I (개인)`,
          rightLabel: `${100 - Math.round(avgs.socialAvg * 14.2)}%<br> G (그룹)`, 
          leftPercent: (avgs.socialAvg * 14.2) 
        },
        { 
          leftLabel: `${Math.round(avgs.motivationAvg * 14.2)}%<br> E (과정)`, 
          rightLabel: `${100 - Math.round(avgs.motivationAvg * 14.2)}%<br>  R (결과)`, 
          leftPercent: (avgs.motivationAvg * 14.2) 
        },
        { 
          leftLabel: `${Math.round(avgs.executionAvg * 14.2)}%<br> P (자율)`, 
          rightLabel: `${100 - Math.round(avgs.executionAvg * 14.2)}%<br> J (계획)`, 
          leftPercent: (avgs.executionAvg * 14.2) 
        },
        { 
          leftLabel: `${Math.round(avgs.activityAvg * 14.2)}%<br> C (저강도)`, 
          rightLabel: `${100 - Math.round(avgs.activityAvg * 14.2)}%<br> D (고강도)`, 
          leftPercent: (avgs.activityAvg * 14.2) 
        },
      ];
  });

  // Vite에서 동적 경로 이미지를 가져오는 함수
  const getImageUrl = (code) => {
    if (code === '진단 중...') return '';
    
    // 주의: 경로는 현재 파일(WbtiResultView.vue) 기준으로 상대 경로를 적어야 합니다.
    return new URL(`../../assets/imgs/wbti_result/${code}.jpg`, import.meta.url).href;
  };
  onMounted(async () => {
    // 스토어에 데이터가 없다면(새로고침 된 경우) DB에서 다시 가져옵니다.
    if (!wbtiStore.aiResponse || Object.keys(wbtiStore.aiResponse).length === 0) {
      await wbtiStore.getUserWbti();
    }
  });
</script>

<style scoped>
.result-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 6rem;
  gap: 2rem;
  min-height: 100vh;

}

.result-wrapper {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  min-width: 800px;

  background: #dedede;
  border: 1px solid #dedede;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;  /* 55px */
  padding: 0.5rem 2.25rem 2.25rem 2.25rem;  /* 36px */
  box-sizing: border-box;
}

/* 메인 결과 카드 */
.result-card {
  width: 100%;
  max-width: 52.25rem;  /* 836px */
  
}

/* 상단 헤더 */
.result-header {
  display: flex;
  gap: 2rem;
  margin-bottom: 2.5rem;
}

.character-section {
  flex-shrink: 0;
}

.character-image {
  width: 19.1875rem;  /* 307px */
  height: 20.75rem;  /* 332px */
  object-fit: contain;
  border: none;
  border-radius: 40%;
}

/* 정보 섹션 */
.info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding-top: 1rem;
}

.result-title h1{
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1.5625rem;  /* 25px */
  line-height: 2.5rem;  /* 40px */
  text-align: center;
  color: #000000;
  margin: 0;
}

/* 지표 바 */
.indicators {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;  /* 24px */
}

.indicator-row {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.indicator-label {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.875rem;  /* 14px */
  line-height: 0.9375rem;  /* 15px */
  color: #000000;
  
  white-space: pre-line; 
  display: flex; 
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.indicator-label.left {
  min-width: 3.5625rem;  /* 57px */
  text-align: center;
}

.indicator-label.right {
  min-width: 3.5625rem;  /* 57px */
  text-align: center;
}

.indicator-bar {
  flex: 1;
  height: 1rem;  /* 16px */
  background: #769BEF;
  border-radius: 0.625rem;  /* 10px */
  position: relative;
  overflow: hidden;
}

.indicator-fill {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  background: #FFFFFF;
  border-radius: 0.625rem 0 0 0.625rem;
  transition: width 0.5s ease;
}

/* 설명 */
.description {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 1rem;  /* 16px */
  line-height: 1.875rem;  /* 30px */
  color: #000000;
  text-align: left;
  padding: 0 1rem;
}

/* 팁 섹션 */
.tips-section {
  display: flex;
  gap: 2rem;
  width: 100%;
  max-width: 52.25rem;
}

.tip-card {
  flex: 1;
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;  /* 55px */
  padding: 2rem;
  box-sizing: border-box;
  min-height: 11.5rem;  /* 184px */
}

.tip-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1.25rem;  /* 20px */
  line-height: 2.5rem;  /* 40px */
  text-align: center;
  color: #000000;
  margin: 0 0 1rem 0;
}

.tip-list {
  list-style: none;
  padding: 0;
  margin: 0;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 1rem;  /* 16px */
  line-height: 1.875rem;  /* 30px */
  color: #000000;
  text-align: center;
}

.tip-list li {
  margin-bottom: 0.5rem;
}

.tip-content {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 1rem;  /* 16px */
  line-height: 1.875rem;  /* 30px */
  color: #000000;
  text-align: center;
  margin: 0;
  padding: 0 1rem;
}

/* 반응형 */
@media (max-width: 768px) {
  .result-header {
    flex-direction: column;
    align-items: center;
  }

  .tips-section {
    flex-direction: column;
  }

  .character-image {
    width: 15rem;
    height: auto;
  }
}
</style>