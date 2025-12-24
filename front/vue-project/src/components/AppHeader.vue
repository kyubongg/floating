<template>
  <header class="app-header">
    <nav class="header-nav">
      <router-link 
        to="/home" 
        class="nav-item"
        :class="{ active: isActive('/home') }"
      >
        FLOATING
      </router-link>
      
      <router-link 
        to="/calendar" 
        class="nav-item"
        :class="{ active: isActive('/calendar') }"
      >
        월간기록
      </router-link>
      
      <router-link 
        to="/mypage" 
        class="nav-item"
        :class="{ active: isActive('/mypage') || isActive('/edit-profile') }"
      >
        마이페이지
      </router-link>

      <div
        class="nav-item welcome-user">
        {{ auth.userName }} 님
      </div>
      
    </nav>
  </header>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const route = useRoute();
const auth = useAuthStore();

const isActive = (path) => {
  return route.path === path;
};

</script>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: #FFFFFF;
  z-index: 100;
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
}

.header-nav {
  display: flex;
  align-items: center;
  gap: 3rem;  /* 48px */
  max-width: 51.125rem;  
  height: 3.3125rem;  /* 53px */
  margin: 0 auto;
}

.nav-item {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.95rem;  /* 14px */
  line-height: 1.0625rem;  /* 17px */
  color: #000000;
  text-decoration: none;
  position: relative;
  cursor: pointer;
  transition: color 0.2s;
  background: none;
  border: none;
  padding: 0;
}

.nav-item:last-child {
  cursor: default
}

/* 첫 번째 아이템 (FLOATING)은 항상 파란색 */
.nav-item:first-child {
  font-weight: 700;
  font-size: 0.95rem;  /* 15px */
  line-height: 1.0625rem;  /* 18px */
  color: #769BEF;
}

/* 호버 효과 */
.nav-item:not(:first-child, :last-child):hover {
  color: #769BEF;
}

/* 활성 상태 (현재 페이지) */
.nav-item.active:not(:first-child, :last-child)::after {
  content: '';
  position: absolute;
  bottom: -0.5rem;
  left: 0;
  width: 100%;
  height: 1px;
  background: #000000;
}

/* userName은 오른쪽 끝으로 */
.welcome-user {
  margin-left: auto;
}

/* 반응형 */
@media (max-width: 768px) {
  .header-nav {
    gap: 1.5rem;
    padding: 0 1rem;
  }
}
</style>