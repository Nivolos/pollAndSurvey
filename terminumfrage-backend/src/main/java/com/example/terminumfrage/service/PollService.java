package com.example.terminumfrage.service;

import com.example.terminumfrage.data.FinalSelectionRepository;
import com.example.terminumfrage.data.InvitationRepository;
import com.example.terminumfrage.data.PollRepository;
import com.example.terminumfrage.data.ResponseRepository;
import com.example.terminumfrage.data.TimeSlotRepository;
import com.example.terminumfrage.data.UserRepository;
import com.example.terminumfrage.model.FinalSelection;
import com.example.terminumfrage.model.Invitation;
import com.example.terminumfrage.model.Poll;
import com.example.terminumfrage.model.PollStatus;
import com.example.terminumfrage.model.Response;
import com.example.terminumfrage.model.ResponseValue;
import com.example.terminumfrage.model.TimeSlot;
import com.example.terminumfrage.model.User;
import com.example.terminumfrage.model.dto.InvitationDto;
import com.example.terminumfrage.model.dto.PollCreateRequest;
import com.example.terminumfrage.model.dto.PollDetailDto;
import com.example.terminumfrage.model.dto.PollSummaryDto;
import com.example.terminumfrage.model.dto.ResponseUpdateDto;
import com.example.terminumfrage.model.dto.SlotSummaryDto;
import com.example.terminumfrage.model.dto.TimeSlotDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class PollService {

    private final PollRepository pollRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final InvitationRepository invitationRepository;
    private final ResponseRepository responseRepository;
    private final FinalSelectionRepository finalSelectionRepository;
    private final UserRepository userRepository;

    public PollService(PollRepository pollRepository, TimeSlotRepository timeSlotRepository,
                       InvitationRepository invitationRepository, ResponseRepository responseRepository,
                       FinalSelectionRepository finalSelectionRepository, UserRepository userRepository) {
        this.pollRepository = pollRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.invitationRepository = invitationRepository;
        this.responseRepository = responseRepository;
        this.finalSelectionRepository = finalSelectionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PollDetailDto createPoll(User organizer, @Valid PollCreateRequest request) {
        validateSlots(request.getTimeSlots());
        Poll poll = new Poll();
        poll.setTitle(request.getTitle());
        poll.setDescription(request.getDescription());
        poll.setOrganizer(organizer);
        poll = pollRepository.save(poll);

        for (TimeSlotDto dto : request.getTimeSlots()) {
            TimeSlot slot = new TimeSlot();
            slot.setPoll(poll);
            slot.setStartDateTime(dto.getStart());
            slot.setEndDateTime(dto.getEnd());
            timeSlotRepository.save(slot);
        }

        for (InvitationDto dto : request.getInvitations()) {
            Invitation invitation = new Invitation();
            invitation.setPoll(poll);
            invitation.setInviteeEmail(dto.getEmail());
            invitation.setToken(UUID.randomUUID().toString());
            userRepository.findByEmail(dto.getEmail()).ifPresent(invitation::setInviteeUser);
            invitationRepository.save(invitation);
        }

        return getPollDetail(poll.getId(), organizer);
    }

    public List<PollSummaryDto> listOrganizerPolls(User organizer) {
        return pollRepository.findByOrganizer(organizer).stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    public PollDetailDto getPollDetail(Long id, User organizer) {
        Poll poll = pollRepository.findById(id)
                .filter(p -> p.getOrganizer().equals(organizer))
                .orElseThrow(() -> new ValidationException("Poll not found"));
        return toDetail(poll);
    }

    @Transactional
    public PollDetailDto openPoll(Long pollId, User organizer) {
        Poll poll = pollRepository.findById(pollId)
                .filter(p -> p.getOrganizer().equals(organizer))
                .orElseThrow(() -> new ValidationException("Poll not found"));
        if (poll.getStatus() != PollStatus.DRAFT) {
            throw new ValidationException("Poll must be draft to open");
        }
        if (poll.getTimeSlots().isEmpty()) {
            throw new ValidationException("Poll requires slots");
        }
        poll.setStatus(PollStatus.OPEN);
        return toDetail(poll);
    }

    @Transactional
    public PollDetailDto finalizePoll(Long pollId, Long slotId, User organizer) {
        Poll poll = pollRepository.findById(pollId)
                .filter(p -> p.getOrganizer().equals(organizer))
                .orElseThrow(() -> new ValidationException("Poll not found"));
        if (poll.getStatus() != PollStatus.OPEN) {
            throw new ValidationException("Poll must be open to finalize");
        }
        TimeSlot slot = timeSlotRepository.findById(slotId)
                .filter(s -> s.getPoll().equals(poll))
                .orElseThrow(() -> new ValidationException("Slot not found"));
        FinalSelection selection = finalSelectionRepository.findByPoll(poll)
                .orElseGet(FinalSelection::new);
        selection.setPoll(poll);
        selection.setSlot(slot);
        finalSelectionRepository.save(selection);
        poll.setStatus(PollStatus.FINALIZED);
        return toDetail(poll);
    }

    @Transactional
    public void updateResponses(Long pollId, User participant, String participantEmail, List<@Valid ResponseUpdateDto> updates) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new ValidationException("Poll not found"));
        if (poll.getStatus() == PollStatus.FINALIZED) {
            throw new ValidationException("Poll finalized");
        }
        if (poll.getStatus() == PollStatus.DRAFT) {
            throw new ValidationException("Poll not open");
        }
        for (ResponseUpdateDto update : updates) {
            TimeSlot slot = timeSlotRepository.findById(update.getSlotId())
                    .filter(s -> s.getPoll().equals(poll))
                    .orElseThrow(() -> new ValidationException("Slot not found"));
            Response response;
            if (participant != null) {
                response = responseRepository.findByTimeSlotAndParticipantUser(slot, participant)
                        .orElseGet(() -> {
                            Response r = new Response();
                            r.setPoll(poll);
                            r.setTimeSlot(slot);
                            r.setParticipantUser(participant);
                            r.setParticipantEmail(participant.getEmail());
                            return r;
                        });
            } else {
                response = responseRepository.findByTimeSlotAndParticipantEmail(slot, participantEmail)
                        .orElseGet(() -> {
                            Response r = new Response();
                            r.setPoll(poll);
                            r.setTimeSlot(slot);
                            r.setParticipantEmail(participantEmail);
                            return r;
                        });
            }
            response.setValue(update.getValue());
            responseRepository.save(response);
        }
    }

    public List<PollSummaryDto> listInvitations(User user) {
        return invitationRepository.findByInviteeUser(user).stream()
                .map(Invitation::getPoll)
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    private void validateSlots(List<TimeSlotDto> slots) {
        for (TimeSlotDto slot : slots) {
            OffsetDateTime start = slot.getStart();
            OffsetDateTime end = slot.getEnd();
            if (start == null || end == null || !end.isAfter(start)) {
                throw new ValidationException("Invalid slot range");
            }
        }
    }

    private PollSummaryDto toSummary(Poll poll) {
        PollSummaryDto dto = new PollSummaryDto();
        dto.setId(poll.getId());
        dto.setTitle(poll.getTitle());
        dto.setStatus(poll.getStatus());
        dto.setCreatedAt(poll.getCreatedAt());
        return dto;
    }

    private PollDetailDto toDetail(Poll poll) {
        PollDetailDto dto = new PollDetailDto();
        dto.setId(poll.getId());
        dto.setTitle(poll.getTitle());
        dto.setDescription(poll.getDescription());
        dto.setStatus(poll.getStatus());
        dto.setCreatedAt(poll.getCreatedAt());
        dto.setTimeSlots(poll.getTimeSlots().stream().map(this::toSlotDto).collect(Collectors.toList()));
        dto.setInvitations(poll.getInvitations().stream().map(this::toInvitationDto).collect(Collectors.toList()));
        Map<Long, SlotSummaryDto> summaries = new HashMap<>();
        List<Response> responses = responseRepository.findByPoll(poll);
        for (Response response : responses) {
            SlotSummaryDto summary = summaries.computeIfAbsent(response.getTimeSlot().getId(), id -> new SlotSummaryDto());
            if (response.getValue() == ResponseValue.YES) {
                summary.setYesCount(summary.getYesCount() + 1);
            } else if (response.getValue() == ResponseValue.NO) {
                summary.setNoCount(summary.getNoCount() + 1);
            } else {
                summary.setMaybeCount(summary.getMaybeCount() + 1);
            }
        }
        dto.setSlotSummaries(summaries);
        return dto;
    }

    private TimeSlotDto toSlotDto(TimeSlot slot) {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setId(slot.getId());
        dto.setStart(slot.getStartDateTime());
        dto.setEnd(slot.getEndDateTime());
        return dto;
    }

    private InvitationDto toInvitationDto(Invitation invitation) {
        InvitationDto dto = new InvitationDto();
        dto.setId(invitation.getId());
        dto.setEmail(invitation.getInviteeEmail());
        dto.setState(invitation.getState());
        return dto;
    }
}
