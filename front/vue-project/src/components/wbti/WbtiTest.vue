<template>
  <div class="test-wrapper">
    <div class="progress-bar">
      <div class="progress-fill" :style="{ width: `${((currentIndex + 1) / totalQuestions) * 100}%` }"></div>
    </div>

    <div class="test-container">
      <div class="text-area">
        <span class="question-number">Q{{ currentIndex + 1 }}.</span>
        <p>{{ currentQuestion.text }}</p>
      </div>

      <div v-if="currentQuestion.isCondition" class="condition-area">
        <div v-if="currentQuestion.inputType === 'checkbox'" class="options-grid">
          <label v-for="opt in currentQuestion.options" :key="opt" class="option-item">
            <input type="checkbox" :value="opt" v-model="selectedConditionValue" />
            <span class="option-text">{{ opt }}</span>
          </label>
        </div>
        
        <div v-else-if="currentQuestion.inputType === 'radio'" class="options-list">
          <label v-for="opt in currentQuestion.options" :key="opt" class="option-item radio">
            <input type="radio" :value="opt" v-model="selectedConditionValue" />
            <span class="option-text">{{ opt }}</span>
          </label>
        </div>

        <div v-else-if="currentQuestion.inputType === 'text'" class="text-input-wrapper">
          <textarea v-model="selectedConditionValue" placeholder="여기에 내용을 입력해주세요..."></textarea>
        </div>
      </div>

      <div v-else class="choice-area">
        <div class="choice-area__label">{{ currentQuestion.leftLabel || '그렇다' }}</div>
        <div class="circles-container">
          <button v-for="n in 7" :key="n"
            class="circle-button"
            :class="{
              [`size-${n}`]: true,
              'is-blue-style': n <= 3, 
              'is-gray-style': n === 4,
              'is-red-style': n >= 5,   
              'is-selected': selectedWbtiChoice === n
            }"
            @click="selectWbtiChoice(n)">
          </button>
        </div>
        <div class="choice-area__label">{{ currentQuestion.rightLabel || '그렇지 않다' }}</div>
      </div>
    </div>

    <div class="form-area">
      <button class="nav-button prev-button" @click="goToPrev" :disabled="currentIndex === 0">이전</button>
      
      <button 
          class="nav-button next-button" 
          @click="handleNext" 
          :disabled="!isAnswered"
      >
          <span>{{ currentIndex < totalQuestions - 1 ? '다음' : '결과 보기' }}</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { useWbtiStore } from '@/stores/wbti';
import { WBTI_QUESTIONS } from '@/constants/wbtiData';
import { computed, ref, defineEmits } from 'vue';

const emit = defineEmits(['complete']);
const wbtiStore = useWbtiStore();

// --- 1. 질문 데이터 구성 ---
const questions = [
  { id: 'Q0', text: '과거 운동 경험을 선택해주세요 (복수 가능)', isCondition: true, inputType: 'checkbox', key: 'experiences', options: ['헬스/웨이트', '요가/필라테스', '러닝/걷기', '수영', '팀 스포츠', '크로스핏', '경험 없음'] },
  { id: 'Q0-1', text: '그만둔 주된 이유는 무엇인가요?', isCondition: true, inputType: 'radio', key: 'quitReason', options: ['시간 부족', '지루함', '체력 한계', '부상/통증', '비용 부담', '혼자 하기 힘듦', '해당 없음'] },
  { id: 'Q0-2', text: '현재 몸 상태를 알려주세요 (복수 가능)', isCondition: true, inputType: 'checkbox', key: 'bodyConditions', options: ['불편함 없음', '무릎 통증', '허리 통증', '목/어깨 통증', '손목/발목 약함', '관절 질환'] },
  { id: 'Q0-3', text: '하루 중 운동 가능한 시간은?', isCondition: true, inputType: 'radio', key: 'availableTime', options: ['15분 이하', '20-30분', '40분-1시간', '1시간 이상', '상관없음'] },
  { id: 'Q-ECONOMY', text: '현재 운동을 위한 경제적 여유는 어떠신가요? (주관식)', isCondition: true, inputType: 'text', key: 'economy' },
  ...WBTI_QUESTIONS 
];

const totalQuestions = questions.length;
const currentIndex = ref(0);
const currentQuestion = computed(() => questions[currentIndex.value]);

// --- 2. 답변 데이터 바인딩 ---
const selectedConditionValue = computed({
  get: () => wbtiStore.userCondition[currentQuestion.value.key],
  set: (val) => { wbtiStore.userCondition[currentQuestion.value.key] = val; }
});

const wbtiAnswers = ref(Array(totalQuestions).fill(null));
const selectedWbtiChoice = computed({
  get: () => wbtiAnswers.value[currentIndex.value],
  set: (val) => { wbtiAnswers.value[currentIndex.value] = val; }
});

const isAnswered = computed(() => {
  if (currentQuestion.value.isCondition) {
    const val = selectedConditionValue.value;
    return Array.isArray(val) ? val.length > 0 : !!val;
  }
  return selectedWbtiChoice.value !== null;
});

const selectWbtiChoice = (n) => { selectedWbtiChoice.value = n; };

// --- 3. 내비게이션 로직 ---
const handleNext = () => {
  if (currentIndex.value < totalQuestions - 1) {
    currentIndex.value++;
  } else {
    // 마지막 질문 완료 시 점수 계산 후 부모에게 완료 알림
    calculateFinalScores();
    emit('complete');
  }
};

const goToPrev = () => { if (currentIndex.value > 0) currentIndex.value--; };

const calculateFinalScores = () => {
  wbtiStore.resetScores();
  questions.forEach((q, index) => {
    if (!q.isCondition && wbtiAnswers.value[index] !== null) {
      wbtiStore.accumulateScore(q.type, 8 - wbtiAnswers.value[index]);
    }
  });
};
</script>

<style scoped>
/* 제공해주신 스타일 그대로 적용 */
.test-wrapper { width: 100%; display: flex; flex-direction: column; align-items: center; }
.test-container { width: 100%; max-width: 600px; text-align: center; }
.text-area { font-family: 'Noto Sans KR', sans-serif; font-weight: 700; font-size: 24px; color: #FFFFFF; margin-bottom: 80px; }
.choice-area { display: flex; align-items: center; justify-content: center; width: 100%; max-width: 900px; margin: 0 auto; }
.choice-area__label { font-family: 'Noto Sans KR', sans-serif; font-weight: 400; font-size: 16px; color: #FFFFFF; white-space: nowrap; width: 80px; text-align: center; }
.circles-container { display: flex; flex: 1; justify-content: space-around; align-items: center; margin: 0 20px; }
.circle-button { border-radius: 50%; cursor: pointer; background-color: #FFFFFF; flex-shrink: 0; border: 2px solid transparent; transition: all 0.2s; padding: 0; }
.circle-button.size-1, .circle-button.size-7 { width: 60px; height: 60px; }
.circle-button.size-2, .circle-button.size-6 { width: 45px; height: 45px; }
.circle-button.size-3, .circle-button.size-5 { width: 35px; height: 35px; }
.circle-button.size-4 { width: 25px; height: 25px; }
.circle-button.is-blue-style { border-color: #769BEF; }
.circle-button.is-red-style { border-color: #FF6B6B; }
.circle-button.is-gray-style { border-color: #999999; }
.circle-button.is-blue-style.is-selected { background-color: #769BEF !important; border: none !important; transform: scale(1.1); }
.circle-button.is-gray-style.is-selected { background-color: #999999 !important; border: none !important; transform: scale(1.1); }
.circle-button.is-red-style.is-selected { background-color: #FF6B6B !important; border: none !important; transform: scale(1.1); }
.form-area { display: flex; justify-content: space-between; width: 90%; max-width: 800px; margin-top: 60px; }
.nav-button { padding: 10px 30px; border-radius: 10px; font-size: 16px; font-weight: 700; cursor: pointer; transition: background-color 0.3s; color: #FFFFFF; border: none; }
.prev-button { background-color: #999999; }
.next-button { background-color: #769BEF; }
.nav-button:disabled { opacity: 0.5; cursor: not-allowed; }
.question-number { display: block; font-size: 18px; color: #769BEF; margin-bottom: 10px; }
.condition-area { margin-top: 40px; }
.options-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }
.option-item { background: #f1f1f1; padding: 15px; border-radius: 12px; cursor: pointer; transition: 0.3s; display: flex; align-items: center; gap: 10px; color: #333; }
.option-item:has(input:checked) { background: #769BEF; color: white; }
.text-input-wrapper textarea { width: 100%; height: 150px; border-radius: 15px; padding: 20px; border: none; font-size: 16px; resize: none; }
.progress-bar { position: fixed; top: 0; left: 0; width: 100%; height: 6px; background: #eee; }
.progress-fill { height: 100%; background: #769BEF; transition: width 0.3s; }
</style>