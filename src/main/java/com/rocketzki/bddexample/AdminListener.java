package com.rocketzki.bddexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AdminListener {

    private final UserAdminProcessor userAdminProcessor;

    public AdminListener(UserAdminProcessor processor) {
        this.userAdminProcessor = processor;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();


    public void receive(String message) {
        try {
            var appUser = objectMapper.readValue(message, AppUserChangeEvent.class);
            userAdminProcessor.process(appUser);

        } catch (IOException exc) {
            log.error("Cannot read message", exc);
        }
    }
}
