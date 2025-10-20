package com.example.terminumfrage.data;

import com.example.terminumfrage.model.Poll;
import com.example.terminumfrage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByOrganizer(User organizer);
}
