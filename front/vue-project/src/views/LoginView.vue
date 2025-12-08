<!-- src/views/LoginView.vue -->
<template>
  <!--
    로그인 화면(LoginView)
    - 로그인 폼 입력 후 submit 시 Pinia auth 스토어의 login() 호출
    - 로그인 성공 → redirect 혹은 "/" 페이지로 이동
    - 로그인 실패 → auth.error에 에러 메시지 표시
  -->
  <div>
    <h2>로그인</h2>

    <form @submit.prevent="onSubmit">
      <!-- 아이디 입력 -->
      <div>
        <label for="username">아이디</label><br />
        <input id="username" v-model="username" type="text" />
      </div>

      <!-- 비밀번호 입력 -->
      <div style="margin-top: 8px;">
        <label for="password">비밀번호</label><br />
        <input id="password" v-model="password" type="password" />
      </div>

      <!-- 로그인 버튼 -->
      <div style="margin-top: 12px;">
        <button type="submit" :disabled="auth.loading">로그인</button>
      </div>

      <!-- 로그인 실패 메시지 -->
      <p v-if="auth.error" style="color: red; margin-top: 8px;">
        {{ auth.error }}
      </p>
    </form>
  </div>
</template>

<script setup>
// Vue Composition API
import { ref } from "vue";

// Pinia 인증 스토어
import { useAuthStore } from "../stores/auth";

// Router: 로그인 성공 시 redirect 처리
import { useRouter, useRoute } from "vue-router";

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();

// 폼 입력 데이터
const username = ref("");
const password = ref("");

// 로그인 폼 submit 이벤트 핸들러
const onSubmit = () => {
  // login()은 axios 요청을 포함하므로 Promise 반환됨
  auth
    .login({
      username: username.value,
      password: password.value,
    })
    .then(() => {
      // redirect 파라미터가 있으면 해당 경로로 이동
      const redirect = route.query.redirect || "/";
      router.push(redirect);
    })
    .catch(() => {
      // 오류 메시지는 auth.error에 저장되므로 추가 처리 필요 없음
    });
};
</script>
