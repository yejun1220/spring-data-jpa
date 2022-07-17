package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTestBy();

    List<Member> findTop2By();

//    @Query(name = "Member.findByUsername") 주석 처리해도 가능
    List<Member> findByUsername(@Param("username") String username);
}
