package com.test.meeting.model.vote;

import com.test.meeting.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor @AllArgsConstructor
@Getter
public class VoteRequest extends BaseModel {

    @NotNull
    private Long idElection;
    @NotBlank
    private String cpf;
    @NotNull
    private VoteEnum vote;
}
