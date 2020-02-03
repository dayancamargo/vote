package com.test.meeting.client;

import com.test.meeting.model.user.UserValidation;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Component;

@Component
public interface ValidatorClient {

    @RequestLine("GET /users/{cpf}")
    UserValidation findByCpf(@Param(value = "cpf") String cpf);
}
