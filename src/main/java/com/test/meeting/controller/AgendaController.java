package com.test.meeting.controller;

import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.agenda.AgendaRequest;
import com.test.meeting.model.response.Response;
import com.test.meeting.service.AgendaService;
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
@RequestMapping("agenda")
@Slf4j
@Api(value = "API com operações de pauta", tags = "Pauta")
public class AgendaController {

    private AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @ApiOperation(value = "Criar uma nova pauta")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@Valid @RequestBody AgendaRequest agendaRequest) {
        log.debug("Creating: {}", agendaRequest);
        agendaService.save(agendaRequest);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Agenda> findAll(Pageable page) {
        log.debug("Finding with: {}", page);
        return Response.build()
                       .withPagination(agendaService.findAll(page))
                       .create();
    }

}
