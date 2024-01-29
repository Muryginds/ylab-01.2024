package ru.ylab.repository.impl;

import ru.ylab.entity.Submission;
import ru.ylab.repository.SubmissionRepository;

import java.time.LocalDate;
import java.util.*;

public class InMemorySubmissionRepository implements SubmissionRepository {
    private static final Map<Long, TreeMap<LocalDate, Submission>> SUBMISSIONS = new HashMap<>();

    private static TreeMap<LocalDate, Submission> getUserSubmissionMap(Long userId) {
        return SUBMISSIONS.getOrDefault(userId, new TreeMap<>());
    }

    @Override
    public Collection<Submission> getByUserId(Long userId) {
        return getUserSubmissionMap(userId).values();
    }

    @Override
    public void save(Submission submission) {
        var readings = getUserSubmissionMap(submission.getUser().getId());
        readings.put(submission.getDate().withDayOfMonth(1), submission);
        SUBMISSIONS.put(submission.getUser().getId(), readings);
    }

    @Override
    public boolean checkExistsByUserIdAndDate(Long userId, LocalDate date) {
        var readings = getUserSubmissionMap(userId);
        return readings.containsKey(date.withDayOfMonth(1));
    }

    @Override
    public Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date) {
        var readings = getUserSubmissionMap(userId);
        return Optional.ofNullable(readings.get(date.withDayOfMonth(1)));
    }

    @Override
    public Optional<Submission> findLastByUserId(Long userId) {
        var readings = getUserSubmissionMap(userId);
        if (readings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(readings.lastEntry().getValue());
    }
}
