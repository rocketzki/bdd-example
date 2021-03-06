package com.rocketzki.bddexample;


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
public class NotEntirelyCorrectUnitTestingTest {


    @Mock
    private InMemoryAppUserRepo repo;

    @InjectMocks
    private UserAdminProcessor processor;

    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;


    @Test
    public void userPropertiesAreSuccessfullyUpdated() {
        given(repo.findUser(9L))
                .willReturn(AppUser.builder()
                        .id(9L)
                        .name("TEST_USER")
                        .assignedApps(new ArrayList<>(Arrays.asList("GameFactor", "AdobeXD")))
                        .userLocalCreatedAt(OffsetDateTime.parse("2020-08-22T11:26:09.00023+01:00"))
                        .userLocalUpdatedAt(OffsetDateTime.parse("2020-09-23T23:21:34.45292+01:00"))
                        .build());


        //when listener receives a change event
        processor.process(AppUserChangeEvent.builder()
                .id(9L)
                .name("TEST_USER")
                .assignedApps(Arrays.asList("Notepad", "vi"))
                .timestamp(OffsetDateTime.parse("2020-09-28T18:21:34.45292+01:00"))
                .build());


        //then a correctly modified entity should be passed to the repository to be saved
        then(repo)
                .should()
                .save(appUserArgumentCaptor.capture());

        assertThat(appUserArgumentCaptor.getAllValues())
                .hasSize(1)
                .extracting(
                        AppUser::getId,
                        AppUser::getName,
                        user -> user.getAssignedApps().size(),
                        AppUser::getUserLocalCreatedAt,
                        AppUser::getUserLocalUpdatedAt
                )
                .containsExactly(
                        tuple(
                                9L,
                                "TEST_USER",
                                4,
                                OffsetDateTime.parse("2020-08-22T11:26:09.00023+01:00"),
                                OffsetDateTime.parse("2020-09-28T18:21:34.45292+01:00")

                        )
                );


    }

    @Test
    public void userIsSuccessfullyCreated() {
        given(repo.findUser(1L))
                .willReturn(null);


        //when listener receives a change event
        processor.process(AppUserChangeEvent.builder()
                .id(1L)
                .name("TEST_USER2")
                .assignedApps(Arrays.asList("Photoshop", "Eclipse", "Calc"))
                .timestamp(OffsetDateTime.parse("2020-09-22T18:29:39.23023+01:00"))
                .build());


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
