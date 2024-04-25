package com.backbase.movies.controller;

import com.backbase.movies.model.AwardRecord;
import com.backbase.movies.service.IAwardService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/awards")
public class AwardController {

    private final IAwardService awardService;

    public AwardController(IAwardService awardService) {
        this.awardService = awardService;
    }

    // Create a single award using Mono
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AwardRecord> createAward(@RequestBody AwardRecord awardRecord) {
        return awardService.saveAward(awardRecord);
    }

    // Retrieve a single award using Mono
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AwardRecord> findAwardById(@PathVariable Long id) {
            return awardService.findAwardById(id).map(awardRecord -> Mono.just(awardRecord))
                    .orElseThrow(() -> new NoSuchElementException());
    }

    // Retrieve multiple awards using Flux
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<AwardRecord> findAllAwards() {
        return awardService.findAllAwards();
    }


    // Update a single award using Mono
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AwardRecord> updateAward(@PathVariable Long id, @RequestBody AwardRecord awardRecord) {
        return awardService.updateAward(id, awardRecord);
    }

    // Delete a single award using Mono
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> deleteAward(@PathVariable Long id) {
        return awardService.deleteAwardById(id);
    }
}
