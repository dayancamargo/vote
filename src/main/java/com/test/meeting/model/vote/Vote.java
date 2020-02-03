package com.test.meeting.model.vote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.meeting.model.BaseModel;
import com.test.meeting.model.election.Election;
import com.test.meeting.model.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter @EqualsAndHashCode(of = {"id", "election", "user"})
@Entity @Table(name = "Vote", uniqueConstraints = @UniqueConstraint(name = "VOTE_UK", columnNames = {"id_election", "id_user"}))
public class Vote extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote")
    private VoteEnum vote;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="id_election")
    private Election election;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="id_user")
    private User user;

    @NotNull
    @Column(name="created_at")
    private LocalDateTime createdAt;

    public Vote() {
        createdAt = LocalDateTime.now();
    }
}
