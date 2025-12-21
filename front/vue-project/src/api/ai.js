import { getWbtiAnalysisPrompt, WBTI_PROMPTS } from "@/constants/prompts"
import axios from "axios";
import api from "./axios";


export const fetchAiAnalysis = async (wbtiResult, userCondition) => {
  
  const systemPrompt = WBTI_PROMPTS;
  const userPrompt = getWbtiAnalysisPrompt(wbtiResult, userCondition);

  try {

    const response = await axios.post(
      '/api-openai/api.openai.com/v1/chat/completions',
      {
        model: 'gpt-4o-mini',
        messages: [
          { role: 'system', content: systemPrompt },
          { role: 'user', content: userPrompt }
        ],
        response_format: { type: 'json_object' },
        temperature: 0.7
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