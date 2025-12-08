import { defineStore } from 'pinia';

export const useWbtiStore = defineStore('wbti', () => {

    // state
    const resultScores = ref({
        social_type: 0,
        intmotivation_type: 0,
        intexecution_type: 0,
        intactivity_type: 0,
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
            const response = await api.post('/api/v1/wbti', payload)
            
            console.log('WBTI 결과 전송 성공:', response.data);
            return response.data;
        } catch(error){
            
            throw error;
        }
    };

    const resetScores = () => {
        resultScores.value = {
            social_type: 0,
            motivation_type: 0,
            execution_type: 0,
            activity_type: 0,
        };
    };

    return {
        resultScores,
        accumulateScore,
        submitResults,
        resetScores
    }
})