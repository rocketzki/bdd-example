package com.rocketzki.bddexample;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class AppUser {

    private Long id;
    private String name;
    private List<String> assignedApps;


    private OffsetDateTime userLocalCreatedAt;
    private OffsetDateTime userLocalUpdatedAt;

    public AppUser(Long id, String name, List<String> assignedApps) {
        OffsetDateTime now = OffsetDateTime.now();
        this.id = id;
        this.name = name;
        this.assignedApps = assignedApps;
        this.userLocalCreatedAt = now;
        this.userLocalUpdatedAt = now;
    }

}
