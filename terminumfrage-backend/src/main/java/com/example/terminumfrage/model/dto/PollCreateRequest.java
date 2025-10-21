package com.example.terminumfrage.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PollCreateRequest {

    @NotBlank
    private String title;

    private String description;

    @NotEmpty
    private List<@Valid InvitationDto> invitations;

    @NotEmpty
    private List<@Valid TimeSlotDto> timeSlots;

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

    public List<InvitationDto> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<InvitationDto> invitations) {
        this.invitations = invitations;
    }

    public List<TimeSlotDto> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotDto> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
