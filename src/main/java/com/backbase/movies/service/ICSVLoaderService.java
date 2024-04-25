package com.backbase.movies.service;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public interface ICSVLoaderService {

    void loadData() throws CsvValidationException, IOException;
}
