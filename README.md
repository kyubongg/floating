# 🏊‍♂️ 프로젝트 명: Floating
**"나에게 딱 맞는 운동, AI가 찾아주는 나의 운동 정체성"**

## 1. 프로그램 개요
Floating은 MBTI 성격 유형 검사에서 착안하여, 사용자의 성향과 생활 습관을 분석해 최적의 운동 방식을 제안하는 서비스입니다. 단순히 운동 종목을 추천하는 것에 그치지 않고, 사용자의 경제적 상황과 가용 시간 등 현실적인 조건을 반영하여 AI가 개인화된 운동 솔루션을 제공하며 하루 기록을 통해 지속적인 운동 습관 형성을 돕습니다.

## 2. 기술 스택
### Frontend
![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

### AI
![ChatGPT](https://img.shields.io/badge/chatGPT-74aa9c?style=for-the-badge&logo=openai&logoColor=white)

## 3. 목표
1. **개인화된 운동 경험** 

    설문 데이터와 AI 분석을 결합하여 사용자에게 가장 효율적인 운동 타입(GRJD, IEPC 등)을 정의합니다.

2. **운동 지속성 부여**

    캘린더 기능을 통해 매일의 계획을 세우고, 사진과 글이 포함된 리뷰를 남겨 성취감을 고취합니다.

3. **현실적인 솔루션** 

    사용자의 경제적 여건(Economy)과 시간(Available Time)을 고려한 실질적인 경제적 팁을 제공합니다.

## 4. 페르소나
사용자의 8가지 지표 조합에 따라 AI가 생성하는 대표 페르소나 예시입니다.

"열정적인 활동가": 에너지가 넘치고 단체 운동에서 시너지를 얻는 타입 (G/E/J/D 성향 강세)

"조용한 계획가": 혼자만의 루틴을 지키며 정적인 환경에서 집중하는 타입 (I/R/P/C 성향 강세)

"가성비 추구형 운동가": 최소한의 비용과 시간으로 최대 효율을 내고자 하는 실용적 타입

## 5. 고객 여정 지도 (Customer Journey Map)
진입 및 로그인: 소셜 혹은 일반 로그인을 통해 시스템에 접속합니다.

성향 테스트: 20여 개의 문항을 통해 사회성, 동기, 실행력, 활동성 지표를 측정합니다.

결과 확인: AI가 분석한 나의 WBTI 코드와 페르소나, 맞춤형 경제 팁을 확인합니다.

계획 및 실천: 메인 화면에서 오늘 할 운동 계획을 세웁니다.

기록 및 리뷰: 운동 완료 후 사진 슬라이더와 텍스트 리뷰 기능을 이용해 하루 기록을 저장합니다.

## 6. 핵심 기능
🔐 스마트 라우팅 및 인증 시스템
Navigation Guard: 로그인하지 않은 사용자의 접근을 차단하고, 로그인 시 원래 목적지로 자동 리다이렉트합니다.

중복 검사 방지: 이미 WBTI 결과가 있는 사용자가 테스트 페이지에 접근 시 결과 페이지로 자동 이동시킵니다.

🤖 AI 운동 성향 분석 (JSON Data Handling)
Hybrid Data Storage: 고정된 점수는 일반 컬럼에, 유동적인 AI 분석 결과는 DB의 JSON 타입 컬럼에 저장하여 데이터 유연성을 확보했습니다.

MyBatis JsonTypeHandler: 자바 객체(Map, List)를 DB JSON 문자열과 자동으로 상호 변환하는 커스텀 핸들러를 구현했습니다.

📅 인터랙티브 캘린더 & 리뷰 슬라이더
Daily Review: 계획 완료 여부에 따라 리뷰 작성을 활성화하며, 한 기록에 최대 5장의 이미지를 슬라이드 형태로 업로드/조회할 수 있습니다.

Debounced Save: 리뷰 작성 시 1.5초간 입력이 없을 때 자동으로 저장하여 사용자 편의성을 높였습니다.