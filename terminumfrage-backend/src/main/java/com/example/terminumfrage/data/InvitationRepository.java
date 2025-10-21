package com.example.terminumfrage.data;

import com.example.terminumfrage.model.Invitation;
import com.example.terminumfrage.model.Poll;
import com.example.terminumfrage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInviteeUser(User inviteeUser);
    List<Invitation> findByInviteeEmail(String email);
    Optional<Invitation> findByPollAndInviteeEmail(Poll poll, String inviteeEmail);
}
