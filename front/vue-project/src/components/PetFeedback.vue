<template>
    <div class="feedback-section">
        <div class="feedback-bubble">
            <img :src="currentPetSrc" class="current-pet" />
            <div class="speech-bubble">
                <p>{{ feedbackMessage }}</p>
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import haedalMainImg from '../assets/imgs/haedal-main.png'
import haedalSadImg1 from '../assets/imgs/haedal-sad1.png'
import haedalSadImg2 from '../assets/imgs/haedal-sad2.png'
import haedalSadImg3 from '../assets/imgs/haedal-sad3.png'
import haedalHappyImg1 from '../assets/imgs/haedal-happy1.png'
import haedalHappyImg2 from '../assets/imgs/haedal-happy2.png'
import haedalHappyImg3 from '../assets/imgs/haedal-happy3.png'

const currentPetSrc = ref(haedalSadImg2);

// 더미 데이터
const feedbackMessage = ref('조금만 더 열심히 해줘!');

const auth = useAuthStore();
onMounted(async () => {
    auth.fetchMe();
    // 1. 레벨 정보 가져오기
    // const level = await fetchUserLevel();

    // 2. 레벨에 따라 펫 이미지 결정
    // petImage.value = getPetImageByLevel(level);

    // 3. AI API 호출해서 피드백 메시지 생성
    // const aiResponse = await fetchAIFeedback(level, workoutData);
    // message.value = aiResponse.message;
});
</script>


<style scoped>
.feedback-section {
    flex: 1;
    max-width: 21.875rem;
    /* 350px */
    display: flex;
    align-items: center;
    justify-content: center;
}

.feedback-bubble {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.current-pet {
    width: 14.875rem;
    /* 238px */
    height: 16rem;
    /* 256px */
    object-fit: contain;
    margin-bottom: 1rem;
}

.speech-bubble {
    position: absolute;
    top: 1rem;
    right: -5rem;
    background: #FFFFFF;
    border: 2px solid #000000;
    border-radius: 1.5rem;
    padding: 0.75rem 1.25rem;
    max-width: 12.5rem;
    /* 200px */
}

.speech-bubble::before {
    content: '';
    position: absolute;
    bottom: -0.5rem;
    left: 2rem;
    width: 0;
    height: 0;
    border-left: 0.5rem solid transparent;
    border-right: 0.5rem solid transparent;
    border-top: 0.5rem solid #000000;
}

.speech-bubble p {
    font-family: 'Noto Sans KR', sans-serif;
    font-size: 0.9375rem;
    /* 15px */
    line-height: 1.125rem;
    /* 18px */
    text-align: center;
    color: #000000;
    margin: 0;
}
</style>