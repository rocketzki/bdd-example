package com.rocketzki.bddexample;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAppUserRepo {

    private final Map<Long, AppUser> store = new HashMap<>();

    public void save(AppUser entity) {
        putOrReplace(entity);
        System.out.println("### Entity '" + entity + "' saved!");
    }

    public AppUser findUser(Long id) {
        return store.getOrDefault(id, null);
    }

    private void putOrReplace(AppUser entity) {
        if (findUser(entity.getId()) == null) {
            store.put(entity.getId(), entity);
        } else {
            store.replace(entity.getId(), entity);
        }
    }
}
