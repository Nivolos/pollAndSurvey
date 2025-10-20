package com.example.terminumfrage.web;

import com.example.terminumfrage.model.User;
import com.example.terminumfrage.model.dto.PollCreateRequest;
import com.example.terminumfrage.model.dto.PollDetailDto;
import com.example.terminumfrage.model.dto.PollSummaryDto;
import com.example.terminumfrage.service.CurrentUserService;
import com.example.terminumfrage.service.PollService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/polls")
public class PollController {

    private final PollService pollService;
    private final CurrentUserService currentUserService;

    public PollController(PollService pollService, CurrentUserService currentUserService) {
        this.pollService = pollService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<PollSummaryDto>> listOrganizerPolls() {
        User user = currentUserService.requireCurrentUser();
        return ResponseEntity.ok(pollService.listOrganizerPolls(user));
    }

    @PostMapping
    public ResponseEntity<PollDetailDto> createPoll(@Valid @RequestBody PollCreateRequest request) {
        User user = currentUserService.requireCurrentUser();
        return ResponseEntity.ok(pollService.createPoll(user, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollDetailDto> getPoll(@PathVariable Long id) {
        User user = currentUserService.requireCurrentUser();
        return ResponseEntity.ok(pollService.getPollDetail(id, user));
    }

    @PostMapping("/{id}/open")
    public ResponseEntity<PollDetailDto> openPoll(@PathVariable Long id) {
        User user = currentUserService.requireCurrentUser();
        return ResponseEntity.ok(pollService.openPoll(id, user));
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<PollDetailDto> finalizePoll(@PathVariable Long id, @RequestBody Map<String, @NotNull Long> body) {
        User user = currentUserService.requireCurrentUser();
        Long slotId = body.get("slotId");
        return ResponseEntity.ok(pollService.finalizePoll(id, slotId, user));
    }
}
