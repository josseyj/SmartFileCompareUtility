package com.example.comparison.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
public class ComparisonResult {

    private final Record actual;

    private final Record expected;

    @Builder.Default
    private final Set<MismatchType> reasons = new HashSet<>();

}
