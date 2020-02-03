package com.test.meeting.service;

import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.agenda.AgendaRequest;
import com.test.meeting.repository.AgendaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class AgendaService {

    private AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda save(AgendaRequest request) {

        if(Objects.isNull(request)){
            throw  new ValidationException("AgendaRequest could not be null.");
        }

        Agenda agenda = new Agenda();
        agenda.setName(request.getName());
        agenda.setCreatedAt(LocalDateTime.now());
        log.debug("saving: {}", agenda);

        return agendaRepository.saveAndFlush(agenda);
    }

    public Agenda findById(Long id) {
        if(Objects.nonNull(id))
            return agendaRepository.findById(id)
                                   .orElseThrow(() -> new NoDataFoundException("Agenda not exists."));
        else
            throw new ValidationException("Agenda id cannot be null.");
    }

    public Page<Agenda> findAll(Pageable page) {
        log.debug("Find with: {}", page);
        return agendaRepository.findAll(page);
    }
}
