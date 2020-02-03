package com.test.meeting.model.agenda;

import com.test.meeting.model.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter @EqualsAndHashCode(of = {"id", "name"})
@Entity @Table(name = "AGENDA")
public class Agenda extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name="name", length = 50)
    private String name;

    @NotNull
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
