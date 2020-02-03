package com.test.meeting.controller;

import com.test.meeting.model.response.Response;
import com.test.meeting.model.user.User;
import com.test.meeting.model.vote.VoteRequest;
import com.test.meeting.model.vote.VoteSummary;
import com.test.meeting.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController()
@RequestMapping("vote")
@Slf4j
@Api(value = "API com operações de votacao", tags = "Votacao")
public class VoteController {

    private VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @ApiOperation(value = "Vote in a election")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@Valid @RequestBody VoteRequest request) {
        log.debug("Voting: {}", request);
        voteService.save(request);
    }

    @ApiOperation(value = "Buscar resultados da votacao de uma eleicao")
    @GetMapping(value = "result/{idElection}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Response<VoteSummary> findByCpf(@PathVariable("idElection") Long idElection ) {
        log.debug("Finding with: {}", idElection);
        return Response.build()
                .withBody(voteService.getVoteResult(idElection))
                .create();
    }
}
