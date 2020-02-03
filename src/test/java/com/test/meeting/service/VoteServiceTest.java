package com.test.meeting.service;

import com.test.meeting.client.ValidatorClient;
import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.election.Election;
import com.test.meeting.model.user.User;
import com.test.meeting.model.user.UserRequest;
import com.test.meeting.model.user.UserValidation;
import com.test.meeting.model.vote.Vote;
import com.test.meeting.model.vote.VoteEnum;
import com.test.meeting.model.vote.VoteRequest;
import com.test.meeting.model.vote.VoteSummary;
import com.test.meeting.repository.VoteRepository;
import feign.FeignException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class VoteServiceTest {

    private final String MOCK_NAME = "name";
    private final LocalDateTime MOCK_CREATED = LocalDateTime.now();
    private String MOCK_CPF = "00000000000";

    @Mock
    UserService userService;
    @Mock
    VoteRepository voteRepository;
    @Mock
    ElectionService electionService;
    @Mock
    ValidatorClient validatorClient;
    @InjectMocks
    VoteService voteService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        when(voteRepository.saveAndFlush(any(Vote.class))).thenReturn(fakeVote());
        when(userService.findByCpf(MOCK_CPF)).thenReturn(fakeUser());
        when(electionService.findById(1L)).thenReturn(fakeElection(10L));
        when(validatorClient.findByCpf(MOCK_CPF)).thenReturn(new UserValidation("ABLE_TO_VOTE"));

        Vote result = voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        Assert.assertEquals(fakeVote(), result);
        verify(userService, never()).save(any());
    }

    @Test
    public void testSaveWithCreateUser() throws Exception {
        when(voteRepository.saveAndFlush(any(Vote.class))).thenReturn(fakeVote());
        when(userService.findByCpf(MOCK_CPF)).thenThrow(NoDataFoundException.class);
        when(userService.save(new UserRequest(MOCK_CPF))).thenReturn(fakeUser());
        when(electionService.findById(1L)).thenReturn(fakeElection(10L));
        when(validatorClient.findByCpf(MOCK_CPF)).thenReturn(new UserValidation("ABLE_TO_VOTE"));

        Vote result = voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        Assert.assertEquals(fakeVote(), result);
        verify(userService, times(1)).save(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveNull() throws Exception {
        voteService.save(null);
        verify(voteRepository, never()).saveAndFlush(any());
    }

    @Test(expected = NoDataFoundException.class)
    public void testSaveElectionNotFound() throws Exception {
        when(electionService.findById(1L)).thenThrow(NoDataFoundException.class);

        voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        verify(voteRepository, never()).saveAndFlush(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveElectionExpired() throws Exception {
        when(electionService.findById(1L)).thenReturn(fakeElection(-1L));

        voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        verify(voteRepository, never()).saveAndFlush(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveValidatorFail() throws Exception {
        when(voteRepository.saveAndFlush(any(Vote.class))).thenReturn(fakeVote());
        when(userService.findByCpf(MOCK_CPF)).thenReturn(fakeUser());
        when(electionService.findById(1L)).thenReturn(fakeElection(10L));
        when(validatorClient.findByCpf(MOCK_CPF)).thenReturn(new UserValidation("BLAH"));

        voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        verify(voteRepository, never()).saveAndFlush(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveValidatorFailNotFound() throws Exception {
        when(voteRepository.saveAndFlush(any(Vote.class))).thenReturn(fakeVote());
        when(userService.findByCpf(MOCK_CPF)).thenReturn(fakeUser());
        when(electionService.findById(1L)).thenReturn(fakeElection(10L));
        when(validatorClient.findByCpf(MOCK_CPF)).thenThrow(FeignException.class);

        voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        verify(voteRepository, never()).saveAndFlush(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveElectionOpened() throws Exception {
        when(voteRepository.saveAndFlush(any(Vote.class))).thenThrow(DataIntegrityViolationException.class);
        when(userService.findByCpf(MOCK_CPF)).thenReturn(fakeUser());
        when(electionService.findById(1L)).thenReturn(fakeElection(10L));
        when(validatorClient.findByCpf(MOCK_CPF)).thenReturn(new UserValidation("ABLE_TO_VOTE"));

        Vote result = voteService.save(new VoteRequest(1L, MOCK_CPF, VoteEnum.SIM));
        Assert.assertEquals(fakeVote(), result);
        verify(userService, never()).save(any());
    }


    @Test
    public void testFindAll() throws Exception {
        when(voteRepository.findAll((Pageable) null)).thenReturn(fakePage());
        Page<Vote> result = voteService.findAll(null);
        Assert.assertEquals(fakePage(), result);
    }

    private Vote fakeVote() {
        Vote vote = new Vote();
        vote.setVote(VoteEnum.SIM);
        vote.setElection(fakeElection(10L));
        vote.setCreatedAt(MOCK_CREATED);
        vote.setUser(fakeUser());
        return vote;
    }

    private Election fakeElection(Long min) {
        Election election = new Election();
        election.setAgenda(fakeAgenda());
        election.setCreatedAt(MOCK_CREATED);
        election.setExpireAt(MOCK_CREATED.plusMinutes(min));
        election.setId(1L);
        return election;
    }

    private Agenda fakeAgenda() {
        Agenda agenda = new Agenda();
        agenda.setName(MOCK_NAME);
        agenda.setCreatedAt(MOCK_CREATED);
        agenda.setId(1L);
        return agenda;
    }

    private List<Vote> fakeList() {
        return Arrays.asList(fakeVote(), fakeVote(), fakeVote());
    }

    private Page<Vote> fakePage() {
        return new PageImpl<>(fakeList());
    }

    private User fakeUser(){
        User user = new User();
        user.setCpf(MOCK_CPF);
        user.setCreatedAt(MOCK_CREATED);
        return user;
    }
}