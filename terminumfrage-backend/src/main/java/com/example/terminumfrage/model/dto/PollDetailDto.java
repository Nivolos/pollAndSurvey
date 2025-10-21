package com.example.terminumfrage.model.dto;

import com.example.terminumfrage.model.PollStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class PollDetailDto {

    private Long id;
    private String title;
    private String description;
    private PollStatus status;
    private Instant createdAt;
    private List<TimeSlotDto> timeSlots;
    private List<InvitationDto> invitations;
    private Map<Long, SlotSummaryDto> slotSummaries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PollStatus getStatus() {
        return status;
    }

    public void setStatus(PollStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<TimeSlotDto> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotDto> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public List<InvitationDto> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<InvitationDto> invitations) {
        this.invitations = invitations;
    }

    public Map<Long, SlotSummaryDto> getSlotSummaries() {
        return slotSummaries;
    }

    public void setSlotSummaries(Map<Long, SlotSummaryDto> slotSummaries) {
        this.slotSummaries = slotSummaries;
    }
}
