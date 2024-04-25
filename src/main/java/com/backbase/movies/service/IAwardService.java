package com.backbase.movies.service;

import com.backbase.movies.model.AwardRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IAwardService {
    Mono<AwardRecord> saveAward(AwardRecord awardRecord);

    Optional<AwardRecord> findAwardById(Long id);

    Mono<Void> deleteAwardById(Long id);

    Mono<AwardRecord> updateAward(Long id, AwardRecord awardRecord);

    long countDataRecords();

    Flux<AwardRecord> findAllAwards();
}
