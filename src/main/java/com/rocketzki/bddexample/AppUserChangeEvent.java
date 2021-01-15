package com.rocketzki.bddexample;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AppUserChangeEvent {
    private final Long id;
    private final String name;
    private final List<String> assignedApps;
    private final OffsetDateTime timestamp;
}
