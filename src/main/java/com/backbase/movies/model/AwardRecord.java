package com.backbase.movies.model;

import java.time.Year;

public record AwardRecord(Year year,
                          String edition,
                          String category,
                          String nominee,
                          String additionalInfo,
                          Boolean won) {}