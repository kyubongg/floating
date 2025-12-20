<template>
  <div class="main-page">
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
          @click="goToNext" 
          :disabled="wbtiStore.isSubmitting || !isAnswered"
      >
          <span v-if="wbtiStore.isSubmitting">분석 중...</span>
          <span v-else>{{ currentIndex < totalQuestions - 1 ? '다음' : '결과 보기' }}</span>
      </button>
    </div>
  </div>
</template>

<script setup>
    import { useWbtiStore } from '@/stores/wbti';
    import { WBTI_QUESTIONS } from '@/constants/wbtiData';
    import { USER_CONDITION_QUESTIONS } from '@/constants/wbtiData'; // 상수로 분리 권장
    import { computed, ref } from 'vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();
    const wbtiStore = useWbtiStore();

    // --- 1. 질문 데이터 통합 ---
    const questions = [
    // 사전 질문 (isCondition: true로 구분)
    { id: 'Q0', text: '과거 운동 경험을 선택해주세요 (복수 가능)', isCondition: true, inputType: 'checkbox', key: 'experiences', options: ['헬스/웨이트', '요가/필라테스', '러닝/걷기', '수영', '팀 스포츠', '크로스핏', '경험 없음'] },
    { id: 'Q0-1', text: '그만둔 주된 이유는 무엇인가요?', isCondition: true, inputType: 'radio', key: 'quitReason', options: ['시간 부족', '지루함', '체력 한계', '부상/통증', '비용 부담', '혼자 하기 힘듦', '해당 없음'] },
    { id: 'Q0-2', text: '현재 몸 상태를 알려주세요 (복수 가능)', isCondition: true, inputType: 'checkbox', key: 'bodyConditions', options: ['불편함 없음', '무릎 통증', '허리 통증', '목/어깨 통증', '손목/발목 약함', '관절 질환'] },
    { id: 'Q0-3', text: '하루 중 운동 가능한 시간은?', isCondition: true, inputType: 'radio', key: 'availableTime', options: ['15분 이하', '20-30분', '40분-1시간', '1시간 이상', '상관없음'] },
    { id: 'Q-ECONOMY', text: '현재 운동을 위한 경제적 여유는 어떠신가요? (주관식)', isCondition: true, inputType: 'text', key: 'economy' },
    // 성향 질문 (기존 데이터)
    ...WBTI_QUESTIONS 
    ];

    const totalQuestions = questions.length;
    const currentIndex = ref(0);
    const currentQuestion = computed(() => questions[currentIndex.value]);

    // --- 2. 답변 데이터 처리 ---
    // 사전 질문은 store의 userCondition에 직접 바인딩하거나 임시 ref에 저장
    const selectedConditionValue = computed({
    get: () => {
        const key = currentQuestion.value.key;
        return wbtiStore.userCondition[key];
    },
    set: (val) => {
        const key = currentQuestion.value.key;
        wbtiStore.userCondition[key] = val;
    }
    });

    // 성향 질문 (WBTI) 답변 저장소 (인덱스 매핑)
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

    // --- 3. 네비게이션 로직 ---
    const goToNext = async () => {
    if (currentIndex.value < totalQuestions - 1) {
        currentIndex.value++;
    } else {
        // 모든 설문 완료
        try {
        console.log("1단계")
        // 🎯 WBTI 점수 최종 누적 (마지막에 한꺼번에 계산하거나 이동 시 처리)
        calculateFinalScores();
        console.log("2단계")
        const result = await wbtiStore.submitResults();
        console.log("3단계")
        router.replace({ name: 'wbtiResult' });
        } catch (error) {
            alert('결과 전송 중 오류가 발생했습니다.');
            console.log(error)
        }
    }
    };

    const goToPrev = () => { if (currentIndex.value > 0) currentIndex.value--; };

    // 성향 질문들에 대해서만 스토어 점수 합산 로직 실행
    const calculateFinalScores = () => {
    wbtiStore.resetScores(); // 초기화 후 재계산
    questions.forEach((q, index) => {
        if (!q.isCondition && wbtiAnswers.value[index] !== null) {
        wbtiStore.accumulateScore(q.type, 8 - wbtiAnswers.value[index]);
        }
    });
    };
</script>

<style scoped>
/* 전체 페이지 스타일 */
.main-page {
    background-color: #6C6C6C;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 50px 20px;
}

/* --- 상단 질문 텍스트 영역 --- */
.text-area {
    font-family: 'Noto Sans KR', sans-serif;
    font-weight: 700;
    font-size: 24px;
    color: #FFFFFF;
    margin-bottom: 80px;
}

/* --- 선택 버튼 영역 --- */
.choice-area {
    display: flex;
    align-items: center;
    justify-content: center; /* 중앙 정렬 */
    width: 100%;             /* 전체 너비 사용 */
    max-width: 900px;        /* 레이블까지 포함해 넉넉하게 잡음 */
    margin: 0 auto;
}

.choice-area__label {
    font-family: 'Noto Sans KR', sans-serif;
    font-weight: 400;
    font-size: 16px;
    color: #FFFFFF;
    white-space: nowrap;
    width: 80px;             /* 레이블 너비 고정으로 균형 유지 */
    text-align: center;
}

.circles-container {
    display: flex;
    flex: 1;                 /* 남은 공간 모두 차지 */
    justify-content: space-around; /* 원들 사이 간격을 균등하게 배분 */
    align-items: center;
    margin: 0 20px;
    /* gap: 50px; <- 이 줄을 삭제하거나 주석 처리하세요. */
}

/* --- 원형 버튼 공통 스타일 --- */
.circle-button {
    border-radius: 50%;
    cursor: pointer;
    background-color: #FFFFFF; 
    flex-shrink: 0;          /* 공간이 부족해도 찌그러지지 않게 설정 */
    border: 2px solid transparent; 
    transition: all 0.2s;
    padding: 0;              /* 버튼 내부 여백 제거 */
}

/* 원 크기 미세 조정 (800px 컨테이너에서 70px은 다소 클 수 있음) */
.circle-button.size-1, .circle-button.size-7 { width: 60px; height: 60px; }
.circle-button.size-2, .circle-button.size-6 { width: 45px; height: 45px; }
.circle-button.size-3, .circle-button.size-5 { width: 35px; height: 35px; }
.circle-button.size-4 { width: 25px; height: 25px; }


/* ----------------------------------- */
/* 2. 성향별 기본 스타일 (테두리 및 중립 배경) */
/* ----------------------------------- */

.circle-button.is-blue-style {
    border-color: #769BEF; 
}
.circle-button.is-red-style {
    border-color: #FF6B6B; 
}
/* 4번 (중립) 버튼은 선택되지 않아도 회색 배경을 가집니다. */
.circle-button.is-gray-style {
    border-color: #999999;
}


/* ----------------------------------- */
/* 3. 선택 상태 (is-selected) */
/* ----------------------------------- */

.circle-button.is-blue-style.is-selected {
    background-color: #769BEF !important;
    border: none !important;
    transform: scale(1.1);
}

.circle-button.is-gray-style.is-selected {
    background-color: #999999 !important; 
    border: none !important;
    transform: scale(1.1);
}


.circle-button.is-red-style.is-selected {
    background-color: #FF6B6B !important;
    border: none !important;
    transform: scale(1.1);
}


/* --- 이전/다음 버튼 영역 스타일 --- */
.form-area {
    display: flex;
    justify-content: space-between;
    width: 90%;
    max-width: 800px;
    margin-top: 60px;
}

.nav-button {
    padding: 10px 30px;
    border-radius: 10px;
    font-size: 16px;
    font-weight: 700;
    cursor: pointer;
    transition: background-color 0.3s;
    color: #FFFFFF;
    border: none;
}

.prev-button {
    background-color: #999999; 
}

.next-button {
    background-color: #769BEF; 
}

.nav-button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

/* 기존 스타일 유지 + 사전 질문용 스타일 추가 */
.test-container { width: 100%; max-width: 600px; text-align: center; }
.question-number { display: block; font-size: 18px; color: #769BEF; margin-bottom: 10px; }

/* 사전 질문 옵션 스타일 */
.condition-area { margin-top: 40px; }

.options-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }
.option-item {
  background: #f1f1f1; padding: 15px; border-radius: 12px;
  cursor: pointer; transition: 0.3s; display: flex; align-items: center; gap: 10px;
}
.option-item:has(input:checked) { background: #769BEF; color: white; }
.text-input-wrapper textarea {
  width: 100%; height: 150px; border-radius: 15px; padding: 20px;
  border: none; font-size: 16px; resize: none;
}

/* 진행 바 */
.progress-bar { position: fixed; top: 0; left: 0; width: 100%; height: 6px; background: #eee; }
.progress-fill { height: 100%; background: #769BEF; transition: width 0.3s; }
</style>