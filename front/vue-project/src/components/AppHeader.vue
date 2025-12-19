<!-- src/components/AppHeader.vue -->
<template>
  <!--
    AppHeader (전역 헤더)
    - 모든 페이지 상단에 공통으로 표시되는 네비게이션 영역
    - 로그인 상태(auth.isAuthenticated)에 따라 표시되는 UI가 달라진다.
      - 로그인 전: Home / Signup / Login 링크
      - 로그인 후: 사용자명 + 로그아웃 버튼
  -->
  <header
    style="
      padding: 8px 16px;
      border-bottom: 1px solid #ddd;
      display: flex;
      justify-content: space-between;
      align-items: center;
    "
  >
    <!-- 왼쪽 메뉴 -->
    <div>
      <RouterLink to="/home">Home</RouterLink>
      <RouterLink to="/signup" style="margin-left: 8px;">Signup</RouterLink>
      <RouterLink to="/login" style="margin-left: 8px;">Login</RouterLink>
      <RouterLink to="/calendar" style="margin-left: 8px;">Calendar</RouterLink>
    </div>

    <!-- 오른쪽 사용자 정보 -->
    <div v-if="auth.isAuthenticated">
      <span>안녕하세요, {{ auth.userName }}님</span>

      <!-- 로그아웃 버튼 -->
      <button @click="onLogout" style="margin-left: 8px;">
        로그아웃
      </button>
    </div>

    <!-- 로그인되지 않은 상태 -->
    <div v-else>
      <span>로그인되지 않음</span>
    </div>
  </header>
</template>

<script setup>
// 사용자 인증 상태 관리
import { useAuthStore } from "../stores/auth";

// 라우터 이동용
import { RouterLink, useRouter } from "vue-router";

const auth = useAuthStore();
const router = useRouter();

/**
 * 로그아웃 처리
 * - auth.logout() → 서버로 /logout 요청
 * - 성공하면 로그인 페이지로 이동
 */
const onLogout = () => {
  auth.logout().then(() => {
    router.push({ name: "login" });
  });
};
</script>
