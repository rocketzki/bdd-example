package com.rocketzki.bddexample;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;

public class AdminListener {

    private final UserAdminProcessor userAdminProcessor;
    private final ObjectMapper objectMapper;

    public AdminListener(UserAdminProcessor processor, ObjectMapper objectMapper) {
        this.userAdminProcessor = processor;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void receive(String message) {
        try {
            var appUser = objectMapper.readValue(message, AppUserChangeEvent.class);
            userAdminProcessor.process(appUser);

        } catch (IOException exc) {
            System.out.println("Cannot read message");
            throw exc;
        }
    }
}
