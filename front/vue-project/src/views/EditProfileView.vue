<template>
  <AppHeader />
  <div class="edit-container">
    <InputConfirmModal 
    :isOpen="showPasswordModal" 
    title="비밀번호 확인"
    inputType="password"
    @close="showPasswordModal = false" 
    @confirm="saveChanges" />

    <div class="edit-card">
      <h2 class="edit-title">정보 수정</h2>

      <div class="input-group">
        <div class="input-row">
          <label class="input-label">이메일</label>
          <input type="email" class="input-value" v-model="formData.email" required />
        </div>

        <div class="input-row">
          <label class="input-label">이름</label>
          <input type="text" class="input-value" v-model="formData.name" required />
        </div>

        <div class="input-row">
          <label class="input-label">생년월일</label>
          <input type="date" class="input-value" v-model="formData.birth" required />
        </div>

        <div class="input-row">
          <label class="input-label">성별</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" name="gender" value="M" v-model="formData.gender" />
              남
            </label>
            <label class="radio-label">
              <input type="radio" name="gender" value="F" v-model="formData.gender" />
              여
            </label>
          </div>
        </div>

        <div class="input-row">
          <label class="input-label">키</label>
          <input type="text" class="input-value" v-model="formData.height" required />
        </div>

        <div class="input-row">
          <label class="input-label">몸무게</label>
          <input type="text" class="input-value" v-model="formData.weight" required />
        </div>
      </div>

      <div class="button-group">
        <button class="action-button cancel-button" @click="cancel">
          취소
        </button>
        <button class="action-button save-button" @click="showPasswordModal = true">
          저장
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AppHeader from '@/components/AppHeader.vue';
import InputConfirmModal from '@/components/InputConfirmModal.vue';
import { useAlert } from '@/composables/useAlert';

const router = useRouter();
const auth = useAuthStore();
const alert = useAlert();

const showPasswordModal = ref(false);
const formData = ref({
  email: '',
  name: '',
  birth: '',
  gender: '',
  height: '',
  weight: ''
});

// 페이지 로드 시 기존 정보 불러오기
onMounted(() => {
  formData.value = {
    email: auth.user.email,
    name: auth.user.name,
    birth: auth.user.birth,
    gender: auth.user.gender,
    height: auth.user.height,
    weight: auth.user.weight
  };
});

const cancel = () => {
  router.push('/mypage');
};

const saveChanges = async (password) => {
  console.log('저장할 데이터:', formData.value);
  console.log('입력 비밀번호:', password);

  try {
    await auth.editProfile(password, formData);
    alert.show('정보가 수정되었습니다!', 'sucess');

    router.push('/mypage');
  } catch (e) {
    alert.show(auth.error, 'error');
  }
};

</script>

<style scoped>
.edit-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  gap: 2rem;
}

.edit-card {
  position: relative;
  width: 23.4375rem;
  /* 375px */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;
  /* 55px */
  padding: 2.625rem 2.5rem;
  /* 42px 40px */
  box-sizing: border-box;
}

.edit-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  /* 16px */
  line-height: 1.1875rem;
  /* 19px */
  color: #000000;
  margin: 0 0 3rem 0;
  /* 48px */
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
  /* 11.2px */
  margin-bottom: 3rem;
  /* 48px */
}

.input-row {
  display: flex;
  align-items: center;
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.9375rem;
  /* 15px */
  line-height: 2.5rem;
  /* 40px */
  color: #000000;
}

.input-label {
  font-weight: 400;
  min-width: 5.125rem;
}

.input-value {
  flex: 1;
  font-weight: 400;
  height: 1.5rem;
  /* 24px */
  background: #D9D9D9;
  border: none;
  border-radius: 0.5rem;
  /* 8px */
  padding: 0 0.75rem;
  /* 8px */
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.875rem;
  /* 14px */
  box-sizing: border-box;
}

.input-value:focus {
  outline: none;
  background: #CACACA;
}

.radio-group {
  display: flex;
  gap: 1rem;
  flex: 1;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.875rem;
  color: #000000;
  cursor: pointer;
}

.radio-label input[type="radio"] {
  cursor: pointer;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 0.75rem;
  /* 12px */
}

.action-button {
  width: 6.1875rem;
  /* 99px */
  height: 2.5rem;
  /* 40px */
  background: #7D7D7D;
  border: 1px solid #7D7D7D;
  border-radius: 1.875rem;
  /* 30px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.75rem;
  /* 12px */
  line-height: 0.875rem;
  /* 14px */
  text-align: center;
  color: #FFFFFF;
  cursor: pointer;
  transition: background 0.3s;
}

.cancel-button {
  background: #7D7D7D;
}

.cancel-button:hover {
  background: #5D5D5D;
}

.save-button {
  background: #769BEF;
  border-color: #769BEF;
}

.save-button:hover {
  background: #5A85E0;
}
</style>