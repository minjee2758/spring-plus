# SPRING PLUS
해당 프로젝트는 기존의 코드를 개선하는 과정을 담았습니다

## 💾 Level 1-1 : `@Transactional` vs `@Transactional(readOnly = true)`
`@Transactional(readOnly = true)`

➡ 엔티티를 수정하는 로직이 있을 경우, lushMode가 MANUAL로 설정되어 더티 체킹에 의한 자동적인 데이터베이스 반영이 일어나지 않게 된다. 

➡️ 데이터 충돌이 일어날 수 있기때문에 데이터를 수정하는 서비스에 적용하면 안된다!

때문에 "할 일 저장 기능"에서 정상적으로 데이터가 변할 수 있도록 `@Transactional`을 적용한다

---

## 🔐 Level 1-2 : JWT 이해하기
`JwtUtil`은 토큰을 만들어주는(발급) 로직이 포함되어있다.

원하는 정보를 `.claim()`를 통해 담아줄 수 있다

<img width="650" alt="image" src="https://github.com/user-attachments/assets/a555aa51-d215-4142-8c2b-d229cd40bcb0" />

---

## ☁️ Level 1-3 : JPQL을 이용해서 다양한 상황 고려하기
<img width="1225" alt="image" src="https://github.com/user-attachments/assets/6af60df9-33c7-4396-afe2-3d90acc4969d" />

기존 JPA 코드에서 `@Query` 메서드를 사용하므로써 N+1문제를 해결함과 동시에, DB에서 원하는 데이터를 쉽고 가독성 높게 가져올 수 있다

---

## 🕹️ Level 1-4 : 컨트롤러 레이어의 테스트 코드
🔗  <a href = "https://velog.io/@minjee2758/SPRING-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EB%8B%A8%EC%9C%84-%EC%8A%AC%EB%9D%BC%EC%9D%B4%EC%8A%A4-%ED%86%B5%ED%95%A9-%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%86%BA%EC%95%84%EB%B3%B4%EA%B8%B0"> 테스트코드 개념정리 블로그 링크 </a>

<img width="1704" alt="image" src="https://github.com/user-attachments/assets/987793b3-7009-4aca-b8dd-d6d114fe1f53" />

기존 테스트코드에서 오류가 났던 이유
: 설계된 컨트롤러의 의도와 다르게 테스트코드가 짜여졌기 때문 (실패가 나와야하는데, 성공했다고 모킹하고 있음)
→ 상태 코드를 `BAD_REQUEST`로 수정

---

## 🧈 Level 1-5 : AOP 이해하기
🔗  <a href = "https://velog.io/@minjee2758/SPRING-%EB%AA%A8%EB%A5%B4%EB%8A%94-%EA%B0%9C%EB%85%90-%EC%A0%95%EB%A6%AC-AOP-RequestParam-Paging"> AOP 개념정리 블로그 링크 </a>

`@AOP`의 어노테이션을 통해 언제 접속 로그를 남길건지 지정함

→ 사용자의 역할을 바꾸는걸 실패해도 로그가 남아야하므로, `@Before`를 통해 실행되기 전에 로그가 남도록 수정함

---

## ⛓️ Level 2-6 : JPA Cascade
할 일을 새로 저장할 시, 할 일(todo)을 생성한 유저는 담당자(manager)로 자동 등록되어야 한다

`CascadeType.ALL` vs `CascadeType.PERSIST` 를 구분해서 잘 사용해야한다

🔗  <a href = "https://velog.io/@minjee2758/SPRING-%EB%AA%A8%EB%A5%B4%EB%8A%94-%EA%B0%9C%EB%85%90-%EC%A0%95%EB%A6%AC-AOP-RequestParam-Paging](https://velog.io/@minjee2758/SPRING-CascadeType-%EA%B5%AC%EB%B6%84%ED%95%B4%EC%84%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0"> CascadeType 개념정리 블로그 링크 </a>

간단버전
- ALL : 모든 변경사항이 자식에게 전파된다. (원치않는 전파가 이루어질 수 있음 조심해야함)
- PERSIST : 저장만 전파된다

CASCADE는 원하는 범위에 맞추어 딱맞게 사용해야 설계에 지장이 없다

---

## 🅽 Level 2-7 : N+1 문제 해결하기
엔티티를 조회할 때 성능 저하를 일으키는 n+1 문제를 해결 `@EntityGraph` & `Fetch Join`

<img width="800" alt="image" src="https://github.com/user-attachments/assets/cab2d1a4-74e2-41db-a240-f62d8be653b6" />

---

## 🔁 Level 2-8 : JPQL에서 QueryDSL로 바꾸기
QueryDSL을 사용하면 컴파일 시점에 에러를 발견할 수 있다!!

<img width="800" alt="image" src="https://github.com/user-attachments/assets/0ffb5d1a-128b-4cc0-b5b5-45534a9923d5" />


N+1문제가 발생하지 않도록 `fetchJoin`까지 신경쓰자

---

## 👮‍♀️ Level 2-9 : Spring Security 도입하기
기존 `JwtFilter`에서 `JwtAuthFilter`로 변경(OncePerRequestFilter 상속)

`SecurityConfig` 에서 인증 인가 URL 설정

🔗  <a href = "https://velog.io/@minjee2758/SPRING-Spring-Security-JWT%EB%A1%9C-%EC%9D%B8%EC%A6%9D-%EC%9D%B8%EA%B0%80-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0"> Spring Security 개념정리 블로그 링크 </a>

---

## 🔎 Level 3-10 : QueryDSL 을 사용하여 검색 기능 만들기
`BooleanBuilder builder = new BooleanBuilder();`를 통해 쿼리문을 붙여줌

---

## 🙆 Level 3-11 : 매니저 등록 요청을 기록하는 로그 테이블을 만들기
<img width="696" alt="image" src="https://github.com/user-attachments/assets/15d9c303-9c4f-4c79-8f67-e12cca9b5403" />
접근한 사람을 저장하는 Log 테이블을 생성해서 매니저 등록을 요청하는 사람들을 저장한다

`@Transactional(propagation = Propagation.REQUIRES_NEW)` = 부모의 트랜잭션과는 독립적으로 작동!

➡️ 등록은 실패할 수 있지만, 로그는 반드시 저장되도록 설정
