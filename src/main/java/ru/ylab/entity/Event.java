package ru.ylab.entity;

import ru.ylab.enumerated.EventType;

public record Event(EventType type, String message) {
}
