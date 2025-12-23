import { ref } from 'vue';

const isOpen = ref(false);
const message = ref('');
const type = ref('info');
const resolvePromise = ref(null);

export function useAlert() {
  const show = (msg, alertType = 'info') => {
    return new Promise((resolve) => {
      message.value = msg;
      type.value = alertType;
      isOpen.value = true;
      resolvePromise.value = resolve;
    });
  };

  const close = () => {
    isOpen.value = false;
    if (resolvePromise.value) {
      resolvePromise.value();
      resolvePromise.value = null;
    }
  };

  return {
    isOpen,
    message,
    type,
    show,
    close,
  };
}