package com.example.terminumfrage.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "response", uniqueConstraints = @UniqueConstraint(columnNames = {"time_slot_id", "participant_user_id", "participant_email"}))
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_user_id")
    private User participantUser;

    @Email
    @Column(name = "participant_email")
    private String participantEmail;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResponseValue value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public User getParticipantUser() {
        return participantUser;
    }

    public void setParticipantUser(User participantUser) {
        this.participantUser = participantUser;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public ResponseValue getValue() {
        return value;
    }

    public void setValue(ResponseValue value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(timeSlot, response.timeSlot) && Objects.equals(participantUser, response.participantUser) && Objects.equals(participantEmail, response.participantEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeSlot, participantUser, participantEmail);
    }
}
