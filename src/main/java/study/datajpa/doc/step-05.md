# 확장 기능 메모 사항들

## 사용자 정의 리포지토리 구현
- Spring Data Jpa는 interface이므로 custom 메서드(Jdbc, queryDsl 등)를 만드려면 상속을 받아야 한다.
- interface `모두 구현`해야 한다.
- JpaRepository와 같이 상속 받으면 된다.
- 상속받은 인터페이스를 따로 구현해준다.
- `CQS` : Command Query Separation -> command : 객체 내부의 상태를 바꾸고 반환 X, Query : 객체 내부의 상태를 바꾸지 않고 반환 O