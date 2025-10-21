package com.example.terminumfrage.data;

import com.example.terminumfrage.model.Poll;
import com.example.terminumfrage.model.Response;
import com.example.terminumfrage.model.TimeSlot;
import com.example.terminumfrage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByPoll(Poll poll);
    Optional<Response> findByTimeSlotAndParticipantUser(TimeSlot timeSlot, User user);
    Optional<Response> findByTimeSlotAndParticipantEmail(TimeSlot timeSlot, String participantEmail);
}
