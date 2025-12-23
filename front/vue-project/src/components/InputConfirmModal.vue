<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="isOpen" class="modal-overlay" @click="handleClose">
        <div class="input-modal" @click.stop>
          <!-- 닫기 버튼 -->
          <button class="close-button" @click="handleClose">X</button>

          <!-- 제목 -->
          <h2 class="modal-title">{{ title }}</h2>

          <!-- 입력 및 확인 -->
          <div class="input-container">
            <input :type="inputType" class="input-context" v-model="inputValue" :placeholder="placeholder"
              @keyup.enter="handleConfirm" />
            <button class="confirm-button" @click="handleConfirm">
              확인
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '확인'
  },
  placeholder: {
    type: String,
    default: ''
  },
  inputType: {
    type: String,
    default: 'text'
  }
});

const emit = defineEmits(['close', 'confirm']);
const inputValue = ref('');

// 모달이 닫힐 때마다 입력값 초기화
watch(() => props.isOpen, (newVal) => {
  if (!newVal) inputValue.value = '';
});

const handleClose = () => {
  emit('close');
};

const handleConfirm = () => {
  if (inputValue.value.trim()) {
    emit('confirm', inputValue.value);
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(125, 125, 125, 0.8);
  /* 회색 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.input-modal {
  position: relative;
  width: 23.4375rem;
  /* 375px */
  height: 7.9375rem;
  /* 127px */
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 1.875rem;
  /* 30px */
  padding: 1.125rem 1.125rem 1.5rem;
  /* 18px 18px 24px */
  box-sizing: border-box;
}

.close-button {
  position: absolute;
  top: 1.0625rem;
  /* 17px */
  right: 1.625rem;
  /* 26px */
  background: none;
  border: none;
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 400;
  font-size: 0.8125rem;
  /* 13px */
  line-height: 1rem;
  /* 16px */
  color: #000000;
  cursor: pointer;
  padding: 0;
  transition: color 0.2s;
}

.close-button:hover {
  color: #769BEF;
}

.modal-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1rem;
  /* 15px */
  line-height: 1.125rem;
  /* 18px */
  color: #000000;
  margin: 0 0 1.5rem 0;
  /* 24px */
}

.input-container {
  display: flex;
  gap: 0.75rem;
  /* 12px */
  align-items: center;
}

.input-context {
  flex: 1;
  height: 2.0625rem;
  /* 33px */
  background: #D9D9D9;
  border: none;
  border-radius: 0.625rem;
  /* 10px */
  padding: 1rem;
  /* 12px */
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 1rem;
  /* 10px */
  line-height: 0.75rem;
  /* 12px */
  color: #000000;
  box-sizing: border-box;
}

.input-context:focus {
  outline: none;
  background: #CACACA;
}

.confirm-button {
  width: 3rem;
  /* 48px */
  height: 2.0625rem;
  /* 33px */
  background: #769BEF;
  border: none;
  border-radius: 0.625rem;
  /* 10px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 0.8125rem;
  /* 13px */
  line-height: 1rem;
  /* 16px */
  color: #FFFFFF;
  cursor: pointer;
  transition: background 0.2s;
}

.confirm-button:hover {
  background: #5A85E0;
}

/* 모달 애니메이션 */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-active .input-modal,
.modal-leave-active .input-modal {
  transition: transform 0.3s ease;
}

.modal-enter-from .input-modal,
.modal-leave-to .input-modal {
  transform: scale(0.9);
}
</style>