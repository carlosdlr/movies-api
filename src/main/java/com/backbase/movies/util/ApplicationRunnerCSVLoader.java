package com.backbase.movies.util;

import com.backbase.movies.service.ICSVLoaderService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerCSVLoader implements ApplicationRunner {

    private final ICSVLoaderService csvLoaderService;

    public ApplicationRunnerCSVLoader(ICSVLoaderService csvLoaderService) {
        this.csvLoaderService = csvLoaderService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.csvLoaderService.loadData();
    }

}
