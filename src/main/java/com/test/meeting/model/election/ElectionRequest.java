package com.test.meeting.model.election;

import com.test.meeting.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Getter
public class ElectionRequest extends BaseModel {

    private Long idAgenda;
    private Long duration;
}
