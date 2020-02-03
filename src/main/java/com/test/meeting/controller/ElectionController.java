package com.test.meeting.controller;

import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.election.ElectionRequest;
import com.test.meeting.model.response.Response;
import com.test.meeting.service.ElectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController()
@RequestMapping("election")
@Slf4j
@Api(value = "API com operações de eleicao", tags = "Eleicao")
public class ElectionController {

    private ElectionService electionService;

    @Autowired
    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @ApiOperation(value = "Criar uma nova eleicao")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@Valid @RequestBody ElectionRequest electionRequest) {
        log.debug("Creating: {}", electionRequest);
        electionService.save(electionRequest);
    }

    @ApiOperation(value = "Buscar todas eleicoes aptas para votação")
    @GetMapping(value ="valid" ,produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Agenda> findAllValid(Pageable page) {
        log.debug("Finding with: {}", page);
        return Response.build()
                .withPagination(electionService.findAllValid(page))
                .create();
    }

    @ApiOperation(value = "Buscar todas eleicoes")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Agenda> findAll(Pageable page) {
        log.debug("Finding with: {}", page);
        return Response.build()
                       .withPagination(electionService.findAll(page))
                       .create();
    }
}
