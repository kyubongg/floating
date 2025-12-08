<!-- src/views/HomeView.vue -->
<template>
  <!--
    HomeView (메인 페이지)
    - 이 페이지는 로그인한 사용자만 접근 가능하도록 Router에서 보호됨.
    - 로그인 여부는 Pinia 스토어(auth.user)로 판단.
    - 사용자 정보는 백엔드 세션을 통해 유지되며,
      Vue 앱이 시작될 때 fetchMe()로 동기화된다.
  -->
  <div>
    <h2>메인 화면</h2>
    <p>이 페이지는 로그인한 사용자만 볼 수 있습니다.</p>

    <!-- 로그인된 사용자 정보 표시 -->
    <div v-if="auth.user">
      <p>현재 사용자: {{ auth.user.username }}</p>
    </div>
  </div>
</template>

<script setup>
// Pinia 인증 스토어
import { useAuthStore } from "../stores/auth";

// 현재 로그인한 사용자 상태 접근
const auth = useAuthStore();
</script>
