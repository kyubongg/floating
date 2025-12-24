INSERT INTO `floating`.`mbti` (`mbti_pk`, `name`) VALUES
(1, 'ISTJ'),
(2, 'ISFJ'),
(3, 'INFJ'),
(4, 'INTJ'),
(5, 'ISTP'),
(6, 'ISFP'),
(7, 'INFP'),
(8, 'INTP'),
(9, 'ESTP'),
(10, 'ESFP');

INSERT INTO `floating`.`mbti` (`mbti_pk`, `name`) VALUES
(0, null);

-- 2. user 테이블 더미 데이터 (mbti_pk 참조)
INSERT INTO `floating`.`user` (`id`, `email`, `pw`, `name`, `birth`, `gender`, `height`, `weight`, `mbti_pk`) VALUES
('user1', 'user1@example.com', 'hashed_pw_1', '김철수', '1995-05-15', 'M', 175, 70, 1),
('user2', 'user2@example.com', 'hashed_pw_2', '이영희', '1998-02-28', 'F', 163, 55, 2),
('user3', 'user3@example.com', 'hashed_pw_3', '박민수', '2001-11-10', 'M', 180, 80, 3),
('user4', 'user4@example.com', 'hashed_pw_4', '최지은', '1992-07-01', 'F', 170, 60, 4),
('user5', 'user5@example.com', 'hashed_pw_5', '정우성', '1988-03-20', 'M', 178, 75, 5),
('user6', 'user6@example.com', 'hashed_pw_6', '한소희', '1999-09-09', 'F', 168, 52, 6),
('user7', 'user7@example.com', 'hashed_pw_7', '강하늘', '1994-12-05', 'M', 183, 77, 7),
('user8', 'user8@example.com', 'hashed_pw_8', '윤아라', '2000-06-25', 'F', 165, 58, 8),
('user9', 'user9@example.com', 'hashed_pw_9', '서준영', '1997-04-18', 'M', 176, 68, 9),
('user10', 'user10@example.com', 'hashed_pw_10', '임나연', '1996-01-30', 'F', 160, 50, 10);

-- 3. plan 테이블 더미 데이터 (user_id 참조)
INSERT INTO `floating`.`plan` (`date`, `category`, `detail`, `time`, `complete_date`, `shifted`, `user_id`) VALUES
('2025-12-15', '운동', '오전 조깅 30분', 30, '2025-12-15', 0, 'user1'),
('2025-12-15', '공부', 'Vue3 컴포넌트 개발', 120, '2025-12-15', 0, 'user2'),
('2025-12-16', '업무', 'DB 스키마 리뷰', 60, NULL, 1, 'user3'),
('2025-12-16', '취미', '독서 1시간', 60, '2025-12-16', 0, 'user4'),
('2025-12-17', '운동', '헬스장 가슴 운동', 90, NULL, 0, 'user5'),
('2025-12-17', '공부', 'MSA 아키텍처 강의 시청', 90, '2025-12-17', 0, 'user6'),
('2025-12-18', '업무', '백엔드 API 개발', 180, NULL, 0, 'user7'),
('2025-12-18', '생활', '은행 업무 처리', 40, '2025-12-18', 0, 'user8'),
('2025-12-19', '운동', '요가 클래스 참여', 75, '2025-12-19', 0, 'user9'),
('2025-12-19', '취미', '프랑스어 단어 암기', 45, NULL, 1, 'user10');

INSERT INTO `floating`.`plan` (`date`, `category`, `detail`, `time`, `complete_date`, `shifted`, `user_id`) VALUES
('2025-12-16', '운동', '요가', 30, NULL, 0, 'ssafy');

select * from plan;
select * from pet;
INSERT INTO pet(score, user_id)
VALUES (0, 'ssafy');

UPDATE pet
SET 
	score = 330
WHERE pet_pk = 16;

-- 4. review 테이블 더미 데이터 (plan_pk 참조)
INSERT INTO `floating`.`review` (`content`, `plan_pk`) VALUES
('오늘 조깅은 상쾌했지만 조금 부족했다.', 6),
('Vue 개발 완료! 에러 없이 잘 돌아가서 뿌듯하다.', 7),
('DB 스키마가 복잡해서 다음 주로 미뤘다. 반성!', 8),
('읽고 싶었던 책 한 권을 완독했다. 만족.', 9),
('헬스장 가는 게 귀찮아서 결국 못 갔다.', 10),
('MSA 강의는 내용이 알찼고 이해가 잘 되었다.', 11),
('API 개발 중 예외 처리 부분에서 막혔다.', 12),
('은행 업무 빠르게 처리 완료!', 13),
('요가로 몸이 개운해졌다. 규칙적으로 해야겠다.', 14),
('프랑스어 단어 암기 시도도 못 함. 내일 꼭 해야지.', 15);

select * from review;
select * from img;

INSERT INTO `floating`.`img` (`img_path`, `review_pk`) VALUES
('/images/jogging_proof.jpg', 29),
('/images/vue_success.png', 28),
('/images/db_problem.png', 28),
('/images/book_cover.jpg', 27),
('/images/failed_gym.jpg', 27),
('/images/msa_notes.pdf', 30),
('/images/api_error.log', 31),
('/images/bank_slip.png', 31),
('/images/yoga_mat.jpg', 32),
('/images/french_book_start.jpg', 32);

-- 6. pet 테이블 더미 데이터 (user_id 참조)
INSERT INTO `floating`.`pet` (`score`, `user_id`) VALUES
(85, 'user1'),
(92, 'user2'),
(55, 'user3'),
(78, 'user4'),
(40, 'user5'),
(95, 'user6'),
(65, 'user7'),
(88, 'user8'),
(72, 'user9'),
(50, 'user10');

-- 7. wbti 테이블 더미 데이터 (user_id 참조)
INSERT INTO `floating`.`wbti` (`social_type`, `motivation_type`, `execution_type`, `activity_type`, `user_id`) VALUES
(4, 3, 5, 2, 'user1'),
(5, 4, 3, 4, 'user2'),
(2, 5, 1, 3, 'user3'),
(3, 3, 4, 5, 'user4'),
(1, 2, 2, 1, 'user5'),
(5, 5, 5, 5, 'user6'),
(4, 1, 3, 2, 'user7'),
(3, 4, 4, 4, 'user8'),
(2, 3, 5, 3, 'user9'),
(4, 2, 1, 5, 'user10');