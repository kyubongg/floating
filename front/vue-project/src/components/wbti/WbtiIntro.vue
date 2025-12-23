<template>
  <div class="intro-overlay">
    <div class="intro-content">
      <div class="character-wrapper">
        <img src="@/assets/imgs/haedal-workout1.png" alt="WBTI Character" class="character-img" />
      </div>

      <h1 class="intro-title">WBTI 검사 시작하기</h1>
      <p class="intro-description">
        WBTI는 운동 성향을 측정하는 검사입니다.
      </p>

      <button class="start-btn" @click="$emit('start')">
        검사 시작
      </button>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
/* 전역 폰트 적용 (Noto Sans KR) */
.intro-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #DCDEE1; /* 이미지의 배경색과 유사한 진한 회색 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  font-family: 'Noto Sans KR', sans-serif;
}

.intro-content {
  text-align: center;
  color: #000;
}

/* 캐릭터 이미지 스타일 */
.character-wrapper {
  margin-bottom: 24px;
}

.character-img {
  width: 120px; /* 이미지 비율에 맞춰 조정 */
  height: auto;
}

/* 텍스트 스타일 */
.intro-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
}

.intro-description {
  font-size: 16px;
  font-weight: 400;
  color: #000;
  margin-bottom: 40px;
}

/* 버튼 스타일 (이미지의 검정색 라운드 버튼) */
.start-btn {
  background-color: #2D2D2D; /* 아주 어두운 회색/검정 */
  color: #FFFFFF;
  border: none;
  padding: 12px 32px;
  border-radius: 25px; /* 완전한 라운드 형태 */
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.1s ease;
}

.start-btn:hover {
  background-color: #1A1A1A;
  transform: scale(1.05);
}

.start-btn:active {
  transform: scale(0.98);
}
</style>