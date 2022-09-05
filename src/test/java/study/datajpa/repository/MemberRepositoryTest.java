package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> byId = memberRepository.findById(member.getId());


        if(byId.isPresent()) {
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
}
