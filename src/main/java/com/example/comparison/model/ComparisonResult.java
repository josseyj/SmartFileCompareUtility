package com.example.comparison.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
/**
 * Defines a record comparison result.
 * If the mismatches are empty, that means the records match.
 */
public class ComparisonResult {

    private final String symbol;

    private final Record actual;

    private final Record expected;

    @Builder.Default
    private final Set<MismatchType> mismatches = new HashSet<>();

}
