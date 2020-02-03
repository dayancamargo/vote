package com.test.meeting.model.agenda;

import com.test.meeting.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class AgendaRequest extends BaseModel {
    private String name;
}
