import { defineStore } from "pinia";
import { ref, computed } from "vue";
import dayjs from 'dayjs';
import isBetween from 'dayjs/plugin/isBetween';
import isoWeek from 'dayjs/plugin/isoWeek';
dayjs.extend(isBetween);
dayjs.extend(isoWeek);
import api from "../api/axios";

export const usePlanStore = defineStore('plan', () => {

  /**
   * state: 상태 정의
   * 스토어의 핵심 데이터
   */
  const plans = ref([]);
  const weeklyPlans = ref([]);
  const error = ref(null);
  const loading = ref(false);

  /**
   * getters: 계산된 상태
   * state를 기반으로 파생된 데이터를 계산
   * 컴포넌트의 computed()와 유사
   * 중요: 상태를 변경하지 않고 읽기 전용으로 사용함
   */

  // 연속 운동 횟수
  const streakCount = computed(() => {
    const completedDates = [...new Set( // 완료된 날짜들만 중복 제거해서 내림차순 정렬
      plans.value
        .filter(p => p.completeDate !== null)
        .map(p => p.completeDate)
    )].sort((a, b) => dayjs(b).diff(dayjs(a)));

    if (completedDates.length === 0) {
      streakCount.value = 0;
      return;
    }

    let count = 0;
    let checkDate = dayjs(completedDates[0]); // 가장 최근에 운동한 날

    const diffToday = dayjs(today).diff(checkDate, 'day');

    if (diffToday <= 1) { // 마지막 운동일이 오늘 혹은 어제여야 스트릭이 유지됨
      count = 1;
      for (let i = 1; i < completedDates.length; i++) {
        const prevDate = dayjs(completedDates[i]);
        if (checkDate.diff(prevDate, 'day') === 1) { // 날짜 차이가 딱 1일이면 연속인 것으로 간주
          count++;
          checkDate = prevDate;
        } else {
          break;
        }
      }
    }
    return count;
  });

  // 총 운동 완료 횟수
  const today = dayjs().startOf('day').format('YYYY-MM-DD');
  const totalCompletedCount = computed(() => {
    return plans.value.filter((p) => p.completeDate !== null).length;
  })

  // 이번 주 운동 횟수 / 7 (일요일 기준)
  const startOfWeek = dayjs().startOf('week').startOf('day').format('YYYY-MM-DD'); // 월요일은 dayjs().startOf('isoWeek')
  const weeklyWorkouts = computed(() => {
    return plans.value.filter(p => {
      if (p.completeDate === null) {
        return false;
      }
      const completeDay = dayjs(p.completeDate).startOf('day');
      return completeDay.isBetween(startOfWeek, today, 'day', '[]');
    }).length;
  })

  // 이번주 계획 유무 (일요일 기준)
  const hasWeeklyPlan = computed(() => {
    const today = dayjs();
    const startOfWeek = today.startOf('week'); // 월요일은 dayjs().startOf('isoWeek')
    const endOfWeek = today.endOf('week'); // 월요일은 dayjs().endOf('isoWeek')

    const found = plans.value.some(p => {
      return dayjs(p.date).isBetween(startOfWeek, endOfWeek, 'day', '[]');
    });

    return found;
  })

  /**
   * actions: 상태 변경 로직
   * 상태를 변경하거나 외부 API 호출 등 비즈니스 로직을 처리함
   */

  const fetchPlan = async (force = false) => {
    // 이 함수를 강제하지 않거나, 이미 데이터가 있으면 API 호출 생략
    if (!force && plans.value && plans.value.length > 0) return;

    loading.value = true;
    error.value = null;

    try {
      const res = await api.get('/plan/');
      plans.value = res.data.planList;
    } catch (error) {
      console.error('계획 로드 실패:', error);
    } finally {
      loading.value = false;
    }
  };

  return {
    plans,
    weeklyPlans,
    error,
    loading,
    streakCount,
    today,
    totalCompletedCount,
    startOfWeek,
    weeklyWorkouts,
    hasWeeklyPlan,
    fetchPlan,
  }

})
