# 프로젝트 환경설정 메모 사항들

## em.flush()
### flush()란
- 영속성 컨텍스트의 변경 내용을 DB 에 반영하는 것
- 영속성 컨텍스트의 객체들을 지우는 것이 아니다.(= em.clear())
- 쓰기 지연 저장소에 들어있던 `SQL문 실행`

### flush 하는법
- `em.flush`
- `SPQL문 실행`
- `트랜젝션.commit` (테스트가 끝나도 실행 된다.)
- `em.find()`는 flush되지 않는다.
```java
em.persist(member);
em.flush();
System.out.println("==========");
member.setUsername("afterName");
List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
```
flush로 인한 insert 문 -> sout -> JPQL로 인한 update 문 -> JPQL로 인한 select 문 

## em.clear()
- 영속성 컨텍스트의 모든 것을 비운다.
- 준영속 상태로 만듦.
- 쓰기 지연 저장소도 비운다.
```java
Member member1 = new Member("beforeName");
em.clear();
member.changeName("afterName");
```
member를 미리 선언했어도 준영속 상태가 되었으므로 update문(더티체킹)이 나가지 않는다.

## 지연 로딩 및 즉시 로딩
**로딩**: 영속성 컨텍스트에 없어서 객체를 생성하는 것이다.  
지연로딩이든 즉시로딩이든 한 엔티티를 가져오면 연관 관계가 있는 엔티티를 무조건 가져온다.(**영속성 컨텍스트에 저장**)  
그 가져오는 엔티티가 프록시냐, 아니랴로 로딩이 달라진다.  

**지연로딩**: 연관관계가 있는 엔티티는 프록시로 가져온다.  
그 프록시 객체에서 데이터를 조회해도 프록시다.(실제 객체를 가리킴)  
실제 그 엔티티를 불러오면 프록시가 아니게 된다.(실제 객체로 바뀜)  
`em.find()`도 실제 객체를 불러온다.

**즉시로딩**: 연관관계가 있는 엔티티에 실제 엔티티를 대입한다.  
-> select문이 나간다. 1차 캐시에 있으면 select문이 안나간다.