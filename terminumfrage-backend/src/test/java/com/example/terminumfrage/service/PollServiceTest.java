package com.example.terminumfrage.service;

import com.example.terminumfrage.data.FinalSelectionRepository;
import com.example.terminumfrage.data.InvitationRepository;
import com.example.terminumfrage.data.PollRepository;
import com.example.terminumfrage.data.ResponseRepository;
import com.example.terminumfrage.data.TimeSlotRepository;
import com.example.terminumfrage.data.UserRepository;
import com.example.terminumfrage.model.User;
import com.example.terminumfrage.model.dto.InvitationDto;
import com.example.terminumfrage.model.dto.PollCreateRequest;
import com.example.terminumfrage.model.dto.TimeSlotDto;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PollServiceTest {

    private PollService pollService;

    @BeforeEach
    void setUp() {
        PollRepository pollRepository = Mockito.mock(PollRepository.class);
        TimeSlotRepository timeSlotRepository = Mockito.mock(TimeSlotRepository.class);
        InvitationRepository invitationRepository = Mockito.mock(InvitationRepository.class);
        ResponseRepository responseRepository = Mockito.mock(ResponseRepository.class);
        FinalSelectionRepository finalSelectionRepository = Mockito.mock(FinalSelectionRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        pollService = new PollService(pollRepository, timeSlotRepository, invitationRepository,
                responseRepository, finalSelectionRepository, userRepository);
    }

    @Test
    void createPollRejectsInvalidSlots() {
        PollCreateRequest request = new PollCreateRequest();
        request.setTitle("Test");
        TimeSlotDto slot = new TimeSlotDto();
        slot.setStart(OffsetDateTime.now());
        slot.setEnd(OffsetDateTime.now().minusHours(1));
        request.setTimeSlots(List.of(slot));
        InvitationDto invitation = new InvitationDto();
        invitation.setEmail("test@example.com");
        request.setInvitations(List.of(invitation));
        assertThrows(ValidationException.class, () -> pollService.createPoll(new User(), request));
    }
}
