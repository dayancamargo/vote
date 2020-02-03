package com.test.meeting.service;

import com.test.meeting.client.ValidatorClient;
import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.user.UserValidation;
import com.test.meeting.model.election.Election;
import com.test.meeting.model.user.User;
import com.test.meeting.model.user.UserRequest;
import com.test.meeting.model.vote.Vote;
import com.test.meeting.model.vote.VoteRequest;
import com.test.meeting.model.vote.VoteSummary;
import com.test.meeting.repository.VoteRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j @Transactional
public class VoteService {

    private UserService userService;
    private VoteRepository voteRepository;
    private ElectionService electionService;
    private ValidatorClient validatorClient;

    @Autowired
    public VoteService(UserService userService, VoteRepository voteRepository, ElectionService electionService,ValidatorClient validatorClient) {
        this.userService = userService;
        this.voteRepository = voteRepository;
        this.electionService = electionService;
        this.validatorClient = validatorClient;
    }

    public Vote save(VoteRequest request) {

        if(Objects.isNull(request)){
            throw  new ValidationException("VoteRequest could not be null.");
        }

        Vote vote = new Vote();
        vote.setVote(request.getVote());
        vote.setElection(getElection(request.getIdElection()));
        vote.setUser(getUser(request.getCpf()));

        log.debug("saving: {}", vote);

        try {
            return voteRepository.saveAndFlush(vote);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            throw new ValidationException("Cpf already voted on this election.");
        }
    }

    public Page<Vote> findAll(Pageable page) {
        log.debug("Find with: {}", page);
        return voteRepository.findAll(page);
    }

    /**
     * Get vote summary from a given election
     */
    public List<VoteSummary> getVoteResult(Long idElection) {
        log.debug("Find with: {}", idElection);
        List result = voteRepository.getVoteResult(idElection);

        if(!CollectionUtils.isEmpty(result))
            return result;
        else
            throw new NoDataFoundException("Cannot found results for this election.");
    }

    /**
     * Get election by id and validate if it not expired
     * @param id
     */
    private Election getElection(Long id){
        Election election = electionService.findById(id);

        if(election.isExired()) {
            throw new ValidationException(String.format("This election is expired (valid: %s)", election.getExpireAt()));
        }
        return election;
    }

    /**
     * Get a user by cpf, if not exists in DB try create a new one;
     * @param cpf
     */
    private User getUser(String cpf) {
        User user = null;

        validateCpf(cpf);

        try {
            user = userService.findByCpf(cpf);
        } catch (NoDataFoundException e) {
            log.warn("try create a new user");
            user = userService.save(new UserRequest(cpf));
        }

        return user;
    }

    /**
     * Validate if this cpf can vote
     * @param cpf
     */
    private void validateCpf(String cpf) {

        UserValidation validation;

        try {
            validation = validatorClient.findByCpf(cpf);
        } catch (FeignException e) {
            throw new ValidationException(e.getMessage());
        }

        if(StringUtils.isBlank(validation.getStatus()) || !validation.getStatus().equalsIgnoreCase("ABLE_TO_VOTE")) {
            throw new ValidationException("Cpf cannot vote.");
        }
    }
}
