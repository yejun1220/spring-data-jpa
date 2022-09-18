package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> byId = memberRepository.findById(member.getId());


        if (byId.isPresent()) {
            Member findMember = byId.get();
            assertThat(findMember.getId()).isEqualTo(savedMember.getId());
            assertThat(findMember).isEqualTo(savedMember);
        }
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member1.getId()).get();
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    // 메서드 이름으로 쿼리 생성
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("A", 10);
        Member m2 = new Member("B", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("B", 10);

        assertThat(result.get(0).getUsername()).isEqualTo("B");
    }

    @Test
    // 전체 조회
    public void findTestBy() {
        Member m1 = new Member("A", 10);
        Member m2 = new Member("B", 20);
        Member m3 = new Member("C", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        List<Member> result = memberRepository.findTestBy();

        assertThat(result.size()).isEqualTo(3);

        List<Member> result2 = memberRepository.findTop2By();
        assertThat(result.get(1).getUsername()).isEqualTo("B");
    }

    @Test
    // JPA namedQuery 사용
    public void namedQuery() {
        Member m1 = new Member("A", 10);
        Member m2 = new Member("B", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("A");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    // @Query 사용
    public void testQuery() {
        Member m1 = new Member("A", 10);
        Member m2 = new Member("B", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("A");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    // @Query 사용
    public void findUsernameList() {
        Member m1 = new Member("A", 10);
        Member m2 = new Member("B", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findByUsernameList();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    // DTO 조회하기
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("A", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findByUsernameDtoList();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    // @Query 사용
    public void findByNames() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AA", "BB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    // @Query 사용
    public void returnType() {
        Member m1 = new Member("AA", 10);
        Member m2 = new Member("BB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result1 = memberRepository.findListByUsername("AA");
        System.out.println("result1 = " + result1);

        Member result2 = memberRepository.findMemberByUsername("AA");
        System.out.println("result2 = " + result2);

        Optional<Member> result3 = memberRepository.findOptionalByUsername("AA");
        System.out.println("result3 = " + result3.get());

        // 반환 타입이 list인 경우, 없는 값을 조회하면 빈 리스트를 반환해준다.(에러 발생 X)
        List<Member> result4 = memberRepository.findListByUsername("C");
        System.out.println("result4 = " + result4);

        // 반환 타입이 list가 아닌 경우, 없는 값을 조회하면 null을 반환해준다.(에러 발생 X)
        // 원래는 에러가 발생하나 Spring Data Jpa 가 try catch로 null을 반환해준다.
        Member result5 = memberRepository.findMemberByUsername("C");
        System.out.println("result5 = " + result5);
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("A", 10));
        memberRepository.save(new Member("B", 10));
        memberRepository.save(new Member("C", 10));
        memberRepository.save(new Member("D", 10));
        memberRepository.save(new Member("E", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> slice = memberRepository.findAgeSlice(age, pageRequest);

        // then
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        // 페이징 안의 요소 갯수
        System.out.println(content.size());

        // 현재 Paging한 페이지 인덱스
        System.out.println(page.getNumber());

        // 첫번째 페이지인지 확인
        System.out.println(page.isFirst());

        // 다음 페이지 있는지 확인
        System.out.println(page.hasNext());

        // Page 갯수
        System.out.println(page.getTotalPages());

        // Page 상관없이 전체 요소 갯수
        System.out.println(page.getTotalElements());

        // slice 이용하여 가져오기
//        slice.getContent();
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("A", 10));
        memberRepository.save(new Member("B", 20));
        memberRepository.save(new Member("C", 30));
        memberRepository.save(new Member("D", 40));
        memberRepository.save(new Member("E", 50));

        int age = 20;

        // when
        int resultCount = memberRepository.bulkAgePlus(age);

        em.flush();
        em.clear();
        List<Member> member = memberRepository.findByUsername("B");
        Member findMember = member.get(0);

        // then
        assertThat(resultCount).isEqualTo(4);
        assertThat(findMember.getAge()).isEqualTo(21);
    }

    @Test
    public void findMemberLazy() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when
        List<Member> all = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : all) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getTeamName() = " + member.getTeam().getTeamName());
        }
    }

    @Test
    public void queryHint() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        // hint로 readOnly를 설정해주었다.
        Member findMember = memberRepository.findReadOnlyByUsername("member1");

        // then
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        // hint로 readOnly를 설정해주었다.
        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
        memberQueryRepository.findMember();
    }

    @Test
    public void projections() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly);
        }
    }
}
