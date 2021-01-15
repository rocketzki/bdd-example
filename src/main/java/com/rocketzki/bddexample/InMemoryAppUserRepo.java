package com.rocketzki.bddexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InMemoryAppUserRepo {

    private final Map<Long, AppUser> store = new HashMap<>();

    public void save(@NonNull AppUser entity) {
        putOrReplace(entity);
        log.info("### Entity '" + entity + "' saved!");
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
