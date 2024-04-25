package com.backbase.movies.service;

import com.backbase.movies.model.Award;
import com.backbase.movies.model.AwardRecord;
import com.backbase.movies.repository.AwardRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Log4j2
public class AwardService implements IAwardService {

    private final AwardRepository awardRepository;
    private final ModelMapper modelMapper;

    // injection by constructor
    public AwardService(AwardRepository awardRepository, ModelMapper modelMapper) {
        this.awardRepository = awardRepository;
        this.modelMapper = modelMapper;
    }

    private AwardRecord toAwardRecord(Award award) {
        return new AwardRecord(award.getYear(), award.getEdition(),
                award.getCategory(), award.getNominee(), award.getAdditionalInfo(),
                award.getWon());
    }

    private void setAward(Award award, AwardRecord awardRecord) {
        award.setYear(awardRecord.year() != null ? awardRecord.year() : award.getYear());
        award.setCategory(awardRecord.category() != null ? awardRecord.category() : award.getCategory());
        award.setNominee(awardRecord.nominee() != null ? awardRecord.nominee() : award.getNominee());
        award.setNominee(awardRecord.nominee() != null ? awardRecord.nominee() : award.getNominee());
        award.setAdditionalInfo(awardRecord.additionalInfo() != null ? awardRecord.additionalInfo() : award.getAdditionalInfo());
        award.setWon(awardRecord.won() != null ? awardRecord.won() : award.getWon());
    }

    @Override
    public long countDataRecords() {
        return awardRepository.count();
    }

    @Override
    public Mono<AwardRecord> saveAward(AwardRecord awardRecord) {
        awardRepository.save(modelMapper.map(awardRecord, Award.class));
        log.info("Saved award: {}", awardRecord);
        return Mono.just(awardRecord);
    }

    @Override
    public Optional<AwardRecord> findAwardById(Long id) {
        return Optional.of(awardRepository.findById(id)
                .map(award -> toAwardRecord(award))
                .orElseThrow());
    }

    @Override
    public Mono<Void> deleteAwardById(Long id) {
        var award = awardRepository.findById(id);
        award.ifPresent(a -> awardRepository.delete(a));
        log.info("Deleted award: {}", award);
        return Mono.empty();
    }

    @Override
    public Mono<AwardRecord> updateAward(Long id, AwardRecord awardRecord) {
        var existingAward = awardRepository.findById(id);
        existingAward.ifPresent(a -> {
            setAward(a, awardRecord);
            awardRepository.save(a);
        });

        log.info("Updated award: {}", existingAward);
        return Mono.just(toAwardRecord(existingAward.get()));
    }

    @Override
    public Flux<AwardRecord> findAllAwards() {
        return Flux.fromStream(awardRepository.findAll(PageRequest.of(1,100))
                .stream()
                .map( award -> toAwardRecord(award)));
    }
}
