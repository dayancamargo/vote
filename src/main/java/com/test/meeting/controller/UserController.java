package com.test.meeting.controller;

import com.test.meeting.model.response.Response;
import com.test.meeting.model.user.User;
import com.test.meeting.model.user.UserRequest;
import com.test.meeting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController()
@RequestMapping("user")
@Slf4j
@Api(value = "API com operações de usuario", tags = "Usuario")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Criar um novo usuario")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@Valid @RequestBody UserRequest electionRequest) {
        log.debug("Creating: {}", electionRequest);
        userService.save(electionRequest);
    }

    @ApiOperation(value = "Buscar todos Usuarios")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<User> findAll(Pageable page) {
        log.debug("Finding with: {}", page);
        return Response.build()
                       .withPagination(userService.findAll(page))
                       .create();
    }

    @ApiOperation(value = "Buscar um usuario por cpf")
    @GetMapping(value = "/{cpf}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<User> findByCpf(@PathVariable("cpf") String cpf ) {
        log.debug("Finding with: {}", cpf);
        return Response.build()
                .withBody(userService.findByCpf(cpf))
                .create();
    }

}
