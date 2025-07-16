-- ===== 카테고리별 문제 데이터 (각 카테고리당 10문제) =====

-- DBMS 카테고리 문제들 (10문제)
-- 1. DBMS 주요 기능 (이미 추가됨)

-- 2. DBMS 유형
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(3, 'DBMS 유형', '관계형 DBMS가 아닌 것은?', 'MULTIPLE_CHOICE', 'EASY', 10, 'MongoDB는 NoSQL 데이터베이스로 문서 지향 데이터베이스입니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(24, 'MySQL', false, 1),
(24, 'PostgreSQL', false, 2),
(24, 'MongoDB', true, 3),
(24, 'Oracle', false, 4);

-- 3. 데이터 독립성
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(3, '데이터 독립성', '논리적 데이터 독립성에 대한 설명으로 옳은 것은?', 'MULTIPLE_CHOICE', 'MEDIUM', 15, '논리적 데이터 독립성은 논리적 구조 변경이 응용프로그램에 영향을 주지 않는 것입니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(25, '물리적 구조 변경이 논리적 구조에 영향을 주지 않음', false, 1),
(25, '논리적 구조 변경이 응용프로그램에 영향을 주지 않음', true, 2),
(25, '데이터 저장 위치 변경이 불가능함', false, 3),
(25, '스키마 변경이 완전히 불가능함', false, 4);

-- 4. 데이터베이스 스키마
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(3, '스키마 레벨', '3단계 스키마 구조에서 외부 스키마의 역할은?', 'MULTIPLE_CHOICE', 'MEDIUM', 15, '외부 스키마는 사용자나 응용프로그램이 접근하는 데이터베이스의 논리적 구조입니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(26, '물리적 저장 구조 정의', false, 1),
(26, '사용자 관점의 논리적 구조 정의', true, 2),
(26, '전체 데이터베이스의 논리적 구조 정의', false, 3),
(26, '하드웨어 구성 정의', false, 4);

-- 5. 데이터 모델
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(3, '데이터 모델', '계층형 데이터 모델의 특징으로 옳은 것은?', 'MULTIPLE_