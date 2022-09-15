package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    final private EntityManager em;

    List<Member> findMember() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
