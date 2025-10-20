package com.example.terminumfrage.config;

import com.example.terminumfrage.data.InvitationRepository;
import com.example.terminumfrage.data.PollRepository;
import com.example.terminumfrage.data.TimeSlotRepository;
import com.example.terminumfrage.data.UserRepository;
import com.example.terminumfrage.model.Invitation;
import com.example.terminumfrage.model.Poll;
import com.example.terminumfrage.model.PollStatus;
import com.example.terminumfrage.model.TimeSlot;
import com.example.terminumfrage.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner demoData(UserRepository userRepository,
                                      PollRepository pollRepository,
                                      TimeSlotRepository timeSlotRepository,
                                      InvitationRepository invitationRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }
            User organizer = new User();
            organizer.setEmail("organizer@example.com");
            organizer.setFullName("Olivia Organizer");
            organizer.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(organizer);

            User participant = new User();
            participant.setEmail("participant@example.com");
            participant.setFullName("Peter Participant");
            participant.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(participant);

            Poll poll = new Poll();
            poll.setTitle("Kickoff Meeting");
            poll.setDescription("Initial project kickoff");
            poll.setOrganizer(organizer);
            poll.setStatus(PollStatus.OPEN);
            poll = pollRepository.save(poll);

            for (int i = 1; i <= 3; i++) {
                TimeSlot slot = new TimeSlot();
                slot.setPoll(poll);
                slot.setStartDateTime(OffsetDateTime.now().plusDays(i));
                slot.setEndDateTime(OffsetDateTime.now().plusDays(i).plusHours(1));
                timeSlotRepository.save(slot);
            }

            Invitation invitation = new Invitation();
            invitation.setPoll(poll);
            invitation.setInviteeEmail(participant.getEmail());
            invitation.setInviteeUser(participant);
            invitation.setToken(UUID.randomUUID().toString());
            invitationRepository.save(invitation);
        };
    }
}
