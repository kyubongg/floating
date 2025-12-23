<template>
    <div class="feedback-section">
        <div class="feedback-bubble">
            <img :src="currentPetSrc" class="current-pet" />
            <div v-if="feedbackMessage" class="speech-bubble">
                <p>{{ feedbackMessage }}</p>
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import haedalMainImg from '../assets/imgs/haedal-main.png'
import haedalSadImg1 from '../assets/imgs/haedal-sad1.png'
import haedalSadImg2 from '../assets/imgs/haedal-sad2.png'
import haedalSadImg3 from '../assets/imgs/haedal-sad3.png'
import haedalHappyImg1 from '../assets/imgs/haedal-happy1.png'
import haedalHappyImg2 from '../assets/imgs/haedal-happy2.png'
import haedalHappyImg3 from '../assets/imgs/haedal-happy3.png'
import { usePlanStore } from '@/stores/plan';

const authStore = useAuthStore();
const planStore = usePlanStore();

const currentPetSrc = computed(() => {
    switch (authStore.level) {
        case 1:
            return haedalSadImg3;
        case 2:
            return haedalSadImg2;
        case 3:
            return haedalSadImg1;
        case 4: 
            return haedalMainImg;
        case 5:
            return haedalHappyImg1;
        case 6:
            return haedalHappyImg2;
        case 7:
            return haedalHappyImg3;
    }
})

const feedbackMessage = computed(() => {
    if (localStorage.getItem('cheerUpQuotes').length > 0) {
        const storedQuotes = JSON.parse(localStorage.getItem('cheerUpQuotes'));

        const randomIdx = Math.floor(Math.random() * storedQuotes.length);
        return storedQuotes[randomIdx]
    }
    return null;
}); 

onMounted(async () => {
    // AI로 메세지 가져오기
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