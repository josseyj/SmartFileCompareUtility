package com.example.comparison.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
public class Result {

    @Singular
    private Set<Record> missingRecords = new HashSet<>();

    @Singular
    private Set<Record> unexpectedRecords = new HashSet<>();

    @Singular
    private final Set<Mismatch> mismatches;

    @Builder
    @Getter
    public static class Mismatch {

        private final Record actual;

        private final Record expected;

        private final Set<String> reasons;

    }

}
