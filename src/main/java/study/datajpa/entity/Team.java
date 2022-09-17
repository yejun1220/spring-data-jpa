package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@ToString(of = {"id", "teamName"})
@NoArgsConstructor(access = PROTECTED)
public class Team extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String teamName;

    public Team(String teamName) {
        this.teamName = teamName;
    }

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
}
