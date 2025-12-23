import { getRevisePlanPrompt, getWbtiAnalysisPrompt, getWeeklyPlanPrompt, REVISE_PLAN_PROMPTS, WBTI_PROMPTS, WEEKLY_PLAN_PROMPTS } from "@/constants/prompts"
import axios from "axios";
import api from "./axios";
import dayjs from "dayjs";

const timeMap = {
  '15분 이하': 15,
  '20-30분': 30,
  '40분-1시간': 60,
  '1시간 이상': 90,
  '상관없음': 30
};

// function: wbti 검사 요청 함수
export const fetchAiAnalysis = async (wbtiResult, userCondition) => {
  
  const systemPrompt = WBTI_PROMPTS;
  const userPrompt = getWbtiAnalysisPrompt(wbtiResult, userCondition);

  try {

    console.log('AI 분석 시작');
    const response = await axios.post(
      '/api-openai/api.openai.com/v1/chat/completions',
      {
        model: 'gpt-4o',
        messages: [
          { role: 'system', content: systemPrompt },
          { role: 'user', content: userPrompt }
        ],
        response_format: { type: 'json_object' }
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${import.meta.env.VITE_OPENAI_API_KEY}`
        }
      }
    );

    const aiContent = JSON.parse(response.data.choices[0].message.content);

    console.log("AI 분석 완료: ", aiContent);
    return aiContent;
  } catch (error) {
        console.error("AI 분석 중 오류 발생:", error.response?.data || error.message);
        throw error;
  }

}

// function: wbti 기반 사용자 운동 주간 계획 추천 함수
export const fetchAiWeeklyPlan = async (aiResult, userCondition) => {

  const todayStr = dayjs().format('YYYY-MM-DD');
  const limitTime = timeMap[userCondition.availableTime] || 30;

  const systemPrompt = WEEKLY_PLAN_PROMPTS;

  const modifiedCondition = { ...userCondition, availableTime: `${limitTime}` };

  const userPrompt = getWeeklyPlanPrompt(aiResult, modifiedCondition, todayStr);

  console.log(userCondition);
  console.log(modifiedCondition);
  try {

    const response = await axios.post(
      '/api-openai/api.openai.com/v1/chat/completions',
      {
        model: 'gpt-4o',
        messages: [
          { role: 'system', content: systemPrompt },
          { role: 'user', content: userPrompt }
        ],
        response_format: { type: 'json_object' }
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${import.meta.env.VITE_OPENAI_API_KEY}`
        }
      }
    );

    const aiContent = JSON.parse(response.data.choices[0].message.content);

    console.log("AI 계획 생성 완료: ", aiContent);
    return aiContent;
  } catch (error) {
        console.error("AI 분석 중 오류 발생:", error.response?.data || error.message);
        throw error;
  }

}

// function: wbti 기반 사용자 운동 하루 계획 추천 함수
export const fetchAiRevisePlan = async (aiResult, userCondition, originalPlan) => {

  const limitTime = timeMap[userCondition.availableTime] || 30;
  const modifiedCondition = { ...userCondition, availableTime: `${limitTime}분` };

  const systemPrompt = REVISE_PLAN_PROMPTS;
  const userPrompt = getRevisePlanPrompt(aiResult, modifiedCondition, originalPlan);

  console.log(userCondition.availableTime);
  console.log(limitTime);
  console.log(userCondition);
  console.log(modifiedCondition);
  try {

    const response = await axios.post(
      '/api-openai/api.openai.com/v1/chat/completions',
      {
        model: 'gpt-4o',
        messages: [
          { role: 'system', content: systemPrompt },
          { role: 'user', content: userPrompt }
        ],
        response_format: { type: 'json_object' }
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${import.meta.env.VITE_OPENAI_API_KEY}`
        }
      }
    );

    const aiContent = JSON.parse(response.data.choices[0].message.content);

    console.log("AI 계획 생성 완료: ", aiContent);
    return aiContent;
  } catch (error) {
        console.error("AI 분석 중 오류 발생:", error.response?.data || error.message);
        throw error;
  }

}