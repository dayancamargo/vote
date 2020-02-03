package com.test.meeting.model.user;

import com.test.meeting.model.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter @EqualsAndHashCode(of = {"id","cpf"})
@Entity @Table(name = "User", uniqueConstraints = @UniqueConstraint(name = "CPF_UK", columnNames = "cpf"))
public class User extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name="cpf", length = 11)
    @CPF
    private String cpf;

    @NotNull
    @Column(name="created_at")
    private LocalDateTime createdAt;

    public User() {
        createdAt = LocalDateTime.now();
    }
}
