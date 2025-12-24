<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="isOpen" class="modal-overlay" @click="handleClose">
        <div class="modal-content" @click.stop>
          <h2 class="modal-title">{{ title }}</h2>
          
          <div class="modal-options" :class="{ single: singleButton }">
            <button 
              v-if="!singleButton"
              class="option-button primary" 
              @click="handlePrimaryClick"
            >
              {{ primaryText }}
            </button>
            
            <button 
              class="option-button" 
              :class="[singleButton ? 'secondary-single' : 'secondary', { 'loading': isAiLoading }]"
              @click="handleSecondaryClick"
              :disabled="isAiLoading" 
            >
              <template v-if="isAiLoading">
                <div class="spinner"></div>
                <span>분석 중...</span>
              </template>
              <template v-else>
                {{ secondaryText }}
              </template>
            </button>
          </div>
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
  title: {
    type: String,
    required: true
  },
  primaryText: {
    type: String,
    required: true
  },
  secondaryText: {
    type: String,
    required: true
  },
  singleButton: {
    type: Boolean,
    default: false
  },
  isAiLoading: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['close', 'primary-click', 'secondary-click']);

const handleClose = () => {
  emit('close');
};

const handlePrimaryClick = () => {
  emit('primary-click');
};

const handleSecondaryClick = () => {
  emit('secondary-click');
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
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  white-space: pre-line;
}

.modal-title {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1.875rem;  /* 30px */
  line-height: 2.8125rem;  /* 45px */
  text-align: center;
  color: #FFFFFF;
  margin: 0;
  max-width: 40rem;
  white-space: pre-line;
}

.modal-options {
  display: flex;
  gap: 2.5rem;  /* 40px */
}

.modal-options.single {
  justify-content: center;
}

.option-button.secondary-single {
  background: #769BEF;
  color: #FFFFFF;
}

.option-button {
  width: 20.6875rem;  /* 331px */
  height: 19.3125rem;  /* 309px */
  border: none;
  border-radius: 3.75rem;  /* 60px */
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 700;
  font-size: 1.875rem;  /* 30px */
  line-height: 2.8125rem;  /* 45px */
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  padding: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  white-space: pre-line;
}

.option-button:hover {
  transform: translateY(-0.5rem);
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.3);
}

.option-button.primary {
  background: #769BEF;
  color: #FFFFFF;
}

.option-button.secondary {
  background: #FFFFFF;
  color: #769BEF;
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

.modal-enter-active .modal-content,
.modal-leave-active .modal-content {
  transition: transform 0.3s ease;
}

.modal-enter-from .modal-content,
.modal-leave-to .modal-content {
  transform: scale(0.9);
}

/* 반응형 */
@media (max-width: 768px) {
  .modal-options {
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .option-button {
    width: 18rem;
    height: 16rem;
    font-size: 1.5rem;
  }
}

.option-button:disabled {
  cursor: not-allowed;
  opacity: 0.8;
  transform: none !important; /* 호버 애니메이션 중단 */
}

.spinner {
  width: 2rem;
  height: 2rem;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid #769BEF;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>