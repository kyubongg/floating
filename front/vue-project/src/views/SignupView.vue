<!-- src/views/SignupView.vue -->
<template>
  <!--
    회원가입 페이지(SignupView)
    - username/password/password2 입력
    - 비밀번호 확인 검증
    - 가입 성공 시 success 메시지 표시
    - 실패 시 auth.error 렌더링
  -->
  <div>
    <h2>회원가입</h2>

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

      <!-- 비밀번호 확인 -->
      <div style="margin-top: 8px;">
        <label for="password2">비밀번호 확인</label><br />
        <input id="password2" v-model="password2" type="password" />
      </div>

      <!-- 회원가입 버튼 -->
      <div style="margin-top: 12px;">
        <button type="submit" :disabled="auth.loading">회원가입</button>
      </div>

      <!-- 성공 메시지 -->
      <p v-if="message" style="color: green; margin-top: 8px;">
        {{ message }}
      </p>

      <!-- 실패 메시지 -->
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

// 라우터(사용할 수도 있고 안 사용할 수도 있음)
import { useRouter } from "vue-router";

const auth = useAuthStore();
const router = useRouter();

// form 데이터
const username = ref("");
const password = ref("");
const password2 = ref("");

// 화면에 출력할 성공 메시지
const message = ref("");

// 회원가입 submit 핸들러
const onSubmit = () => {
  message.value = "";
  auth.error = null;

  // 1) 비밀번호 확인
  if (password.value !== password2.value) {
    auth.error = "비밀번호가 일치하지 않습니다.";
    return;
  }

  // 2) 회원가입 요청 (Pinia store → axios → backend)
  auth
    .signup({
      username: username.value,
      password: password.value,
    })
    .then(() => {
      // 성공 메시지 표시
      message.value = "회원가입 성공! 이제 로그인하세요.";
      auth.error = null;

      // 원하면 회원가입 후 자동으로 로그인 페이지 이동도 가능
      // router.push({ name: "login" });
    })
    .catch(() => {
      // 실패 메시지는 auth.error에 저장되므로 여기서 할 일 없음
    });
};
</script>
