package com.backbase.movies.controller;

import com.backbase.movies.model.AwardRecord;
import com.backbase.movies.service.IAwardService;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Year;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AwardController.class)
@AutoConfigureWebTestClient
@WithMockUser(username = "admin", password = "admin")
@Log4j2
public class AwardControllerTest {

    @MockBean
    private IAwardService awardService;

    @Autowired
    private WebTestClient webTestClient;

//    @MockBean
//    private JwtTokenService jwtTokenService;


    @Test
    public void testCreateAward() {
        // Given
        AwardRecord awardRecord = new AwardRecord(Year.parse("2010"), "category", "winner",
                "nominee", "test", true);
        when(awardService.saveAward(awardRecord)).thenReturn(Mono.just(awardRecord));

        // When
        webTestClient.post().uri("/api/v1/awards").contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(awardRecord),AwardRecord.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.category").isEqualTo("winner");

    }

    @Test
    public void testFindAwardById_Found() {
        // Given
        Long id = 1L;
        AwardRecord awardRecord = new AwardRecord(Year.parse("2010"), "category", "winner",
                "nominee","test", true);
        when(awardService.findAwardById(id)).thenReturn(Optional.of(awardRecord));

        // When
         webTestClient.get().uri("/api/v1/awards/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.year").isEqualTo("2010");
    }

    @Test
    public void testFindAwardById_NotFound() {
        // Given
        Long id = 6L;
        when(awardService.findAwardById(id)).thenReturn(Optional.empty());

        // When
        webTestClient.get().uri("/api/v1/awards/{id}", 6)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    public void testFindAllAwards_Found() {
        // Given
        Flux<AwardRecord> awardRecords = Flux.just(new AwardRecord(Year.parse("2010"), "category1", "winner1",
                        "nominee1", "test", true),
                new AwardRecord(Year.parse("2011"), "category2", "winner2",
                        "nominee2","test", true));
        when(awardService.findAllAwards()).thenReturn(awardRecords);

        // When
        webTestClient.get().uri("/api/v1/awards").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(AwardRecord.class)
                .hasSize(2)
                .consumeWith(award ->{
                    List<AwardRecord> awards = award.getResponseBody();
                    awards.forEach( a ->{
                        assertTrue(a.won() != null);
                    });
                });

    }


    @Test
    public void testUpdateAward() {
        // Given
        Long id = 1L;
        AwardRecord awardRecord = new AwardRecord(Year.parse("2010"), "category", "winner",
                "nominee", "test", true);
        when(awardService.updateAward(id, awardRecord)).thenReturn(Mono.just(awardRecord));

        // When
        webTestClient.put().uri("/api/v1/awards/{id}",id)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(awardRecord),AwardRecord.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.won").isEqualTo(true);
    }

    @Test
    public void testDeleteAward() {
        // Given
        Long id = 1L;
        when(awardService.deleteAwardById(id)).thenReturn(Mono.empty());

        // When
        webTestClient.delete().uri("/api/v1/awards/{id}",id)
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }
}
