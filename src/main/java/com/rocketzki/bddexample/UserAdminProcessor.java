package com.rocketzki.bddexample;

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
        user.setUserLocalUpdatedAt(event.getTimestamp());

        repository.save(user);

        System.out.println("## User " + user.getId() + " has been modified.");
    }

    private void createUser(AppUserChangeEvent event) {
        var user = new AppUser(event.getId(), event.getName(), event.getAssignedApps(), event.getTimestamp());

        repository.save(user);

        System.out.println("## User " + user.getId() + " has been modified.");

    }


}
