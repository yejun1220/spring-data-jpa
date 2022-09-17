# 스프링 데이터 JPA 분석 메모 사항들

## 새로운 엔티티를 구별하는 방법
- Spring Data Jpa의 save는 식별자가 없으면 새로운 엔티티로 인식한다.
- 직접 PK를 할당하는 경우 (String으로 직접 입력 등) merge가 호출 되면서 성능이 덜 나온다.
- 따라서 Persistable 을 구현하여 isNew를 재정의 해줘야 한다.
