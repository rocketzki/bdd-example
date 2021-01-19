package com.rocketzki.bddexample;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class AppUserChangeEvent {
    private Long id;
    private String name;
    private List<String> assignedApps;
    private OffsetDateTime timestamp;
}
