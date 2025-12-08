<template>
    <div class="main-page">
        <div class="logo-area">
            <img class="logo-area__image"src="../assets/imgs/haedal-workout3.png" alt="FLOATING 로고 수달 이미지" > 
            <div class="logo-area__title">EBTI 검사 시작하기</div>
            <span>EBTI는 운동 성향을 측정하는 검사입니다.</span>
        </div>
        <div>
            <button class="start-button" @click="goToWbtiTest">검사 시작</button> 
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';


    const router = useRouter();

    const goToWbtiTest = () => {
        router.push({ name: 'wbtiTest' });
    }
</script>

<style scoped>
/* 전체 페이지 스타일 설정 */

.main-page {
    /* 이미지와 같이 전체 배경색을 어두운 회색으로 설정 */
    background-color: #6C6C6C; /* 이미지 배경색과 유사하게 설정 */
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
}

/* 로고 영역 (수달 이미지, 제목, 설명 텍스트 포함) */
.logo-area {
    display: flex;
    flex-direction: column;
    align-items: center;
}

/* 수달 이미지 스타일 */
.logo-area__image {
    width: 125px; /* 이미지 크기 조정 */
    height: auto;
    margin-bottom: 20px;
    /* 실제 이미지가 없으므로, 이미지 대신 보여질 영역만 설정합니다. 
       만약 실제 이미지를 사용한다면 이 부분은 필요 없을 수 있습니다. */
    /* background: url('../assets/imgs/haedal-main.jpeg') no-repeat center center/cover; */
}

/* 'EBTI 검사 시작하기' 제목 스타일 */
.logo-area__title {
    /* EBTI 검사 시작하기 */
    font-family: 'Noto Sans KR', sans-serif;
    font-style: normal;
    font-weight: 900;
    font-size: 30px;
    line-height: 36px;
    letter-spacing: 0.25px;
    color: #FFFFFF;
    margin-bottom: 10px;
}

/* 'EBTI는 운동 성향을 측정하는 검사입니다.' 설명 텍스트 스타일 */
.logo-area > span {
    /* EBTI는 운동 성향을 측정하는 검사입니다. */
    font-family: 'Noto Sans KR', sans-serif;
    font-style: normal;
    font-weight: 700;
    font-size: 18px;
    line-height: 22px;
    letter-spacing: 0.25px;
    color: #FFFFFF;
    margin-bottom: 50px; /* 버튼과의 간격 조정 */
}

/* 버튼 스타일 */
.start-button {
    box-sizing: border-box;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 12px 20px; /* 패딩 조정 */
    gap: 8px;

    /* 이미지의 버튼 색상과 동일하게 설정 */
    background: #2C2C2C; 
    border: 1px solid #2C2C2C;
    border-radius: 15px;
    
    color: #FFFFFF;
    font-family: 'Noto Sans KR', sans-serif;
    font-weight: 700;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.start-button:hover {
    background: #3a3a3a;
}
</style>