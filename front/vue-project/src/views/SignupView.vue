<!-- src/views/SignupView.vue -->
<template>
  <!--
    회원가입 페이지(SignupView)
    - username/password/passwordConfirm 입력
    - 비밀번호 확인 검증
    - 가입 성공 시 success 메시지 표시
    - 실패 시 auth.error 렌더링
  -->
  <div class="signup-container">
    <div class="signup-card">
      <h2 class="signup-title">회원가입</h2>

      <!-- 성공/에러 메시지 -->
      <p v-if="message" class="success-message">{{ message }}</p>
      <p v-if="auth.error" class="error-message">{{ auth.error }}</p>

      <div class="input-group">
        <div class="input-wrapper">
          <label class="input-label">아이디</label>
          <input type="text" class="input-field" v-model="formData.id" required />
        </div>

        <div class="input-wrapper">
          <label :class="['input-label', {'error-text': !isPasswordAble && formData.password?.length > 0 }]">비밀번호</label>
          <input id="pw-input" type="password" class="input-field" placeholder="영문과 숫자를 포함한 8~13자리" v-model="formData.password" required />
        </div>

        <div class="input-wrapper">
          <label :class="['input-label', {'error-text': !isPasswordMatch && formData.passwordConfirm?.length > 0 }]">비밀번호 확인</label>
          <input id="pw-confirm-input" type="password" class="input-field" placeholder="비밀번호 재입력" v-model="formData.passwordConfirm" required />
        </div>

        <div class="input-wrapper">
          <label class="input-label">이메일</label>
          <input type="email" class="input-field" v-model="formData.email" required />
        </div>

        <div class="input-wrapper">
          <label class="input-label">이름</label>
          <input type="text" class="input-field" v-model="formData.name" required />
        </div>

        <div class="input-wrapper">
          <label class="input-label">생년월일</label>
          <input type="date" class="input-field" v-model="formData.birth" required />
        </div>

        <div class="input-wrapper">
          <label class="input-label">성별</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" name="gender" value="M" v-model="formData.gender" required />
              남
            </label>
            <label class="radio-label">
              <input type="radio" name="gender" value="F" v-model="formData.gender" required />
              여
            </label>
          </div>
        </div>

        <div class="input-wrapper">
          <label class="input-label">키 (cm)</label>
          <input type="number" class="input-field" v-model="formData.height" required />
        </div>

        <div class="input-wrapper">
          <label class="input-label">몸무게 (kg)</label>
          <input type="number" class="input-field" v-model="formData.weight" required />
        </div>
      </div>

      <button type="submit" class="signup-button" @click="onSubmit">
        등록
      </button>
    </div>
  </div>
</template>

<script setup>
// Vue Composition API
import { ref, onMounted, computed } from "vue";

// Pinia 인증 스토어
import { useAuthStore } from "../stores/auth";

import { useRouter } from "vue-router";

const auth = useAuthStore();
const router = useRouter();

// form 데이터
const formData = ref({
  id: '',
  password: '',
  passwordConfirm: '',
  email: '',
  name: '',
  birth: '',
  gender: '',
  height: '',
  weight: ''
});

// 화면에 출력할 성공 메시지
const message = ref("");

// 에러메세지 초기화
onMounted(() => {
  auth.error = null;
  message.value = "";
});

// 비밀번호 제한 확인
const isPasswordAble = computed(() => {
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,13}$/;
  return passwordRegex.test(formData.value.password);
})

// 비밀번호 입력 일치 확인
const isPasswordMatch = computed(() => {
  return formData.value.password === formData.value.passwordConfirm;
});

// 회원가입 submit 핸들러
const onSubmit = () => {
  message.value = "";
  auth.error = null;

  // 1) 비밀번호 확인
  if (!isPasswordMatch.value) {
    auth.error = "비밀번호가 일치하지 않습니다.";
    
    const target = document.querySelector('#pw-confirm-input');
    if (target) target.focus();
    return;
  }

  // 2) 비밀번호 제한사항 검증 (숫자, 문자 포함 8~13자리)
  // 문자와 숫자가 각각 최소 하나 이상 포함되어야 함
  if (!isPasswordAble.value) {
    auth.error = "비밀번호는 영문과 숫자를 포함하여 8~13자리여야 합니다.";

    const target = document.querySelector('#pw-input');
    if (target) target.focus();
    return;
  }

  // 3) 회원가입 요청 (Pinia store → axios → backend)
  auth
    .signup({
      id: formData.value.id,
      password: formData.value.password,
      email: formData.value.email,
      name: formData.value.name,
      birth: formData.value.birth,
      gender: formData.value.gender,
      height: formData.value.height,
      weight: formData.value.weight
    })
    .then(() => {
      // 성공 메시지 표시
      message.value = "회원가입 성공! 이제 로그인하세요.";
      auth.error = null;

      router.push('/login');
    })
    .catch(() => {
      // 실패 메시지는 auth.error에 저장되므로 여기서 할 일 없음
    });
};
</script>

<style scoped>
.signup-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.signup-card {
  position: relative;
  width: 24rem;  /* 321px → 384px (크기 증가) */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 3.4375rem;
  padding: 2.625rem 2.5625rem;
  box-sizing: border-box;
}

.signup-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  line-height: 1.1875rem;
  color: #000000;
  margin: 0 0 2rem 0;
}

.success-message {
  position: absolute;
  top: 70px;
  left: 50%;
  transform: translateX(-50%);
  color: #4CAF50;
  font-size: 12px;
  margin: 0;
  white-space: nowrap;
}

.error-message {
  position: absolute;
  top: 70px;
  left: 50%;
  transform: translateX(-50%);
  color: #ff4444;
  font-size: 12px;
  margin: 0;
  white-space: nowrap;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.input-wrapper {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.input-label {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.9375rem;
  color: #000000;
  min-width: 6.5rem;  /* 70px → 104px (라벨 폭 증가로 정렬) */
  flex-shrink: 0;
}

.input-field {
  flex: 1;
  height: 1.4775rem;
  background: #D9D9D9;
  border: none;
  border-radius: 0.5rem;
  padding: 0 0.5rem;
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 0.625rem;
  box-sizing: border-box;
}

.input-field:focus {
  outline: none;
  background: #CACACA;
}

/* ⭐ 라디오 버튼 스타일 */
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

.signup-button {
  width: 3rem;
  height: 2.0625rem;
  background: #769BEF;
  border: none;
  border-radius: 0.625rem;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.8125rem;
  color: #FFFFFF;
  cursor: pointer;
  margin: 0 auto;
  display: block;
}

.signup-button:hover {
  background: #5A85E0;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.error-text {
  color: red;
  font-weight: bold;
}
</style>