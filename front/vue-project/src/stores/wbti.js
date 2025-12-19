import { fetchAiAnalysis } from '@/api/ai';
import api from '@/api/axios';
import { WBTI_QUESTIONS } from '@/constants/wbtiData';
import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useWbtiStore = defineStore('wbti', () => {

    // state
    const resultScores = ref({
        socialType: 0,
        motivationType: 0,
        executionType: 0,
        activityType: 0,
    });

    const userCondition = ref({
        experiences: [],
        quitReason: '',
        bodyConditions: [],
        availableTime: '',
        economy: '',
        purpose: '',
    })

    const isSubmitting = ref(false);

    // getters

    /**
     * 질문 리스트에서 타입별 질문 개수를 계산
     */
    const questionCounts = computed(() => {
        const counts = { socialType: 0, motivationType: 0, executionType: 0, activityType: 0 };
        
        WBTI_QUESTIONS.forEach(q => {
            if (counts.hasOwnProperty(q.type)){
                counts[q.type]++;
            }
        })

        return counts;
    });

    /**
     * 평균 점수를 기반으로 WBTI 코드 및 평균값 반환
     */
    const wbtiResult = computed(() => {
        const scores = resultScores.value;
        const counts = questionCounts.value;

        // 평균 계산 함수
        const getAvg = (type) => counts[type] > 0 ? (scores[type] / counts[type]) : 4;
        
        // 1. 각 타입별 코드를 결정합니다.
        const gi = getAvg('socialType') >= 4 ? 'G' : 'I';
        const re = getAvg('motivationType') >= 4 ? 'R' : 'E';
        const jp = getAvg('executionType') >= 4 ? 'J' : 'P';
        const dc = getAvg('activityType') >= 4 ? 'D' : 'C';

        // 2. 결정된 코드들을 합쳐 4자리 code를 만듭니다.
        return {
            code: `${gi}${re}${jp}${dc}`, // gi, re, jp, dc를 사용하여 "GRJD" 형태 생성
            averages: {
                socialAvg: getAvg('socialType').toFixed(1),
                motivationAvg: getAvg('motivationType').toFixed(1),
                executionAvg: getAvg('executionType').toFixed(1),
                activityAvg: getAvg('activityType').toFixed(1)
            }
        };
    });


    // actions

    /**
     * 뷰 컴포넌트에서 호출하여 점수를 누적하는 액션
     * @param {string} type - 'social', 'intmotivation', 'intexecution', 'intactivity' 중 하나
     * @param {number} score - 1 부터 7까지의 점수
     */
    const accumulateScore = (type, score) => {
        resultScores.value[type] += score;
    }

    /**
     * 최종 결과를 백엔드에 전송하는 액션
     * 
     */
    const submitResults = async () => {

        if(isSubmitting.value) return;

        try {

            isSubmitting.value = true;
            
            const aiResponse = await fetchAiAnalysis(wbtiResult.value, userCondition.value);
        
            const payload = {
                wbtiCode: wbtiResult.value.code,
                socialScore: wbtiResult.value.averages.socialAvg,
                motivationScore: wbtiResult.value.averages.motivationAvg,
                executionScore: wbtiResult.value.averages.executionAvg,
                activityScore: wbtiResult.value.averages.activityAvg,

                aiAnalysis: aiResponse.analysis, 
                aiEconomicTip: aiResponse.recommendation.economic_tip,
                ...userCondition.value
            }

            const response = await api.post('/wbti/', payload)
            
            console.log('WBTI 결과 전송 성공:', response.data);
            return { dbData: response.data, aiData: aiResponse }
        } catch(error){
            
            throw error;
        }
    };

    const resetScores = () => {
        resultScores.value = {
            socialType: 0,
            motivationType: 0,
            executionType: 0,
            activityType: 0,
        };
    };

    return {
        resultScores,
        userCondition,
        wbtiQuestions: WBTI_QUESTIONS,
        questionCounts,
        wbtiResult,
        accumulateScore,
        submitResults,
        resetScores
    }
})