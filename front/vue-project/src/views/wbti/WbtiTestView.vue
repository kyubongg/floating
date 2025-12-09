<template>
    <div class="main-page">
        <div class="text-area">
            <span>{{ testText }}</span>
        </div>
        <div class="choice-area">
            <div class="choice-area__label">그렇다</div>
            <div class="circles-container">
                <button v-for="n in 7" :key="n"
                    class="circle-button"
                    :class="{
                        // 1. 크기 분류
                        [`size-${n}`]: true,
                        // 2. 성향 분류 (컬러 및 기본 테두리)
                        'is-blue-style': n <= 3, 
                        'is-gray-style': n === 4,
                        'is-red-style': n >= 5,   
                        // 3. 선택 상태 (is-selected)
                        'is-selected': selectedChoice === n
                    }"
                    @click="selectChoice(n)">
                </button>
            </div>
            <div class="choice-area__label">그렇지 않다</div>
        </div>
        <div class="form-area">
            <button
                class="nav-button prev-button"
                @click="goToPrev"
                :disabled="currentIndex === 0">이전
            </button>
            <button
                class="nav-button next-button"
                @click="goToNext"
                :disabled="selectedChoice === null">
                {{ currentIndex < questions.length - 1 ? '다음' : '결과 보기' }}
            </button>
        </div>
    </div>
</template>

<script setup>
    import { useWbtiStore } from '@/stores/wbti';
import { computed, ref } from 'vue';
    import { useRouter } from 'vue-router';

    const questions = [
        { text: '당신은 야외에서 운동하는걸 좋아하나요?', type: 'social_type'},
        { text: '운동할 때 음악을 듣는 것을 선호하나요?', type: 'motivation_type'},
        { text: '운동 후 스트레칭을 꼭 하나요?', type: 'execution_type'},
        { text: '운동 계획을 세우는 것을 즐기나요?', type: 'activity_type'},
        { text: '운동 중간에 휴식을 자주 취하나요?', type: 'social_type'}
    ];

    const router = useRouter();
    const wbtiStore = useWbtiStore();

    const currentIndex = ref(0);
    const userAnswers = ref(Array(questions.length).fill(null));

    const resultScores = ref({
        social_type: 0,
        motivation_type: 0,
        execution_type: 0,
        activity_type: 0
    });

    const testText = computed(() => {
        return questions[currentIndex.value].text;
    });

    // 💡 Computed Ref: 현재 질문의 답변을 읽고 쓰는 역할을 담당합니다.
    const selectedChoice = computed({
        get() {
            return userAnswers.value[currentIndex.value];
        },
        set(val) {
            userAnswers.value[currentIndex.value] = val;
        }
    });

    const selectChoice = (choice) => {
        // 선택지 클릭 시 userAnswers가 업데이트되고, is-selected 클래스가 적용됩니다.
        selectedChoice.value = choice;
    }

    const goToNext = async () => {
        if(selectedChoice.value === null) { // selectedChoice는 computed ref이므로 .value로 접근
            alert('선택지를 선택해주세요.');
            return;
        }

        const currentQuestion = questions[currentIndex.value];
        const questionType = currentQuestion.type;

        // 임시 저장소에 점수 누적
        wbtiStore.accumulateScore(questionType, 8 - selectedChoice.value);

        if(currentIndex.value < questions.length - 1){
            currentIndex.value++;
        } else{
            // 모든 질문이 끝났을 때: 최종 점수를 서버로 전송
            try{
                alert('검사가 완료되었습니다. 결과 페이지로 이동합니다.');

                const result = await wbtiStore.submitAnswers();
                console.log('서버 응답:', result);

                // 결과 페이지로 이동
                router.replace({ name: 'wbtiResult' });
            } catch (error) {
                console.error('결과 전송 중 오류 발생:', error);
                alert('결과 전송 중 오류가 발생했습니다. 다시 시도해주세요.');
            } 
        }
    }

    const goToPrev = () => {
        if(currentIndex.value > 0){
            // 이전 질문의 정보와 답변을 가져옴
            const previousIndex = currentIndex.value - 1;
            const previousQuestion = questions[previousIndex];
            const previousAnswer = userAnswers.value[previousIndex];
            
            // 취소할 점수 계산 (누적 점수의 반대 부호)
            const scoreToSubtract = previousAnswer;
            
            // 점수를 되돌림
            wbtiStore.accumulateScore(previousQuestion.type, -scoreToSubtract);
            
            // 질문 인덱스 이동
            currentIndex.value--;
        }
    }
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
    width: 90%;
    max-width: 800px;
}

.choice-area__label {
    font-family: 'Noto Sans KR', sans-serif;
    font-weight: 400;
    font-size: 16px;
    color: #FFFFFF;
    white-space: nowrap;
    padding: 0 15px;
}

.circles-container {
    display: flex;
    flex-grow: 1;
    justify-content: space-between;
    align-items: center;
    margin: 0 10px;
}

/* --- 원형 버튼 공통 스타일 --- */
.circle-button {
    border-radius: 50%;
    cursor: pointer;
    background-color: #FFFFFF; 
    flex-shrink: 0;
    border: 2px solid transparent; 
    transition: all 0.2s;
}

/* ----------------------------------- */
/* 1. 크기별 스타일 (size-1 부터 size-7까지) */
/* ----------------------------------- */

.circle-button.size-1, .circle-button.size-7 { width: 70px; height: 70px; }
.circle-button.size-2, .circle-button.size-6 { width: 50px; height: 50px; }
.circle-button.size-3, .circle-button.size-5 { width: 40px; height: 40px; }
.circle-button.size-4 { width: 30px; height: 30px; }


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
</style>