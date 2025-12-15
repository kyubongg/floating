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

  const today = dayjs().startOf('day');
  const startOfWeek = dayjs().startOf('isoWeek'); // 월요일 기준, 일요일은 week

  /**
   * getters: 계산된 상태
   * state를 기반으로 파생된 데이터를 계산
   * 컴포넌트의 computed()와 유사
   * 중요: 상태를 변경하지 않고 읽기 전용으로 사용함
   */

  // 연속 운동 횟수
  const streakCount = computed(() => {
    const completedDates = [...new Set(
      plans.value
        .filter(p => p.completeDate !== null)
        .map(p => p.completeDate)
    )].sort((a, b) => dayjs(b).diff(dayjs(a)));

    if (completedDates.length === 0) return 0;

    let count = 0;
    let checkDate = dayjs(completedDates[0]);
    const diffToday = today.diff(checkDate, 'day');

    if (diffToday <= 1) {
      count = 1;
      for (let i = 1; i < completedDates.length; i++) {
        const prevDate = dayjs(completedDates[i]);
        if (checkDate.diff(prevDate, 'day') === 1) {
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
  const totalCompletedCount = computed(() => {
    return plans.value.filter((p) => p.completeDate !== null).length;
  });

  // 이번 주 운동 횟수
  const weeklyWorkouts = computed(() => {
    return plans.value.filter(p => {
      if (p.completeDate === null) return false;
      const completeDay = dayjs(p.completeDate).startOf('day');
      return completeDay.isBetween(startOfWeek, today, 'day', '[]');
    }).length;
  });

  // 이번주 계획 유무
  const hasWeeklyPlan = computed(() => {
    const endOfWeek = dayjs().endOf('isoWeek');
    return plans.value.some(p => {
      return dayjs(p.date).isBetween(startOfWeek, endOfWeek, 'day', '[]');
    });
  });

  // 이번주 계획
  const weekDaysWithPlans = computed(() => {
    const dayNames = ['일', '월', '화', '수', '목', '금', '토'];
    const result = [];

    for (let i = 0; i < 7; i++) {
      const currentDay = startOfWeek.add(i, 'day');
      const dayName = dayNames[currentDay.day()];
      const isToday = currentDay.isSame(today, 'day');

      // 해당 날짜의 계획들 찾기
      const dayPlans = plans.value.filter(p => {
        return dayjs(p.date).isSame(currentDay, 'day');
      });

      // 완료 여부 확인
      const completed = dayPlans.some(p => p.completeDate !== null);

      // 운동 정보 (detail + time)
      const exerciseText = dayPlans.length > 0
        ? dayPlans.map(p => `${p.detail}\n${p.time}분`).join('\n\n')
        : null;

      // 운동 detail 정보
      const exerciseCategory = dayPlans.length > 0
        ? dayPlans.map(p => `${p.category}`).join('\n\n')
        : null;

      // 운동 detail 정보
      const exerciseDetail = dayPlans.length > 0
        ? dayPlans.map(p => `${p.detail}`).join('\n\n')
        : null;
      
      // 운동 time 정보
      const exerciseTime = dayPlans.length > 0
        ? dayPlans.map(p => `${p.time}분`).join('\n\n')
        : null;

      // 미룬 횟수
      const postponed = dayPlans.length > 0
        ? Math.max(...dayPlans.map(p => p.shifted))
        : 0;

      result.push({
        name: dayName,
        date: currentDay.format('YYYY-MM-DD'),
        exercise: exerciseText,
        exerciseCategory,
        exerciseDetail,
        exerciseTime,
        completed,
        hasExercise: dayPlans.length > 0,
        isToday,
        postponed,
        plans: dayPlans // 원본 데이터도 저장
      });
    }

    return result;
  });

  // 오늘의 계획
  const todayPlans = computed(() => {
    return plans.value.filter(p => {
      return dayjs(p.date).isSame(today, 'day');
    });
  });

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

  // 주간 계획 생성
  const createWeeklyPlan = async (type) => {
    // type: 'postpone' (지난주 미루기) or 'ai' (AI 추천)
    loading.value = true;
    error.value = null;

    try {
      const res = await api.post('/plan/weekly', { type });
      plans.value = res.data.planList;
    } catch (error) {
      console.error('주간 계획 생성 실패:', error);
      error.value = error.message;
    } finally {
      loading.value = false;
    }
  };

  // 오늘 계획 변경
  const updateTodayPlan = async (action) => {
    // action: 'postpone' (다음날로 미루기) or 'alternative' (AI 대체 운동)
    loading.value = true;
    error.value = null;

    try {
      const res = await api.put('/plan/today', { action });
      await fetchPlan(true); // 강제 새로고침
    } catch (error) {
      console.error('오늘 계획 수정 실패:', error);
      error.value = error.message;
    } finally {
      loading.value = false;
    }
  };

  // 계획 완료 토글
  const togglePlanComplete = async (date) => {
  loading.value = true;
  error.value = null;

  try {
    // 해당 날짜의 계획들 찾기
    const dayPlans = plans.value.filter(p => {
      return dayjs(p.date).isSame(dayjs(date), 'day');
    });

    if (dayPlans.length === 0) return;

    // 이미 완료된 경우 -> 완료 취소
    const isCompleted = dayPlans.some(p => p.completeDate !== null);
    
    if (isCompleted) {
      // 완료 취소 API
      await api.put(`/plan/uncomplete`, { date });
    } else {
      // 완료 처리 API
      await api.put(`/plan/complete`, { date });
    }

    // 데이터 새로고침
    await fetchPlan(true);
  } catch (error) {
    console.error('계획 완료 토글 실패:', error);
    error.value = error.message;
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
    today: today.format('YYYY-MM-DD'),
    totalCompletedCount,
    startOfWeek: startOfWeek.format('YYYY-MM-DD'),
    weeklyWorkouts,
    hasWeeklyPlan,
    weekDaysWithPlans, 
    todayPlans, 
    fetchPlan,
    createWeeklyPlan, 
    updateTodayPlan, 
    togglePlanComplete,
  }

})
