package com.test.meeting.repository;

import com.test.meeting.model.election.Election;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

    @Query("Select e from Election e where expireAt > sysdate")
    Page<Election> findAllNotExpired(Pageable page);

    @Query( value = " Select count(0) from Election e where e.id_Agenda = :idAgenda and e.expire_At > sysdate ",
            nativeQuery = true)
    Long countValidByAgenda(@Param("idAgenda") Long idAgenda);
}
