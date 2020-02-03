package com.test.meeting.model.user;

import com.test.meeting.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Getter
public class UserRequest extends BaseModel {
    private String cpf;
}
