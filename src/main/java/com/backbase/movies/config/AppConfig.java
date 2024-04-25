package com.backbase.movies.config;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.record.RecordModule;
import org.modelmapper.record.RecordValueReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;

@Configuration
public class AppConfig {

    @Value("${csv-file.path}")
    private String fileName;


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper.registerModule(new RecordModule());
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        return modelMapper;
    }

    @Bean
    public CSVReader csvReader() throws FileNotFoundException {
        return new CSVReaderBuilder(new FileReader(fileName))
                .withSkipLines(1) // Skip header lines
                .build();
    }
}
