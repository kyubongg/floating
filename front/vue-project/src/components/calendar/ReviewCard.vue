<template>
  <div class="review-section">
    <div class="review-card">
      <div class="card-header">
        <h3>하루 기록 | {{ formattedDate }}</h3>
        <button class="close-btn" @click="$emit('close')">X</button>
      </div>

      <div class="card-content">
        <div class="plan-list">
          <template v-if="plans && plans.length > 0">
            <div v-for="plan in plans" :key="plan.plan_pk" class="plan-item">
              <div class="plan-icon-wrapper">
                <div class="plan-icon-placeholder" :style="{ backgroundColor: getCategoryColor(plan.completeDate) }"></div>
              </div>
              <p><span class="plan-detail-text">{{ plan.category || '일정' }} | {{ plan.detail }} | {{ plan.time }}분</span></p>
            </div>
          </template>
          <p v-else class="no-plan-text">이 날짜에 등록된 계획이 없습니다.</p>
        </div>

        <div class="review-photo-container">
          <div class="input-wrapper">
            <textarea 
              class="review-input" 
              :value="modelValue"
              @input="handleInput"
              :disabled="!isReviewEditable || isSaving"
              :placeholder="savingMessage"
            ></textarea>
            <div :class="['save-status', { 'success': showSuccessMessage }]">
              {{ savingMessage }}
            </div>
          </div>
          
          
          <div class="photo-slider-area">
            <div class="slider-container">
              
              <template v-if="allDisplayImages.length > 0">
                <div class="main-photo">
                  <img :src="allDisplayImages[currentIndex].url" class="preview-img" />
                  <button class="remove-img-btn" @click.stop="removeImage(currentIndex, allDisplayImages[currentIndex].isNew)">X</button>
                </div>

                <button v-if="currentIndex > 0" class="nav-btn prev" @click="prevSlide">〈</button>
                <button v-if="currentIndex < allDisplayImages.length - 1" class="nav-btn next" @click="nextSlide">〉</button>
                
                <div class="slider-dots">
                  <span v-for="(_, i) in allDisplayImages" :key="i" :class="['dot', { active: i === currentIndex }]"></span>
                </div>
              </template>

              <div v-else class="photo-placeholder empty" @click="triggerFileInput">
                <span class="plus-icon">+</span>
                <span class="add-text">이미지 추가</span>
              </div>
            </div>

            <button v-if="allDisplayImages.length > 0 && allDisplayImages.length < 5" class="add-more-btn" @click="triggerFileInput">
              + 추가 등록 ({{ allDisplayImages.length }}/5)
            </button>
          </div>

          <input type="file" ref="fileInput" style="display: none" accept="image/*" multiple @change="onFileChange" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue';

const props = defineProps({
  modelValue: String,
  formattedDate: String,
  plans: Array,
  getCategoryColor: Function,
  initialImages: Array,
  isSaving: Boolean,
});


const emit = defineEmits(['update:modelValue', 'close', 'save', 'image-upload', 'delete-existing-img']);

const fileInput = ref(null);
const newFiles = ref([]);
const newPreviews = ref([]);
const currentIndex = ref(0); // 현재 보고 있는 이미지 번호
const showSuccessMessage = ref(false);

const allDisplayImages = computed(() => {
  const SERVER_URL = 'http://localhost:8080';
  const existing = (props.initialImages || []).map(img => ({
    url: `${SERVER_URL}${img.imgPath}`,
    isNew: false,
    pk: img.imgPk
  }));
  const news = newPreviews.value.map(url => ({ url, isNew: true }));
  return [...existing, ...news];
});

const savingMessage = computed(() => {
  if (props.isSaving) return "🔄 저장 중...";
  if (showSuccessMessage.value) return "✅ 저장 완료!";
  return ""; // 평소에는 아무것도 띄우지 않음
});

// 🎯 슬라이더 제어 로직
const prevSlide = () => { if (currentIndex.value > 0) currentIndex.value--; };
const nextSlide = () => { if (currentIndex.value < allDisplayImages.value.length - 1) currentIndex.value++; };

// 부모로부터 새로운 리뷰 데이터를 받았을때
watch(() => props.initialImages, () => {
  // console.log(props.initialImages)
  currentIndex.value = 0;
  newFiles.value = [];
  newPreviews.value = [];
}, { deep: true, immediate: true });

// 이미지가 삭제되었을 때 인덱스 보정
watch(allDisplayImages, (newVal) => {
  if (currentIndex.value >= newVal.length && newVal.length > 0) {
    currentIndex.value = newVal.length - 1;
  }
});

// 리뷰가 변경되었을때
watch(() => props.isSaving, (newVal, oldVal) => {
  if (newVal === false && oldVal === true) {
    showSuccessMessage.value = true;

    // 2초 후에 "저장 완료!" 메시지를 숨기고 기본 문구로 복구
    setTimeout(() => {
      showSuccessMessage.value = false;
    }, 2000);
  }
});

const triggerFileInput = () => { if (isReviewEditable.value) fileInput.value.click(); };

const onFileChange = (e) => {
  const files = Array.from(e.target.files);
  files.forEach(file => {
    newFiles.value.push(file);
    newPreviews.value.push(URL.createObjectURL(file));
  });
  emit('image-upload', newFiles.value);
  
  debouncedSave(props.modelValue);
  // 새 이미지를 추가하면 마지막으로 이동
  currentIndex.value = allDisplayImages.value.length - 1;
};

const removeImage = (index, isNew) => {
  if (isNew) {
    const existingCount = props.initialImages?.length || 0;
    const newIdx = index - existingCount;
    newFiles.value.splice(newIdx, 1);
    newPreviews.value.splice(newIdx, 1);
    emit('image-upload', newFiles.value);
  } else {
    emit('delete-existing-img', props.initialImages[index].imgPk);
  }

  debouncedSave(props.modelValue);
};

// --- 기존 로직 ---
const debounce = (fn, delay) => {
  let timeoutId;
  return function(...args) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => fn.apply(this, args), delay);
  };
};
const debouncedSave = debounce((content) => { emit('save', content); }, 1500);
const handleInput = (e) => {
  const value = e.target.value;
  emit('update:modelValue', value);
  debouncedSave(value);
};
const isReviewEditable = computed(() => props.plans && props.plans.some(plan => !!plan.completeDate));
</script>

<style scoped>
.review-card {
  max-width: 505px; min-height: 483px; background: #FFFFFF;
  border: 1px solid #ECECEC; box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 55px; padding: 30px; 
  font-family: 'Noto Sans KR', sans-serif !important;
}

.review-photo-container { display: flex; justify-content: space-between; gap: 15px; margin-top: 15px; }

.input-wrapper {
  width: 50%;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.review-input {
  width: 100%; /* 부모 wrapper에 맞춤 */
  height: 330px; /* status 영역만큼 살짝 조절 */
  width: 100%; height: 350px; padding: 25px; background: #D9D9D9; 
  border: none; border-radius: 40px; resize: none; font-size: 14px;  
  font-family: 'Noto Sans KR', sans-serif;
}

.save-status {
  height: 20px;
  font-size: 12px;
  color: #888;
  padding-left: 10px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.save-status.success {
  color: #4CAF50; /* 저장 완료 시 초록색으로 강조 */
}

/* 슬라이더 스타일 */
.photo-slider-area {
  width: 50%; display: flex; flex-direction: column; gap: 10px;
}

.slider-container {
  position: relative; width: 100%; height: 310px;
  background: #f9f9f9; border-radius: 20px; overflow: hidden;
}

.main-photo { width: 100%; height: 100%; position: relative; }

.preview-img { width: 100%; height: 100%; object-fit: cover; border-radius: 20px; }

/* 🎯 화살표 버튼 */
.nav-btn {
  position: absolute; top: 50%; transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.7); border: none;
  width: 30px; height: 30px; border-radius: 50%; cursor: pointer;
  font-weight: bold; font-size: 18px; display: flex; align-items: center; justify-content: center;
  transition: background 0.2s; z-index: 10;
}

.nav-btn:hover { background: rgba(255, 255, 255, 0.9); }
.prev { left: 10px; }
.next { right: 10px; }

/* 🎯 도트 인덱스 */
.slider-dots {
  position: absolute; bottom: 15px; left: 50%; transform: translateX(-50%);
  display: flex; gap: 6px;
}
.dot {
  width: 8px; height: 8px; background: rgba(0, 0, 0, 0.2); border-radius: 50%;
}
.dot.active { background: #769BEF; width: 10px; border-radius: 5px; }

.add-more-btn {
  width: 100%; height: 35px; background: #fff; border: 1px dashed #ccc;
  border-radius: 10px; font-size: 12px; color: #666; cursor: pointer;
}

.photo-placeholder.empty {
  width: 100%; height: 100%; display: flex; flex-direction: column;
  align-items: center; justify-content: center; cursor: pointer;
}

.plus-icon {
  font-size: 18px;
}
.add-text {
  font-size: 14px;
}
.remove-img-btn {
  position: absolute; top: 10px; right: 10px; background: rgba(0, 0, 0, 0.5);
  color: white; border: none; border-radius: 50%; width: 25px; height: 25px; cursor: pointer;
}

/* 헤더/리스트 스타일은 이전과 동일 */
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.card-header h3 { font-family: 'Noto Sans KR', sans-serif; font-weight: 700; font-size: 18px; color: #000000; margin: 0; }
.close-btn { background: none; border: none; font-size: 16px; cursor: pointer; color: #000000; }
.plan-list { margin-bottom: 20px; }
.plan-item { display: flex; align-items: center; margin-bottom: 10px; }
.plan-icon-placeholder { width: 22.19px; height: 20.09px; border-radius: 10px; margin-right: 8px; }
.plan-detail-text { font-family: 'Noto Sans KR', sans-serif; font-size: 14px; color: #000000; }
.no-plan-text { color: #aaa; font-size: 12px; padding: 10px 0; }
</style>