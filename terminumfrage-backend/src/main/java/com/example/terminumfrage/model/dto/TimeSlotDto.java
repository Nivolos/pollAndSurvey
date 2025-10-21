package com.example.terminumfrage.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class TimeSlotDto {

    private Long id;

    @NotNull
    private OffsetDateTime start;

    @NotNull
    private OffsetDateTime end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public void setStart(OffsetDateTime start) {
        this.start = start;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public void setEnd(OffsetDateTime end) {
        this.end = end;
    }
}
