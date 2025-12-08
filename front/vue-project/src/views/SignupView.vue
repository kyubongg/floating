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
          <label class="input-label">비밀번호</label>
          <input type="password" class="input-field" v-model="formData.password" required />
        </div>

        <div class="input-wrapper">
          <label class="input-label">비밀번호 확인</label>
          <input type="password" class="input-field" v-model="formData.passwordConfirm" required />
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
              <input type="radio" name="gender" value="남" v-model="formData.gender" required />
              남
            </label>
            <label class="radio-label">
              <input type="radio" name="gender" value="여" v-model="formData.gender" required />
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
import { ref, onMounted } from "vue";

// Pinia 인증 스토어
import { useAuthStore } from "../stores/auth";

// 라우터(사용할 수도 있고 안 사용할 수도 있음)
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

// 회원가입 submit 핸들러
const onSubmit = () => {
  message.value = "";
  auth.error = null;

  // 1) 비밀번호 확인
  if (formData.value.password !== formData.value.passwordConfirm) {
    auth.error = "비밀번호가 일치하지 않습니다.";
    return;
  }

  // 2) 회원가입 요청 (Pinia store → axios → backend)
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
</style>