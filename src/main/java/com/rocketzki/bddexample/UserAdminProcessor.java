package com.rocketzki.bddexample;

import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

@Slf4j
public class UserAdminProcessor {

    private final InMemoryAppUserRepo repository;

    public UserAdminProcessor(InMemoryAppUserRepo repository) {
        this.repository = repository;
    }

    void process(AppUserChangeEvent event) {
        var user = repository.findUser(event.getId());

        if (user != null) {
            modifyUser(event, user);

        } else {
            createUser(event);
        }
    }

    private void modifyUser(AppUserChangeEvent event, AppUser user) {
        user.getAssignedApps().addAll(event.getAssignedApps());
        user.setName(event.getName());
        user.setUserLocalUpdatedAt(OffsetDateTime.now());

        repository.save(user);

        log.info("## User " + user.getId() + " has been modified.");
    }

    private void createUser(AppUserChangeEvent event) {
        var user = new AppUser(event.getId(), event.getName(), event.getAssignedApps());

        repository.save(user);

        log.info("## User " + user.getId() + " has been modified.");

    }


}
