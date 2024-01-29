package ru.ylab.enumerated;

/**
 * Enumeration representing different types of audition events in the system.
 *
 * <p>This enum defines several event types, each representing a specific action or occurrence within the system.
 */
public enum AuditionEventType {
    SESSION_START,
    SESSION_END,
    REGISTRATION,
    READINGS_SUBMISSION,
    NEW_METER_TYPE_ADDITION,
    SINGLE_SUBMISSION_ACQUIRE,
    SUBMISSION_HISTORY_ACQUIRE
}
