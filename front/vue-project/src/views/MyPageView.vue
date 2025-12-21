<template>
  <AppHeader/>
  <div class="mypage-container">
    <PasswordConfirmModal 
    :isOpen="showPasswordModal" 
    @close="showPasswordModal = false" 
    @confirm="confirmWithdraw" />

    <div class="mypage-card">
      <h2 class="mypage-title">내 정보</h2>
      
      <div class="info-group">
        <div class="info-row">
          <span class="info-label">이메일</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.email }}</span>
        </div>
        
        <div class="info-row">
          <span class="info-label">이름</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.name }}</span>
        </div>
        
        <div class="info-row">
          <span class="info-label">생년월일</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.birth }}</span>
        </div>
        
        <div class="info-row">
          <span class="info-label">성별</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.gender }}</span>
        </div>
        
        <div class="info-row">
          <span class="info-label">키</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.height }}</span>
        </div>
        
        <div class="info-row">
          <span class="info-label">몸무게</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.weight }}</span>
        </div>
        
        <div class="info-row">
          <span class="info-label">WBTI</span>
          <span class="info-divider">|</span>
          <span class="info-value">{{ auth.user?.mbtiName }}</span>
        </div>
      </div>
      
      <div class="button-group">
        <button class="action-button" @click="goToEditInfo">
          정보 수정
        </button>
        <button class="action-button" @click="goToWbtiTest">
          WBTI 재검사
        </button>
      </div>
    </div>
    
    <div class="withdraw-section">
      <a href="#" class="logout-link" @click.prevent="doLogout">
        로그아웃 
      </a>

      <a href="#" class="withdraw-link" @click.prevent="showPasswordModal = true">
        탈퇴하시겠습니까?
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import AppHeader from '@/components/AppHeader.vue';
import PasswordConfirmModal from '@/components/PasswordConfirmModal.vue';

const router = useRouter();
const auth = useAuthStore();

const message = ref("");
const showPasswordModal = ref(false);

const goToEditInfo = () => {
  router.push('/edit-profile');
};

const goToWbtiTest = () => {
  router.push('/wbti-test');
};

const doLogout = async () => {
  try {
    await auth.logout();

    message.value = "로그아웃 성공!";
    router.push('/'); 
  } catch (error) {
    console.error("로그아웃 실패:", error);
  }
};

const confirmWithdraw = async (password) => {
  try {
    await auth.withdraw(password);
    alert('회원탈퇴 되었습니다!');

    router.push('/home');
  } catch (e) {
    alert(auth.error);
  }
};
</script>

<style scoped>
.mypage-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  gap: 2rem;
}

.mypage-card {
  position: relative;
  width: 23.4375rem;  /* 375px */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;  /* 55px */
  padding: 2.625rem 2.5rem;  /* 42px 40px */
  box-sizing: border-box;
}

.mypage-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;  /* 16px */
  line-height: 1.1875rem;  /* 19px */
  color: #000000;
  margin: 0 0 3rem 0;  /* 48px */
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 0.7rem; /* 11.2px */
  margin-bottom: 3rem;  /* 48px */
}

.info-row {
  display: flex;
  align-items: center;
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.9375rem;  /* 15px */
  line-height: 2.5rem;  /* 40px */
  color: #000000;
}

.info-label {
  font-weight: 400;
  min-width: 4.375rem;  /* 70px */
}

.info-divider {
  margin: 0 0.75rem;  /* 12px */
  color: #000000;
}

.info-value {
  font-weight: 400;
  flex: 1;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 0.75rem;  /* 12px */
}

.action-button {
  width: 6.1875rem;  /* 99px */
  height: 2.5rem;  /* 40px */
  background: #7D7D7D;
  border: 1px solid #7D7D7D;
  border-radius: 1.875rem;  /* 30px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.75rem;  /* 12px */
  line-height: 0.875rem;  /* 14px */
  text-align: center;
  color: #FFFFFF;
  cursor: pointer;
  transition: background 0.3s;
}

.action-button:hover {
  background: #5D5D5D;
}

.withdraw-section {
  /* text-align: center; */
  display: flex;
  gap: 2.5rem;
}

.logout-link,
.withdraw-link {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.75rem;  /* 12px */
  line-height: 0.875rem;  /* 14px */
  text-align: center;
  color: #000000;
  text-decoration: none;
  padding-bottom: 0.25rem;
  border-bottom: 1px solid #000000;
  transition: color 0.3s, border-color 0.3s;
}

.withdraw-link:hover {
  color: #FF4444;
  border-bottom-color: #FF4444;
}
</style>