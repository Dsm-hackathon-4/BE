-- 카테고리 추가
INSERT INTO tbl_category (name, description, icon_url) VALUES
('네트워크', '네트워크 프로토콜, 통신 관련 문제', 'network-icon.png'),
('프로그래밍', '프로그래밍 언어, 개발 관련 문제', 'programming-icon.png');

-- 1. 기본 객관식 (MULTIPLE_CHOICE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(1, 'TCP와 UDP의 차이점', 'TCP와 UDP 프로토콜의 가장 큰 차이점은 무엇인가요?', 'MULTIPLE_CHOICE', 'EASY', 10, 'TCP는 연결지향적이고 신뢰성을 보장하지만, UDP는 비연결지향적이고 빠른 전송에 중점을 둡니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(1, 'TCP는 연결지향, UDP는 비연결지향', true, 1),
(1, 'TCP는 비연결지향, UDP는 연결지향', false, 2),
(1, '둘 다 연결지향 프로토콜이다', false, 3),
(1, '둘 다 비연결지향 프로토콜이다', false, 4);

-- 2. 초성 객관식 (INITIAL_CHOICE) - 안드로이드
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation, hint) VALUES
(2, '초성 맞추기', 'ㅇ ㄷ ㄹ ㅇ ㄷ', 'INITIAL_CHOICE', 'EASY', 10, '구글에서 개발한 모바일 운영체제입니다.', '스마트폰 OS');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(2, '안드로이드', true, 1),
(2, '아이폰OS', false, 2),
(2, '윈도우폰', false, 3),
(2, '타이젠', false, 4);

-- 3. 빈칸 객관식 (BLANK_CHOICE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(1, '빈칸 채우기', 'HTTP는 _______ 프로토콜이다.', 'BLANK_CHOICE', 'MEDIUM', 15, 'HTTP는 Hypertext Transfer Protocol의 약자로 웹에서 사용되는 프로토콜입니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(3, '웹 전송', true, 1),
(3, '파일 전송', false, 2),
(3, '메일 전송', false, 3),
(3, '음성 전송', false, 4);

-- 4. 단어 객관식 (WORD_CHOICE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(2, '단어 선택', '다음 중 데이터베이스 관리 시스템(DBMS)이 아닌 것은?', 'WORD_CHOICE', 'MEDIUM', 15, 'HTML은 마크업 언어이지 데이터베이스 관리 시스템이 아닙니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(4, 'HTML', true, 1),
(4, 'MySQL', false, 2),
(4, 'PostgreSQL', false, 3),
(4, 'Oracle', false, 4);

-- 5. OX 객관식 (OX_CHOICE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(1, 'OX 퀴즈', 'TCP는 연결지향 프로토콜이다.', 'OX_CHOICE', 'EASY', 5, 'TCP는 연결을 설정한 후 데이터를 전송하는 연결지향 프로토콜입니다.');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(5, 'O', true, 1),
(5, 'X', false, 2);

-- 6. 이미지 객관식 (IMAGE_CHOICE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation, image_url) VALUES
(2, '로고 맞추기', '다음 로고는 어떤 회사의 것인가요?', 'IMAGE_CHOICE', 'EASY', 10, '구글의 로고입니다.', 'https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(6, '구글', true, 1),
(6, '애플', false, 2),
(6, '마이크로소프트', false, 3),
(6, '아마존', false, 4);

-- 7. 기본 주관식 (SUBJECTIVE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(1, 'HTTP 정식명칭', 'HTTP의 정식 명칭을 영어로 작성하세요.', 'SUBJECTIVE', 'MEDIUM', 15, 'Hypertext Transfer Protocol입니다.');

INSERT INTO tbl_subjective_answer (problem_id, correct_answer, keywords) VALUES
(7, 'Hypertext Transfer Protocol', 'hypertext,transfer,protocol,HTTP');

-- 8. 초성 주관식 (INITIAL_SUBJECTIVE) - 자바스크립트 (ㅈㅂㅅㅋㄹㅌ)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation, hint) VALUES
(2, '초성 주관식', 'ㅈ ㅂ ㅅ ㅋ ㄹ ㅌ', 'INITIAL_SUBJECTIVE', 'MEDIUM', 15, '웹 개발에 사용되는 프로그래밍 언어입니다.', '웹 프로그래밍 언어');

INSERT INTO tbl_subjective_answer (problem_id, correct_answer, keywords) VALUES
(8, '자바스크립트', '자바스크립트,javascript,JS');

-- 9. 빈칸 주관식 (BLANK_SUBJECTIVE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(1, '빈칸 주관식', 'SQL에서 데이터를 조회할 때 사용하는 명령어는 _______ 입니다.', 'BLANK_SUBJECTIVE', 'EASY', 10, 'SELECT 명령어를 사용하여 데이터를 조회합니다.');

INSERT INTO tbl_subjective_answer (problem_id, correct_answer, keywords) VALUES
(9, 'SELECT', 'select,Select');

-- 10. 단어 주관식 (WORD_SUBJECTIVE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation) VALUES
(2, '단어 주관식', '웹페이지의 구조를 정의하는 마크업 언어의 이름을 작성하세요.', 'WORD_SUBJECTIVE', 'EASY', 10, 'HTML(Hypertext Markup Language)입니다.');

INSERT INTO tbl_subjective_answer (problem_id, correct_answer, keywords) VALUES
(10, 'HTML', 'html,Hypertext Markup Language');

-- 11. 이미지 주관식 (IMAGE_SUBJECTIVE)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation, image_url) VALUES
(2, '이미지 주관식', '다음 이미지에 나타난 프로그래밍 언어의 이름을 작성하세요.', 'IMAGE_SUBJECTIVE', 'MEDIUM', 15, 'Python 언어의 로고입니다.', 'https://upload.wikimedia.org/wikipedia/commons/c/c3/Python-logo-notext.svg');

INSERT INTO tbl_subjective_answer (problem_id, correct_answer, keywords) VALUES
(11, 'Python', 'python,파이썬,Python');

-- 추가 초성 문제들
-- 12. 파이썬 (ㅍㅇㅆ)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation, hint) VALUES
(2, '초성 맞추기2', 'ㅍ ㅇ ㅆ', 'INITIAL_CHOICE', 'EASY', 10, '인공지능과 데이터 과학에 많이 사용되는 프로그래밍 언어입니다.', '뱀 이름의 언어');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(12, '파이썬', true, 1),
(12, '펄', false, 2),
(12, '포트란', false, 3),
(12, '프롤로그', false, 4);

-- 13. 깃허브 (ㄱㅎㅂ)
INSERT INTO tbl_problem (category_id, title, content, type, difficulty, xp_reward, explanation, hint) VALUES
(2, '초성 맞추기3', 'ㄱ ㅎ ㅂ', 'INITIAL_CHOICE', 'EASY', 10, '개발자들이 코드를 공유하고 협업하는 플랫폼입니다.', '버전 관리 플랫폼');

INSERT INTO tbl_choice (problem_id, content, is_correct, order_index) VALUES
(13, '깃허브', true, 1),
(13, '구글', false, 2),
(13, '깃랩', false, 3),
(13, '그래들', false, 4);
