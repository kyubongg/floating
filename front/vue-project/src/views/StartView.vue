<template>
  <div class="main-page"> 

    <div class="logo-area">
      <img src="../assets/imgs/haedal-main.jpeg" alt="FLOATING 로고 수달 이미지" class="logo-area__image"> 
      <div class="logo-area__title">FLOATING</div>
    </div>

    <div class="auth-section">

      <div class="auth-item">
        <div class="auth-item__text">이곳이 처음이신가요?</div>
        <button @click="goToSignup" class="auth-item__button auth-item__button--signup">회원가입</button> 
      </div>

      <div class="auth-item">
        <div class="auth-item__text">이미 회원이신가요?</div>
        <button @click="goToLogin" class="auth-item__button auth-item__button--login">로그인</button>
      </div>
      
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToSignup = () => {
  router.push('/signup')
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>

  /* 1. 페이지 전체 컨테이너: 화면 전체 높이를 차지하고 내용을 중앙 정렬 */
.main-page {
  display: flex;
  flex-direction: column; /* 세로 방향으로 요소 배치 */
  justify-content: center; /* 수직 가운데 정렬 */
  align-items: center; /* 수평 가운데 정렬 */
  min-height: 100vh; /* 뷰포트 전체 높이 사용 */
  background-color: #ffffff;
}

/* --- 로고 영역 --- */

.logo-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 80px; /* 인증 섹션과의 간격 */
}

.logo-area__image {
  width: 99px; /* 원본 크기 유지 */
  height: 111px; /* 원본 크기 유지 */
  margin-bottom: 10px;
  /* (주의: 실제 이미지 URL은 Vue 템플릿의 src 속성에 넣어야 합니다.) */
}

.logo-area__title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 900;
  font-size: 30px;
  color: #000000;
  letter-spacing: 0.25px;
}

/* --- 인증 섹션 --- */

.auth-section {
  display: flex;
  flex-direction: column; /* 아이템들을 세로로 쌓음 */
  gap: 14px; /* 회원가입, 로그인 섹션 사이의 세로 간격 */
}

.auth-item {
  display: flex;
  align-items: center;
  gap: 15px; /* 텍스트와 버튼 사이의 가로 간격 */
}

.auth-item__text {
  /* 폰트 설정 (Inter 16px) */
  font-family: 'Inter', sans-serif;
  font-size: 16px;
  font-weight: 400;
  color: #000000;
  width: 150px; /* 텍스트 너비를 고정하여 버튼 위치를 맞춤 */
  text-align: left; /* 텍스트를 버튼 쪽으로 붙임 */
}

/* --- 버튼 공통 스타일 --- */

.auth-item__button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 109px;
  height: 40px;
  padding: 12px;
  border-radius: 15px;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  transition: background-color 0.2s;
}

/* --- 버튼 고유 스타일 (Modifier) --- */

.auth-item__button--signup {
  background: #769BEF;
  border: 1px solid #769BEF;
  color: #ffffff; /* 텍스트 색상을 흰색으로 지정 */
}

.auth-item__button--login {
  background: #000000;
  border: 1px solid #000000;
  color: #ffffff; /* 텍스트 색상을 흰색으로 지정 */
}
</style>


