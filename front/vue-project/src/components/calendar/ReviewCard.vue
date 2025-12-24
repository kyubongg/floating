<template>
  <div class="review-section">
    <div class="review-card">
      <div class="card-header">
        <h3>하루 기록 | {{ formattedDate }}</h3>
        <button class="close-btn" @click="$emit('close')">X</button>
      </div>

      <div class="plan-list">
        <template v-if="plans && plans.length > 0">
          <div v-for="plan in plans" :key="plan.planPk" class="plan-item">
            <div class="plan-icon-placeholder" :style="{ backgroundColor: getCategoryColor(plan.completeDate) }"></div>
            <div class="plan-detail-wrapper">
              <span class="plan-detail-text">
                {{ plan.category || '일정' }} | {{ plan.detail }} | {{ plan.time }}분
              </span>
              <div :class="['save-status', { 'success': showSuccessMessage }]">
                {{ savingMessage }}
              </div>
            </div>
          </div>
        </template>
        <p v-else class="no-plan-text">이 날짜에 등록된 계획이 없습니다.</p>
      </div>

      <div class="review-photo-container">
        <div class="input-wrapper">
          <textarea 
            class="review-input" 
            placeholder="오늘의 운동은 어땠나요? 기록을 남겨보세요."
            :value="modelValue"
            @input="handleInput"
            :disabled="!isReviewEditable || isSaving"
          ></textarea>
        </div>
        
        <div class="photo-slider-area" :class="{ 'is-empty': allDisplayImages.length === 0 }">
          <div class="slider-container" :class="{ 'empty-container': allDisplayImages.length === 0 }">
            
            <template v-if="allDisplayImages.length > 0">
              <div class="main-photo">
                <img :src="allDisplayImages[currentIndex].url" class="preview-img" />
                <button class="remove-img-btn" @click.stop="removeImage(currentIndex, allDisplayImages[currentIndex].isNew)">X</button>
              </div>

              <template v-if="allDisplayImages.length > 1">
                <button v-if="currentIndex > 0" class="nav-btn prev" @click="prevSlide">〈</button>
                <button v-if="currentIndex < allDisplayImages.length - 1" class="nav-btn next" @click="nextSlide">〉</button>
              </template>
            </template>

            <div v-else class="photo-placeholder empty" @click="triggerFileInput">
              <span class="plus-icon">+</span>
              <span class="add-text">이미지 추가</span>
            </div>
          </div>

          <div v-if="allDisplayImages.length > 0" class="slider-dots">
            <span v-for="(_, i) in allDisplayImages" :key="i" :class="['dot', { active: i === currentIndex }]"></span>
          </div>
          <button v-if="allDisplayImages.length > 0 && allDisplayImages.length < 5" class="add-more-btn" @click="triggerFileInput">
            + 사진 추가 ({{ allDisplayImages.length }}/5)
          </button>
        </div>

        <input 
          type="file" 
          ref="fileInput" 
          style="display: none" 
          accept="image/*" 
          multiple 
          @change="onFileChange" 
        />
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
  if (showSuccessMessage.value) return "✔ 저장 완료!";
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
    }, 3500);
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
  console.log(isNew);
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
/* 1. 전체 카드 레이아웃 */
.review-section {
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.review-card {
  width: 505px;
  min-height: 483px;
  background: #FFFFFF;
  border: 1px solid #ECECEC;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  border-radius: 55px;
  padding: 40px;
  font-family: 'Noto Sans KR', sans-serif;
  box-sizing: border-box;
}

/* 2. 헤더 영역 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.card-header h3 {
  font-weight: 700;
  font-size: 18px;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #999;
  transition: color 0.2s;
}

.close-btn:hover { color: #333; }

/* 3. 계획 리스트 영역 */
.plan-list {
  margin-bottom: 20px;
  min-height: 40px;
}

.plan-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.plan-icon-placeholder {
  width: 22px;
  height: 20px;
  border-radius: 6px;
  margin-right: 12px;
}

.plan-detail-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.plan-detail-text {
  font-size: 14px;
  color: #333;
}

.save-status {
  font-size: 12px;
  color: #bbb;
  font-weight: 500;
  min-width: 70px;
  text-align: right;
}

.save-status.success { color: #769BEF; }

/* 4. 컨텐츠 하단 (입력창 + 사진) */
.review-photo-container {
  display: flex;
  gap: 20px;
  margin-top: 20px;
  align-items: flex-start;
}

/* 5. 왼쪽 입력 영역 */
.input-wrapper {
  width: 50%;
  display: flex;
  flex-direction: column;
}

.review-input {
  width: 100%;
  height: 350px; /* 고정 높이 */
  padding: 25px;
  background: #F2F2F2;
  border: none;
  border-radius: 30px;
  resize: none;
  font-size: 14px;
  font-family: inherit;
  box-sizing: border-box;
  transition: background 0.2s;
}

.review-input:focus {
  background: #EAEAEA;
  outline: none;
}

/* 6. 오른쪽 사진 영역 (가변 높이 핵심) */
.photo-slider-area {
  width: 50%;
  display: flex;
  flex-direction: column;
  gap: 12px;
  transition: all 0.3s ease;

  align-items: center;
}

.slider-container {
  position: relative;
  width: 100%;
  height: 268px; /* 사진이 있을 때 입력창과 높이 맞춤 */
  background: #F9F9F9;
  border-radius: 30px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 사진 없을 때의 컨테이너 스타일 */
.empty-container {
  height: 45px !important;
  background: #FFFFFF !important;
  border: 1px dashed #DDD;
}

.photo-placeholder.empty {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  cursor: pointer;
  color: #888;
}

.photo-placeholder.empty:hover {
  background: #fdfdfd;
  border-color: #769BEF;
  color: #769BEF;
}

/* 7. 사진 슬라이더 세부 요소 */
.main-photo {
  width: 100%;
  height: 100%;
  position: relative;
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-img-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  background: rgba(0, 0, 0, 0.4);
  color: white;
  border: none;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  cursor: pointer;
  backdrop-filter: blur(4px);
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.8);
  border: none;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
  z-index: 10;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.nav-btn:hover { background: white; }
.prev { left: 10px; }
.next { right: 10px; }

.slider-dots {
  display: flex;
  gap: 6px;
  justify-content: center;
  align-items: center;
  padding: 5px 0; 
  width: 100%;
}

.dot {
  width: 6px;
  height: 6px;
  background: rgba(118, 155, 239, 0.3);
  border-radius: 50%;
  transition: all 0.3s;
}

.dot.active {
  background: #769BEF;
  width: 15px;
  border-radius: 4px;
}

/* 8. 추가 등록 버튼 */
.add-more-btn {
  width: 100%;
  height: 40px;
  background: white;
  border: 1px solid #ECECEC;
  border-radius: 15px;
  font-size: 13px;
  color: #777;
  cursor: pointer;
  transition: all 0.2s;
}

.add-more-btn:hover {
  background: #F9F9F9;
  border-color: #769BEF;
  color: #769BEF;
}

/* 반응형 */
@media (max-width: 600px) {
  .review-card { width: 95%; padding: 25px; border-radius: 35px; }
  .review-photo-container { flex-direction: column; }
  .input-wrapper, .photo-slider-area { width: 100%; }
  .photo-slider-area.is-empty { margin-top: 0; }
}
</style>