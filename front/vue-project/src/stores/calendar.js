import api from "@/api/axios";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useCalenderStore = defineStore('calendar', () => {

  // state
  const reviews = ref([])

  // getters

  // actions
  
  /**
   * 달력 컴포넌트에서 호출하여 완료한 계획의 댓글을 가져오는 액션
   */
  const getReviews = async () => {
    
    try {

      const response = await api.get('/review/');

      reviews.value = response.data.userReviews;
      // console.log(response.data.userReviews);
    } catch (error) {
      throw error;
    }
  }

  return {
    reviews,
    getReviews,

  }
})