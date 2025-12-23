<template>
  <Teleport to="body">
    <Transition name="alert">
      <div v-if="isOpen" class="alert-overlay" @click="handleClose">
        <div class="alert-box" @click.stop>
          <!-- 귀여운 아이콘 -->
          <div class="alert-icon" :class="type">
            <span v-if="type === 'success'">✓</span>
            <span v-else-if="type === 'error'">✕</span>
            <span v-else-if="type === 'warning'">!</span>
            <span v-else>ℹ</span>
          </div>
          
          <!-- 메시지 -->
          <p class="alert-message">{{ message }}</p>
          
          <!-- 확인 버튼 -->
          <button class="alert-button" @click="handleConfirm">
            확인
          </button>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  message: {
    type: String,
    default: ''
  },
  type: {
    type: String,
    default: 'info', // 'success', 'error', 'warning', 'info'
    validator: (value) => ['success', 'error', 'warning', 'info'].includes(value)
  }
});

const emit = defineEmits(['close', 'confirm']);

const handleClose = () => {
  emit('close');
};

const handleConfirm = () => {
  emit('confirm');
  emit('close');
};
</script>

<style scoped>
.alert-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(125, 125, 125, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.alert-box {
  background: #FFFFFF;
  border: 2px solid #ECECEC;
  box-shadow: 0px 8px 24px rgba(0, 0, 0, 0.15);
  border-radius: 2rem;  /* 32px */
  padding: 2rem 2.5rem;
  max-width: 21.875rem;  /* 350px */
  text-align: center;
  animation: bounce 0.5s ease;
}

@keyframes bounce {
  0% {
    transform: scale(0.3);
    opacity: 0;
  }
  50% {
    transform: scale(1.05);
  }
  70% {
    transform: scale(0.9);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 귀여운 아이콘 */
.alert-icon {
  width: 4rem;  /* 64px */
  height: 4rem;
  border-radius: 50%;
  margin: 0 auto 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;  /* 32px */
  font-weight: bold;
  animation: pop 0.6s ease 0.2s both;
}

@keyframes pop {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.alert-icon.success {
  background: linear-gradient(135deg, #A8E6CF 0%, #81C784 100%);
  color: #FFFFFF;
}

.alert-icon.error {
  background: linear-gradient(135deg, #FFB3BA 0%, #EF5350 100%);
  color: #FFFFFF;
}

.alert-icon.warning {
  background: linear-gradient(135deg, #FFE082 0%, #FFC107 100%);
  color: #FFFFFF;
}

.alert-icon.info {
  background: linear-gradient(135deg, #A8D5E2 0%, #769BEF 100%);
  color: #FFFFFF;
}

/* 메시지 */
.alert-message {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
  font-size: 1rem;  /* 16px */
  line-height: 1.625rem;  /* 26px */
  color: #333333;
  margin: 0 0 1.5rem 0;
  white-space: pre-line;
  word-break: keep-all;
}

/* 확인 버튼 */
.alert-button {
  width: 100%;
  height: 2.75rem;  /* 44px */
  background: #769BEF;
  border: none;
  border-radius: 1.375rem;  /* 22px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.9375rem;  /* 15px */
  color: #FFFFFF;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0px 4px 12px rgba(118, 155, 239, 0.3);
}

.alert-button:hover {
  transform: translateY(-2px);
  box-shadow: 0px 6px 16px rgba(118, 155, 239, 0.4);
}

.alert-button:active {
  transform: translateY(0);
}

/* 애니메이션 */
.alert-enter-active {
  transition: opacity 0.3s ease;
}

.alert-leave-active {
  transition: opacity 0.2s ease;
}

.alert-enter-from,
.alert-leave-to {
  opacity: 0;
}
</style>