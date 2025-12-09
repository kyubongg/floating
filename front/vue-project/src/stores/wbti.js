import api from '@/api/axios';
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useWbtiStore = defineStore('wbti', () => {

    // state
    const resultScores = ref({
        socialType: 0,
        motivationType: 0,
        executionType: 0,
        activityType: 0,
    });

    // getters


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
     */
    const submitResults = async () => {
        
        const payload = resultScores.value;

        try {
            const response = await api.post('/wbti/', payload)
            
            console.log('WBTI 결과 전송 성공:', response.data);
            return response.data;
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
        accumulateScore,
        submitResults,
        resetScores
    }
})