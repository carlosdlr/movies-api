package com.backbase.movies.service;

import com.backbase.movies.model.Award;
import com.backbase.movies.model.AwardRecord;
import com.backbase.movies.repository.AwardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.time.Year;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AwardServiceTest {

    @MockBean
    private AwardRepository awardRepository;

    @MockBean
    private ICSVLoaderService csvLoaderService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private AwardService awardService;

    @Test
    public void testCountDataRecords() {
        // given
        when(awardRepository.count()).thenReturn(10L);

        // when
        long count = awardService.countDataRecords();

        // then
        assertEquals(10L, count);
    }

    @Test
    public void testSaveAward() {
        // given
        AwardRecord awardRecord = new AwardRecord(Year.parse("2022"), "edition",
                "category", "nominee", "additionalInfo", true);
        Award award = new Award(1L, Year.parse("2022"), "edition",
                "category", "nominee", "additionalInfo", true);
        when(modelMapper.map(awardRecord, Award.class)).thenReturn(award);
        when(awardRepository.save(award)).thenReturn(award);

        // when
        Mono<AwardRecord> result = awardService.saveAward(awardRecord);

        // then
        AwardRecord savedAwardRecord = result.block();
        assertEquals(awardRecord, savedAwardRecord);
    }

    @Test
    public void testFindAwardById() {
        // given
        Long id = 1L;
        Award award = new Award(id, Year.parse("2022"), "edition",
                "category", "nominee", "additionalInfo", true);
        when(awardRepository.findById(id)).thenReturn(Optional.of(award));

        // when
        Optional<AwardRecord> result = awardService.findAwardById(id);

        // then
        assertTrue(result.isPresent());
        AwardRecord awardRecord = result.get();
        assertEquals(award.getYear(), awardRecord.year());
        assertEquals(award.getCategory(), awardRecord.category());
        assertEquals(award.getNominee(), awardRecord.nominee());
        assertEquals(award.getAdditionalInfo(), awardRecord.additionalInfo());
        assertEquals(award.getWon(), awardRecord.won());
    }

    @Test
    public void testDeleteAwardById() {
        // given
        Long id = 1L;
        Award award = new Award(id, Year.parse("2022"), "edition",
                "category", "nominee", "additionalInfo", true);
        when(awardRepository.findById(id)).thenReturn(Optional.of(award));

        // when
        Mono<Void> result = awardService.deleteAwardById(id);

        // then
        verify(awardRepository, times(1)).delete(award);
    }

    @Test
    public void testUpdateAward() {
        // given
        Long id = 1L;
        AwardRecord awardRecord = new AwardRecord(Year.parse("2022"), "edition",
                "category", "nominee", "additionalInfo", true);
        Award existingAward = new Award(id, Year.parse("2022"), "edition",
                "category", "nominee", "additionalInfo", true);
        when(awardRepository.findById(id)).thenReturn(Optional.of(existingAward));

        // when
        Mono<AwardRecord> result = awardService.updateAward(id, awardRecord);

        // then
        AwardRecord updatedAwardRecord = result.block();
        assertEquals(awardRecord.year(), updatedAwardRecord.year());
        assertEquals(awardRecord.category(), updatedAwardRecord.category());
        assertEquals(awardRecord.nominee(), updatedAwardRecord.nominee());
        assertEquals(awardRecord.additionalInfo(), updatedAwardRecord.additionalInfo());
        assertEquals(awardRecord.won(), updatedAwardRecord.won());
    }

}