// src/constants/wbtiData.js
export const WBTI_PERSONAS = [
  { code: "GRJD", name: "열정적 파이터 (E.P.F.)", desc: "그룹 속 체계적/고강도 경쟁, 확실한 결과 추구", category: "고강도 인터벌 & 순환" },
  { code: "GRJC", name: "규율적 팀메이트 (R.T.M.)", desc: "팀과 함께 정해진 루틴, 안전하고 꾸준한 결과 축적", category: "수영 & 아쿠아" },
  { code: "GRPD", name: "도전적 플레이어 (C.P.L.)", desc: "그룹과 함께 고강도 즉흥 활동, 성취감과 결과", category: "레크리에이션/팀 스포츠" },
  { code: "GRPC", name: "협력적 관찰자 (C.O.B.)", desc: "타인과 부드러운 활동, 편안한 방식으로 참여", category: "요가 & 필라테스 (그룹 클래스)" },
  { code: "GEJD", name: "활동적 리더 (A.L.D.)", desc: "그룹 즐거움 중시, 계획된 고강도 루틴으로 에너지 발산", category: "고강도 인터벌 & 순환" },
  { code: "GEJC", name: "사교적 학습가 (S.L.G.)", desc: "그룹 내 정해진 강습, 편안하고 재미있는 경험", category: "수영 & 아쿠아 (강습)" },
  { code: "GEPD", name: "즉흥적 엔조이어 (I.E.Z.)", desc: "사람들과 어울려 놀이처럼 즉흥적 즐거움 추구", category: "레크리에이션/팀 스포츠" },
  { code: "GEPC", name: "평온한 동행자 (P.C.W.)", desc: "타인과 함께하는 과정 만족, 편안하고 자율적 활동", category: "유산소/러닝 (단체 걷기)" },
  { code: "IRJD", name: "고독한 성취가 (S.A.C.)", desc: "혼자만의 체계적 계획, 고강도 운동으로 확실한 결과", category: "웨이트/헬스" },
  { code: "IRJC", name: "정밀한 자기관리자 (P.S.M.)", desc: "혼자서 정확한 자세/루틴, 세밀한 결과 관리", category: "요가 & 필라테스 (기구/매트)" },
  { code: "IRPD", name: "자유로운 기록자 (F.R.D.)", desc: "혼자서 고강도 즉흥 운동, 데이터 기록 및 확인", category: "유산소/러닝 (기록 측정)" },
  { code: "IRPC", name: "집중적 연구원 (I.R.E.)", desc: "혼자서 저강도 자율 활동, 몸 상태 변화 관찰", category: "수영 & 아쿠아 (자유 수영)" },
  { code: "IEJD", name: "철저한 자기개발자 (T.S.D.)", desc: "혼자만의 목표/루틴, 고강도 과정의 성장 즐거움", category: "웨이트/헬스" },
  { code: "IEJC", name: "평정심 유지자 (P.K.M.)", desc: "혼자 정해진 방식, 조용하고 부드러운 내면 평온 추구", category: "요가 & 필라테스 (매트 요가)" },
  { code: "IEPD", name: "자유로운 탐험가 (F.E.X.)", desc: "혼자서 고강도 즉흥 활동, 새로운 경험과 재미 추구", category: "유산소/러닝 (야외 트레일)" },
  { code: "IEPC", name: "평화로운 명상가 (P.M.T.)", desc: "혼자서 최저 강도, 자유롭고 평화로운 휴식", category: "요가 & 필라테스 (명상/회복)" }
];

// src/constants/wbtiQuestions.js
export const WBTI_QUESTIONS = [
  // [G/I] 에너지 방향 (socialType)
  { id: 1, type: 'socialType', text: '운동할 때 나는 혼자 집중하는 것보다 여럿이 함께 즐기는 것을 선호한다.', left: '혼자 집중', right: '여럿이 함께' },
  { id: 2, type: 'socialType', text: '운동 후 나 자신과의 약속을 지켰을 때보다 사람들과 하이파이브 할 때 더 만족스럽다.', left: '자신과의 약속', right: '하이파이브' },
  { id: 3, type: 'socialType', text: '새로운 운동을 시작한다면 유튜브 독학보다 PT나 그룹 수업을 듣는 편을 택한다.', left: '유튜브 독학', right: '그룹 수업' },
  { id: 4, type: 'socialType', text: '운동 중 옆 사람이 말을 걸면 집중이 깨지기보다 반갑고 재미있다.', left: '집중 깨짐', right: '반갑고 재미남' },
  { id: 5, type: 'socialType', text: '운동 메이트가 있으면 신경 쓰이기보다 동기부여가 되어 지속하기 쉽다.', left: '부담/신경쓰임', right: '동기부여/지속' },

  // [R/E] 핵심 동기 (motivationType)
  { id: 6, type: 'motivationType', text: '운동을 하는 진짜 이유는 운동하는 순간의 즐거움보다 몸무게나 근육량 숫자를 확인하기 위해서다.', left: '순간의 즐거움', right: '숫자 확인', isReverse: true },
  { id: 7, type: 'motivationType', text: '성공적인 운동의 기준은 과정의 즐거움보다 목표 달성(3kg 감량 등) 여부다.', left: '즐거웠던 과정', right: '목표 달성', isReverse: true },
  { id: 8, type: 'motivationType', text: '사진을 찍는다면 운동하는 순간의 풍경보다 인바디 결과지나 숫자를 찍고 싶다.', left: '순간의 풍경', right: '인바디/결과', isReverse: true },
  { id: 9, type: 'motivationType', text: '운동 후 가장 먼저 하는 생각은 "기분 좋다"보다 "오늘 몇 칼로리 소모했지?"이다.', left: '기분 좋다', right: '칼로리 소모', isReverse: true },
  { id: 10, type: 'motivationType', text: '한 달 뒤 내 모습으로 기대되는 것은 여유로워진 마음보다 눈에 띄게 달라진 몸이다.', left: '여유로운 마음', right: '달라진 몸', isReverse: true },

  // [J/P] 운동 방식 (executionType)
  { id: 11, type: 'executionType', text: '운동 계획을 세울 때 나는 기분대로 하기보다 요일별로 상세히 짜놓는 편이다.', left: '기분대로', right: '상세한 계획' },
  { id: 12, type: 'executionType', text: '운동 루틴이 깨지면 수용하기보다 스트레스와 죄책감을 느낀다.', left: '수용함', right: '스트레스/죄책감' },
  { id: 13, type: 'executionType', text: '운동 기록은 특별히 안 하기보다 앱이나 수첩에 매일 상세히 적는 편이다.', left: '안 함', right: '매일 기록' },
  { id: 14, type: 'executionType', text: '같은 운동을 반복하는 것이 지루하기보다 익숙하고 효과적이라서 좋다.', left: '지루함', right: '익숙하고 좋음' },
  { id: 15, type: 'executionType', text: '운동 프로그램 선택 시 자유로운 구성보다 체계적으로 짜여진 커리큘럼을 선호한다.', left: '자유로운 구성', right: '체계적 커리큘럼' },

  // [D/C] 선호 강도 (activityType)
  { id: 16, type: 'activityType', text: '운동 후 이상적인 상태는 상쾌하고 가벼운 몸보다 땀이 흠뻑 나고 숨이 헐떡이는 상태다.', left: '상쾌/가벼움', right: '땀/헐떡임' },
  { id: 17, type: 'activityType', text: '내가 좋아하는 운동 템포는 부드러운 움직임보다 빠르고 격렬한 동작이다.', left: '부드러움', right: '빠르고 격렬함' },
  { id: 18, type: 'activityType', text: '운동 강도를 높이는 것은 부담스럽기보다 흥미진진하고 도전적이다.', left: '부담스러움', right: '흥미진진함' },
  { id: 19, type: 'activityType', text: '근육통이 오면 걱정되기보다 제대로 운동했다는 증거 같아 뿌듯하다.', left: '무리가 갈까 걱정', right: '뿌듯함' },
  { id: 20, type: 'activityType', text: '심박수가 높게 오르면 멈추고 싶기보다 운동이 잘 되는 느낌이라 최고다.', left: '멈추고 싶음', right: '운동되는 느낌' }
];

// src/constants/wbtiQuestions.js에 추가

export const USER_CONDITION_QUESTIONS = {
  experience: {
    id: 'Q0',
    text: '과거 운동 경험 (복수선택 가능)',
    options: ['헬스/웨이트', '요가/필라테스', '러닝/걷기', '수영', '팀 스포츠', '크로스핏', '운동 경험 없음']
  },
  quitReason: {
    id: 'Q0-1',
    text: '그만둔 주된 이유는?',
    options: ['시간 부족', '지루함', '체력적 한계', '부상/통증', '비용 부담', '혼자 하기 힘듦', '해당 없음']
  },
  bodyCondition: {
    id: 'Q0-2',
    text: '현재 몸 상태 (복수선택)',
    options: ['불편함 없음', '무릎 통증', '허리 통증', '어깨/목 통증', '손목/발목 약함', '디스크', '기타']
  },
  availableTime: {
    id: 'Q0-3',
    text: '운동 가능한 시간',
    options: ['15분 이하', '20-30분', '40분-1시간', '1시간 이상', '상관없음']
  }
};