# 프로젝트 환경설정 메모 사항들

## @RestController
### @RestController 란
@RestController는 @Controller와 @ResponseBody를 합친 애노테이션이다.  
@ResponseBody는 view 조회가 아닌 HTTP 메시지 정보를 직접 반환할 때 쓰인다.

## ddl-auto
### ddl-auto 옵션 종류
- `create`: 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
- `create-drop`: create와 같으나 종료시점에 테이블 DROP
- `update`: 변경분만 반영
- `validate`: 엔티티와 테이블이 정상 매핑되었는지만 확인
- `none`: 사용하지 않음(사실상 없는 값이지만 관례상 none이라고 한다.)
## 주의할 점
- 운영 장비에서는 절대 crate, create-drop, update 사용하면 안된다.
- 개발 초기 단계는 create 또는 update
- 테스트 서버는 update 또는 validate
- 스테이징과 운영 서버는 validate 또는 none

## @PersistenceContext
- JPA 스펙에서 제공하는 기능
- `@Autowired`처럼 영속성 컨텍스트를 주입해주는 표준 애노테이션
- EntityManagerFactory 생성 등을 대신 해준다.

## @Transactional
- 스프링은 `@Transactional` 애노테이션을 이용한 선언적 트랜잭션 처리를 지원한다.

### 트랜잭션(Transaction)이란?

데이터베이스 관리 시스템 또는 유사한 시스템에서 상호작용의 단위이다.  
상호작용의 단위: 더 이상 쪼개질 수 없는 최소의 연산  
데이터베이스의 데이터를 다루 때(변경이 있을 때) 오류가 발생하면 그 전의 상태(하나의 단위)로 돌리기 위함이다.(`Rollback`)
- Spring Test에서 트렌젝션은 기본적으로 롤백을 시킨다.(=Sql문이 나가지 않는다.)