import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "../api/axios";

export const usePlanStore = defineStore('plan', () => {

  /**
   * state: 상태 정의
   * 스토어의 핵심 데이터
   */
  const plans = ref([]);

  /**
   * getters: 계산된 상태
   * state를 기반으로 파생된 데이터를 계산
   * 컴포넌트의 computed()와 유사
   * 중요: 상태를 변경하지 않고 읽기 전용으로 사용함
   */
    const planCount = computed(() => {
    return plans.value.filter((plan) => { return plan.isDone }).length
    })

  /**
   * actions: 상태 변경 로직
   * 상태를 변경하거나 외부 API 호출 등 비즈니스 로직을 처리함
   */

    /**
   * 세션 기반 로그인 상태 동기화
   * - 새로고침(F5) 이후에도 로컬 저장소를 통해 Access Token 존재 여부 확인
   * - 로그인 상태라면 user를 세팅, 아니라면 null로 초기화
   */
  const fetchMe = () => {
    loading.value = true;
    error.value = null;

    const accessToken = localStorage.getItem("accessToken");
    if(!accessToken){
      user.value = null;
      initialized.value = true;
      loading.value = false;
      return Promise.resolve();
    }

    return api
      .get("/plan/detail")
      .then((res) => {
        if(res.data && res.data.id){
          user.value = res.data;
        }else{
          user.value = null;
        }
      })
      .catch((e) => {
        localStorage.removeItem("accessToken");
        user.value = null;
      })
      .finally(() => {
        loading.value = false;
        initialized.value = true;
      })
    }

    return {
      plans,
      planCount,
      fetchMe,
    }

})
