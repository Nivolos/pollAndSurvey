package com.example.terminumfrage.model.dto;

public class SlotSummaryDto {

    private long yesCount;
    private long noCount;
    private long maybeCount;

    public long getYesCount() {
        return yesCount;
    }

    public void setYesCount(long yesCount) {
        this.yesCount = yesCount;
    }

    public long getNoCount() {
        return noCount;
    }

    public void setNoCount(long noCount) {
        this.noCount = noCount;
    }

    public long getMaybeCount() {
        return maybeCount;
    }

    public void setMaybeCount(long maybeCount) {
        this.maybeCount = maybeCount;
    }
}
