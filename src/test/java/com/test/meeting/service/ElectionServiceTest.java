package com.test.meeting.service;

import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.election.Election;
import com.test.meeting.model.election.ElectionRequest;
import com.test.meeting.repository.ElectionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ElectionServiceTest {
    private final String MOCK_NAME = "name";
    private final LocalDateTime MOCK_CREATED = LocalDateTime.now();
    private final Long MOCK_EXPIRED_MIN = 10L;

    @Mock
    ElectionRepository electionRepository;
    @Mock
    AgendaService agendaService;
    @InjectMocks
    ElectionService electionService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        when(electionRepository.countValidByAgenda(1L)).thenReturn(0L);
        when(agendaService.findById(1L)).thenReturn(fakeAgenda());
        when(electionRepository.saveAndFlush(fakeElection())).thenReturn(fakeElection(MOCK_EXPIRED_MIN));

        Election result = electionService.save(new ElectionRequest(1L, MOCK_EXPIRED_MIN));
        Assert.assertEquals(fakeElection(MOCK_EXPIRED_MIN), result);
        Assert.assertEquals(MOCK_CREATED.plusMinutes(MOCK_EXPIRED_MIN), result.getExpireAt());
        Assert.assertFalse(result.isExired());
    }

    @Test
    public void testSaveNoExpireInformed() throws Exception {
        when(electionRepository.countValidByAgenda(1L)).thenReturn(0L);
        when(agendaService.findById(1L)).thenReturn(fakeAgenda());
        when(electionRepository.saveAndFlush(fakeElection())).thenReturn(fakeElection(1L));

        Election result = electionService.save(new ElectionRequest(1L, null));
        Assert.assertEquals(MOCK_CREATED.plusMinutes(1L), result.getExpireAt());
        Assert.assertFalse(result.isExired());
    }

    @Test(expected = ValidationException.class)
    public void testSaveNull() throws Exception {
        electionService.save(null);
        verify(electionRepository, never()).saveAndFlush(any());
        verify(agendaService, never()).findById(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveFailValidation() throws Exception {
        when(electionRepository.countValidByAgenda(1L)).thenReturn(0L);
        when(agendaService.findById(1L)).thenReturn(fakeAgenda());
        when(electionRepository.saveAndFlush(fakeElection())).thenReturn(fakeElection(MOCK_EXPIRED_MIN));

        electionService.save(new ElectionRequest());
        verify(electionRepository, never()).saveAndFlush(any());
        verify(agendaService, never()).findById(any());
    }

    @Test(expected = ValidationException.class)
    public void testSaveFailFindAgenda() throws Exception {
        when(electionRepository.countValidByAgenda(1L)).thenReturn(0L);
        when(agendaService.findById(1L)).thenReturn(null);

        electionService.save(new ElectionRequest());
        verify(electionRepository, never()).saveAndFlush(any());
        verify(agendaService, times(1)).findById(1L);
    }

    @Test(expected = ValidationException.class)
    public void testSaveHasOpenElection() throws Exception {
        when(electionRepository.countValidByAgenda(1L)).thenReturn(1L);
        when(agendaService.findById(1L)).thenReturn(fakeAgenda());
        when(electionRepository.saveAndFlush(fakeElection())).thenReturn(fakeElection(MOCK_EXPIRED_MIN));

        Election result = electionService.save(new ElectionRequest(1L, MOCK_EXPIRED_MIN));
        verify(electionRepository, never()).saveAndFlush(any());
        verify(agendaService, times(1)).findById(1L);
    }

    @Test
    public void testFindAll() throws Exception {
        when(electionRepository.findAll((Pageable) null)).thenReturn(fakePage());
        Page<Election> result = electionService.findAll(null);
        Assert.assertEquals(fakePage(), result);
    }

    @Test
    public void testFindAllValid() throws Exception {
        when(electionRepository.findAllNotExpired((Pageable) null)).thenReturn(fakePage());
        Page<Election> result = electionService.findAllValid(null);
        Assert.assertEquals(fakePage(), result);
    }

    @Test
    public void testFindById() throws Exception {
        when(electionRepository.findById(1L)).thenReturn(Optional.of(fakeElection()));
        Election result = electionService.findById(Long.valueOf(1));
        Assert.assertEquals(fakeElection(), result);
    }

    @Test(expected = ValidationException.class)
    public void testFindByIdWithNull() throws Exception {
        electionService.findById(null);
    }

    @Test(expected = NoDataFoundException.class)
    public void testFindByIdNoDataFound() throws Exception {
        when(electionRepository.findById(1L)).thenReturn(Optional.empty());
        electionService.findById(1L);
    }

    private Election fakeElection() {
        Election election = new Election();
        election.setAgenda(fakeAgenda());
        election.setCreatedAt(MOCK_CREATED);
        return election;
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

    private List<Election> fakeList() {
        return Arrays.asList(fakeElection(), fakeElection(), fakeElection());
    }

    private Page<Election> fakePage(){
        return new PageImpl<>(fakeList());
    }
}

