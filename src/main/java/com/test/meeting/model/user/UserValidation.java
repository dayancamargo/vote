package com.test.meeting.model.user;

import com.test.meeting.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class UserValidation extends BaseModel {
    String status;
}
