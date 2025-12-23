<!-- src/views/FindInfoView.vue -->
<template>
  <!--
    ID/비밀번호 찾기 화면(FindInfoView)
  -->
  <div>
    <div class="find-container">
      <InputConfirmModal :isOpen="ShowEmailModal" title="이메일 코드 인증" inputType="emailCode"
        @close="ShowEmailModal = false" @confirm="handleVerifyConfirm" />

      <div class="find-card">
        <h2 class="find-title">{{ title }}</h2>

        <!-- 에러 메시지 -->
        <p v-if="auth.error" class="error-message">
          {{ auth.error }}
        </p>

        <form @submit.prevent="onSubmit">
          <div class="input-group">
            <div class="input-wrapper">
              <label class="input-label">이름</label>
              <input type="name" class="input-field" v-model="name" required />
            </div>

            <div class="input-wrapper">
              <label class="input-label">이메일</label>
              <input type="email" class="input-field" v-model="email" required />
            </div>
          </div>

          <button class="find-button">확인</button>
        </form>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "../stores/auth";
import { useRouter, useRoute } from "vue-router";
import InputConfirmModal from "@/components/InputConfirmModal.vue";
import { useAlert } from '@/composables/useAlert';

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();
const alert = useAlert();

const name = ref("");
const email = ref("");

const ShowEmailModal = ref(false);

const title = computed(() => {
  const mode = route.query.mode;
  if (mode === 'id') return '아이디 찾기';
  if (mode === 'pw') return '비밀번호 찾기';
  return '내 정보 찾기';
});

onMounted(() => {
  auth.error = null;
});

const onSubmit = async () => {
  try {
    await auth.sendVerificationCode(name.value, email.value, route.query.mode);
    alert.show("인증번호가 발송되었습니다.", 'success');
    ShowEmailModal.value = true;
  } catch (e) {
    // alert(auth.error || "사용자 정보를 확인해주세요.");
  }
};

const handleVerifyConfirm = async (inputCode) => {
  try {
    const res = await auth.verifyCode(email.value, inputCode);

    console.log(res);
    console.log(res.data);
    if (route.query.mode === 'id') {
      alert.show('찾으시는 아이디는 [ ${res.data.data} ] 입니다.', 'success');
      router.push('/login');
    } else {
      alert.show("인증 성공! 비밀번호 재설정 페이지로 이동합니다.", 'success');
      router.push('/resetPassword');
    }

    ShowEmailModal.value = false;
  } catch (e) {
    alert.show(auth.error || "인증번호가 틀렸습니다.", 'error');
  }
};

</script>

<style scoped>
.find-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.find-card {
  position: relative;
  width: 20.0625rem;
  /* 321px */
  height: 16rem;
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;
  /* 55px */
  padding: 2.625rem 2.5625rem;
  /* 42px 41px */
  box-sizing: border-box;
}

.find-title {
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
  font-size: 0.9375rem;
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
  padding: 0 0.5rem;
  /* 8px */
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.625rem;
  /* 10px */
  box-sizing: border-box;
}

.input-field:focus {
  outline: none;
  background: #CACACA;
}

.find-button {
  width: 3rem;
  /* 48px */
  height: 1.8675rem;
  /* 29.88px */
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

.find-button:hover {
  background: #5A85E0;
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