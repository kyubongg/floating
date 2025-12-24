<template>
  <div class="loading-container">
    <div class="loader-visual">
      <div class="spinner"></div>
      <div class="character-mini">
        <img src="../../assets/imgs/haedal-main.png" alt="해달 메인 이미지">
      </div>
    </div>
    
    <div class="loading-text">
      <h2>AI 분석가 가동 중...</h2>
      <p>{{ currentMessage }}</p>
    </div>

    <div class="loading-bar-wrapper">
      <div class="loading-bar-fill"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const messages = [
  "당신의 답변을 바탕으로 성향 점수를 계산하고 있어요.",
  "AI가 당신에게 딱 맞는 운동 페르소나를 찾고 있습니다.",
  "현재 경제 상황에 가장 효율적인 운동 팁을 생성 중입니다.",
  "거의 다 됐어요! 당신의 완벽한 솔루션을 준비 중입니다."
];
const currentMessage = ref(messages[0]);

onMounted(() => {
  let i = 0;
  setInterval(() => {
    i = (i + 1) % messages.length;
    currentMessage.value = messages[i];
    console.log(i, currentMessage.value);
  }, 1500);
});
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  font-family: 'Noto Sans KR', sans-serif;
}

.loader-visual {
  position: relative;
  margin-bottom: 30px;
}

.spinner {
  width: 80px;
  height: 80px;
  border: 5px solid rgba(255, 255, 255, 0.9);
  border-top-color: #769BEF;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.character-mini {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 30px;
}

.loading-text {
  color: #000;
}

.loading-text h2 { font-size: 24px; margin-bottom: 10px; }
.loading-text p { font-size: 16px; opacity: 0.8; height: 24px; }

.loading-bar-wrapper {
  width: 250px;
  height: 6px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 10px;
  margin-top: 40px;
  overflow: hidden;
}

.loading-bar-fill {
  width: 100%;
  height: 100%;
  background: #769BEF;
  animation: loadingProgress 4s ease-in-out forwards;
}

.character-mini {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex; /* 내부 이미지 중앙 정렬 */
  justify-content: center;
  align-items: center;
}

.character-mini img {
  width: 45px;     /* 스피너(80px)보다 작은 적절한 크기 설정 */
  height: auto;    /* 비율에 맞춰 높이 자동 조절 */
  object-fit: contain;
  display: block;  /* 하단 공백 제거 */
}


@keyframes spin { to { transform: rotate(360deg); } }
@keyframes loadingProgress { from { width: 0; } to { width: 100%; } }
</style>