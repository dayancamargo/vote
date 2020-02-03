package com.test.meeting.service;

import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.agenda.AgendaRequest;
import com.test.meeting.model.user.User;
import com.test.meeting.repository.AgendaRepository;
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

public class AgendaServiceTest {
    private final String MOCK_NAME = "name";
    private final LocalDateTime MOCK_CREATED = LocalDateTime.now();
    @Mock
    AgendaRepository agendaRepository;

    @InjectMocks
    AgendaService agendaService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        when(agendaRepository.saveAndFlush(fakeAgenda())).thenReturn(fakeAgenda());
        Agenda result = agendaService.save(new AgendaRequest(MOCK_NAME));
        Assert.assertEquals(fakeAgenda(), result);
    }

    @Test(expected = ValidationException.class)
    public void testSaveNull() throws Exception {
       agendaService.save(null);
    }

    @Test
    public void testFindById() throws Exception {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(fakeAgenda()));
        Agenda result = agendaService.findById(1L);
        Assert.assertEquals(fakeAgenda(), result);
    }

    @Test(expected = ValidationException.class)
    public void testFindByIdWithNull() throws Exception {
        agendaService.findById(null);
    }

    @Test(expected = NoDataFoundException.class)
    public void testFindByIdNoDataFound() throws Exception {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());
        agendaService.findById(1L);
    }

    @Test
    public void testFindAll() throws Exception {
        when(agendaRepository.findAll((Pageable) null)).thenReturn(fakePage());
        Page<Agenda> result = agendaService.findAll(null);
        Assert.assertEquals(fakePage(), result);
    }

    private Agenda fakeAgenda(){
        Agenda agenda = new Agenda();
        agenda.setName(MOCK_NAME);
        agenda.setCreatedAt(MOCK_CREATED);
        return agenda;
    }

    private List<Agenda> fakeList(){
        return Arrays.asList(fakeAgenda(), fakeAgenda(), fakeAgenda());
    }

    private Page<Agenda> fakePage(){
        return new PageImpl<>(fakeList());
    }
}