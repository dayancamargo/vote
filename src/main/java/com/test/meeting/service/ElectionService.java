package com.test.meeting.service;

import com.test.meeting.exception.NoDataFoundException;
import com.test.meeting.exception.ValidationException;
import com.test.meeting.model.agenda.Agenda;
import com.test.meeting.model.election.Election;
import com.test.meeting.model.election.ElectionRequest;
import com.test.meeting.repository.ElectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class ElectionService {

    private ElectionRepository electionRepository;
    private AgendaService agendaService;

    @Autowired
    public ElectionService(ElectionRepository electionRepository, AgendaService agendaService) {
        this.electionRepository = electionRepository;
        this.agendaService = agendaService;
    }

    public Election save(ElectionRequest request) {

        validateInsert(request);

        Agenda agenda = agendaService.findById(request.getIdAgenda());

        validateOpenElection(agenda);

        Election election = new Election();
        election.setAgenda(agenda);

        if(Objects.nonNull(request.getDuration()) && request.getDuration() > 1) {
            election.setExpireAt(election.getCreatedAt().plusMinutes(request.getDuration()));
        } else {
            election.setExpireAt(election.getCreatedAt().plusMinutes(1L));
        }

        log.debug("saving: {}", election);

        return electionRepository.saveAndFlush(election);
    }

    public Page<Election> findAll(Pageable page) {
        log.debug("Find with: {}", page);
        return electionRepository.findAll(page);
    }

    /**
     * Find all eligible elections for voting
     */
    public Page<Election> findAllValid(Pageable page) {
        log.debug("Find with: {}", page);
        return electionRepository.findAllNotExpired(page);
    }

    public Election findById(Long id) {
        if(Objects.nonNull(id))
            return electionRepository.findById(id).orElseThrow(() -> new NoDataFoundException("Cannot found election with id " + id));
        else
            throw new ValidationException("Id could not be null.");
    }

    /**
     * Validate if has a open election(not expired) with this agenda
     */
    private void validateOpenElection(Agenda agenda) {
        Long countValid = electionRepository.countValidByAgenda(agenda.getId());
        if(countValid > 0 ) {
            throw new ValidationException("This agenda has a open election.");
        }
    }

    /**
     * Validate data to insert a new record
     */
    private void validateInsert(ElectionRequest request) {
        if(Objects.isNull(request)){
            throw new ValidationException("ElectionRequest could not be null.");
        }

        if(Objects.isNull(request.getIdAgenda()) ){
            throw new ValidationException("Agenda id could not be null.");
        }
    }
}
