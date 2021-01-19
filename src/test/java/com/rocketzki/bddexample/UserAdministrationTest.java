package com.rocketzki.bddexample;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class UserAdministrationTest {


    private AdminListener listener;

    @Mock
    private InMemoryAppUserRepo repo;

    @InjectMocks
    private UserAdminProcessor processor;

    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;

    @Before
    public void setup() {
        listener = new AdminListener(processor, new ObjectMapper()
                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
                .findAndRegisterModules());
    }

    @Test
    public void userPropertiesAreSuccessfullyUpdated() {
        given(repo.findUser(9L))
                .willReturn(AppUser.builder()
                        .id(9L)
                        .name("TEST_USER")
                        .assignedApps(new ArrayList<>(Arrays.asList("GameFactor", "AdobeXD")))
                        .userLocalCreatedAt(OffsetDateTime.parse("2020-08-22T11:26:09.00023+01:00"))
                        .userLocalUpdatedAt(OffsetDateTime.parse("2020-09-21T18:21:34.45292+01:00"))
                        .build());


        //when listener receives a change event
        listener.receive("{" +
                "  \"id\": \"9\"," +
                "  \"name\": \"TEST_USER\"," +
                "  \"assignedApps\": [" +
                "    \"Notepad\"," +
                "    \"MediaCoder\"" +
                "  ]," +
                "  \"timestamp\": \"2020-10-09T12:56:09.90023+01:00\"" +
                "}");


        //then a correctly modified entity should be passed to the repository to be saved
        then(repo)
                .should()
                .save(appUserArgumentCaptor.capture());

        assertThat(appUserArgumentCaptor.getAllValues())
                .hasSize(1)
                .extracting(
                        AppUser::getId,
                        AppUser::getName,
                        s -> s.getAssignedApps().size(),
                        AppUser::getUserLocalCreatedAt,
                        AppUser::getUserLocalUpdatedAt
                )
                .containsExactly(
                        tuple(
                                9L,
                                "TEST_USER",
                                4,
                                OffsetDateTime.parse("2020-08-22T11:26:09.00023+01:00"),
                                OffsetDateTime.parse("2020-10-09T12:56:09.90023+01:00")

                        )
                );
    }

    @Test
    public void userIsSuccessfullyCreated() {
        given(repo.findUser(1L))
                .willReturn(null);


        //when listener receives a change event
        listener.receive("{" +
                "  \"id\": \"1\"," +
                "  \"name\": \"TEST_USER2\"," +
                "  \"assignedApps\": [" +
                "    \"Photoshop\"," +
                "    \"Eclipse\"," +
                "    \"Calc\"" +
                "  ]," +
                "  \"timestamp\": \"2020-09-22T18:29:39.23023+01:00\"" +
                "}");


        //then a correctly modified entity should be passed to the repository to be saved
        then(repo)
                .should()
                .save(appUserArgumentCaptor.capture());

        assertThat(appUserArgumentCaptor.getAllValues())
                .hasSize(1)
                .extracting(
                        AppUser::getId,
                        AppUser::getName,
                        AppUser::getAssignedApps,
                        AppUser::getUserLocalCreatedAt,
                        AppUser::getUserLocalUpdatedAt
                )
                .containsExactly(
                        tuple(
                                1L,
                                "TEST_USER2",
                                Arrays.asList("Photoshop", "Eclipse", "Calc"),
                                OffsetDateTime.parse("2020-09-22T18:29:39.23023+01:00"),
                                OffsetDateTime.parse("2020-09-22T18:29:39.23023+01:00")
                        )
                );
    }
}
