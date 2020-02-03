package com.test.meeting.repository;

import com.test.meeting.model.vote.VoteSummary;
import com.test.meeting.model.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value = " select count(0) as Quantity, v.vote as Vote, v.id_election as Election" +
            " from Vote v " +
            " where v.id_election = :idElection " +
            " group by v.vote, v.id_election ",
            nativeQuery = true)
    List<VoteSummary> getVoteResult(@Param("idElection") Long idElection);
}
