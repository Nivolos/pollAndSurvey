package com.example.terminumfrage.data;

import com.example.terminumfrage.model.FinalSelection;
import com.example.terminumfrage.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinalSelectionRepository extends JpaRepository<FinalSelection, Long> {
    Optional<FinalSelection> findByPoll(Poll poll);
}
