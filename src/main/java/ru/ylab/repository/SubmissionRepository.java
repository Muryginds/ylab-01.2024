package ru.ylab.repository;

import ru.ylab.entity.Submission;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface SubmissionRepository {
    Collection<Submission> getByUserId(Long userId);

    void save(Submission submission);

    boolean checkExistsByUserIdAndDate(Long userId, LocalDate date);

    Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date);

    Optional<Submission> findLastByUserId(Long userId);
}
