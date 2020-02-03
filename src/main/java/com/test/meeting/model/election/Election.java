package com.test.meeting.model.election;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.meeting.model.BaseModel;
import com.test.meeting.model.agenda.Agenda;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @EqualsAndHashCode(of = {"id", "agenda"})
@Entity @Table(name = "ELECTION")
public class Election extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore @NotNull
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="id_agenda")
    private Agenda agenda;

    @NotNull
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name="expire_at")
    private LocalDateTime expireAt;

    public Election() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Returns if agenda is expired
     * @return true if expired or null
     */
    public boolean isExired() {
        return Objects.isNull(expireAt) || expireAt.isBefore(LocalDateTime.now());
    }
}
