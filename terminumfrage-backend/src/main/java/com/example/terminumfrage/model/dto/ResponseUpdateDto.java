package com.example.terminumfrage.model.dto;

import com.example.terminumfrage.model.ResponseValue;
import jakarta.validation.constraints.NotNull;

public class ResponseUpdateDto {

    @NotNull
    private Long slotId;

    @NotNull
    private ResponseValue value;

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public ResponseValue getValue() {
        return value;
    }

    public void setValue(ResponseValue value) {
        this.value = value;
    }
}
