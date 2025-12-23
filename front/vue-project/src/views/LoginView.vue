<!-- src/views/LoginView.vue -->
<template>
  <!--
    로그인 화면(LoginView)
    - 로그인 폼 입력 후 submit 시 Pinia auth 스토어의 login() 호출
    - 로그인 성공 → redirect 혹은 "/" 페이지로 이동
    - 로그인 실패 → auth.error에 에러 메시지 표시
  -->
  <div>
    <div class="login-container">

      <div class="login-card">
        <h2 class="login-title">로그인</h2>

        <!-- 에러 메시지 -->
        <p v-if="auth.error" class="error-message">
          {{ auth.error }}
        </p>

        <form @submit.prevent="onSubmit">
          <div class="input-group">
            <div class="input-wrapper">
              <label class="input-label">아이디</label>
              <input type="id" class="input-field" v-model="id" required />
            </div>

            <div class="input-wrapper">
              <label class="input-label">비밀번호</label>
              <input type="password" class="input-field" v-model="pw" required />
            </div>
          </div>

          <button class="login-button">로그인</button>
        </form>

        <div class="find-links">
          <router-link :to="{ path: '/findInfo', query: { mode: 'id' } }" class="find-link">
            아이디 찾기
          </router-link>
          <router-link :to="{ path: '/findInfo', query: { mode: 'pw' } }" class="find-link">
            비밀번호 찾기
          </router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
// Vue Composition API
import { ref, onMounted } from "vue";

// Pinia 인증 스토어
import { useAuthStore } from "../stores/auth";

// Router: 로그인 성공 시 redirect 처리
import { useRouter, useRoute } from "vue-router";
import AppHeader from "@/components/AppHeader.vue";

import FindInfoView from "./FindInfoView.vue";

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();

// 폼 입력 데이터
const id = ref("");
const pw = ref("");

// 에러메세지 초기화
onMounted(() => {
  auth.error = null;
});

// 로그인 폼 submit 이벤트 핸들러
const onSubmit = () => {
  // login()은 axios 요청을 포함하므로 Promise 반환됨
  auth
    .login({
      id: id.value,
      pw: pw.value,
    })
    .then(() => {

      if (!auth.hasWbti) {
        router.push('/wbti-test');
      } else {
        router.push('/home');
      }

    })
    .catch(() => {
      // 오류 메시지는 auth.error에 저장되므로 추가 처리 필요 없음
    });
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.login-card {
  position: relative;
  width: 25rem;
  /* 321px */
  height: 25rem;
  /* 306px */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;
  /* 55px */
  padding: 2.5rem;
  /* 42px 41px */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.login-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  /* 16px */
  line-height: 1.1875rem;
  /* 19px */
  color: #000000;
  margin: 0 0 2rem 0;
  /* 32px */
}

.input-wrapper {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  /* 12px */
}

.input-label {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 1rem;
  /* 15px */
  color: #000000;
  min-width: 4.375rem;
  /* 70px */
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  /* 16px */
  margin-bottom: 1.125rem;
  /* 18px */
}

.input-field {
  width: 100%;
  height: 1.4775rem;
  /* 23.64px */
  background: #D9D9D9;
  border: none;
  border-radius: 0.5rem;
  /* 8px */
  padding: 1rem;
  /* 8px */
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 1rem;
  /* 10px */
  box-sizing: border-box;
}

.input-field:focus {
  outline: none;
  background: #CACACA;
}

.login-button {
  width: 5rem;
  height: 2rem;
  
  background: #769BEF;
  border: none;
  border-radius: 0.625rem;
  /* 10px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.8125rem;
  /* 13px */
  color: #FFFFFF;
  cursor: pointer;
  margin: 0 auto;
  display: block;
}

.login-button:hover {
  background: #5A85E0;
}

.find-links {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  /* 8px */
  padding-top: 1.875rem;
  /* 30px */
}

.find-link {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.8rem;
  /* 10px */
  line-height: 0.75rem;
  /* 12px */
  text-align: center;
  color: #000000;
  text-decoration: none;
  padding-bottom: 0.375rem;
  /* 6px */
  border-bottom: 1px solid #000000;
}

.find-link:hover {
  color: #769BEF;
  border-bottom-color: #769BEF;
}

.error-message {
  position: absolute;
  top: 4.375rem;
  /* 70px (제목 아래로 조정) */
  left: 50%;
  transform: translateX(-50%);
  color: #ff4444;
  font-size: 0.75rem;
  /* 12px */
  margin: 0;
  white-space: nowrap;
}
</style>