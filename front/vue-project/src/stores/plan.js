import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "../api/axios";

export const usePlanStore = defineStore('plan', () => {

  /**
   * state: 상태 정의
   * 스토어의 핵심 데이터
   */
  const plans = ref(null);
  const weeklyPlans = ref(null);
  const loading = ref(false);

  /**
   * getters: 계산된 상태
   * state를 기반으로 파생된 데이터를 계산
   * 컴포넌트의 computed()와 유사
   * 중요: 상태를 변경하지 않고 읽기 전용으로 사용함
   */
  const planCount = computed(() => {
    return plans.value.filter((plan) => { return plan.isDone }).length
  })

  // const consecutiveDays = ref(0);
  // const totalDays = ref(0);
  // const weeklyCount = ref(0);

  /**
   * actions: 상태 변경 로직
   * 상태를 변경하거나 외부 API 호출 등 비즈니스 로직을 처리함
   */

  const fetchPlan = async (force = false) => {
    // 이 함수를 강제하지 않거나, 이미 데이터가 있으면 API 호출 생략
    if (!force && weeklyPlans.value) return;

    loading.value = true;
    error.value = null;

    try {
      const res = await api.get('/plan/');
      weeklyPlans.value = res.data;
    } catch (error) {
      console.error('계획 로드 실패:', error);
    } finally {
      loading.value = false;
    }
  };

  return {
    plans,
    weeklyPlans,
    loading,
    planCount,
    fetchPlan,
  }

})
