package com.backbase.movies.service;

import com.backbase.movies.model.AwardRecord;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Year;


@Service
@Log4j2
public class CSVLoaderService implements ICSVLoaderService {

    private final IAwardService awardService;
    private final CSVReader csvReader;

    public CSVLoaderService(IAwardService awardService, CSVReader csvReader) {
        this.awardService = awardService;
        this.csvReader = csvReader;
    }

    private boolean isDataPresent() {
        return awardService.countDataRecords() > 0;
    }

    public void loadData() throws CsvValidationException, IOException {
        if (isDataPresent())
            return;

        String [] nextLine;
        while((nextLine=this.csvReader.readNext())!=null){
            try {
                awardService.saveAward(getAwardRecordFromCSV(nextLine));
            } catch (Exception e) {
                log.error(e);
            }
            log.info(nextLine);
        }

        csvReader.close();
    }

    private AwardRecord getAwardRecordFromCSV(String [] data) {
        var awardYearAndEdition = data[0].split("/");
        awardYearAndEdition = awardYearAndEdition[0].split(" ");
        return new AwardRecord(Year.parse(awardYearAndEdition[0]),
                awardYearAndEdition[1], data[1], data[2], data[3], Boolean.parseBoolean(data[4]));
    }

}
