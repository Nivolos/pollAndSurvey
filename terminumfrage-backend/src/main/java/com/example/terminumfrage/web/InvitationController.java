package com.example.terminumfrage.web;

import com.example.terminumfrage.model.User;
import com.example.terminumfrage.model.dto.PollSummaryDto;
import com.example.terminumfrage.model.dto.ResponseUpdateDto;
import com.example.terminumfrage.service.CurrentUserService;
import com.example.terminumfrage.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InvitationController {

    private final PollService pollService;
    private final CurrentUserService currentUserService;

    public InvitationController(PollService pollService, CurrentUserService currentUserService) {
        this.pollService = pollService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<PollSummaryDto>> listInvitations() {
        User user = currentUserService.requireCurrentUser();
        return ResponseEntity.ok(pollService.listInvitations(user));
    }

    @PutMapping("/polls/{pollId}/responses")
    public ResponseEntity<Void> updateResponses(@PathVariable Long pollId, @Valid @RequestBody List<ResponseUpdateDto> updates) {
        User user = currentUserService.requireCurrentUser();
        pollService.updateResponses(pollId, user, user.getEmail(), updates);
        return ResponseEntity.noContent().build();
    }
}
