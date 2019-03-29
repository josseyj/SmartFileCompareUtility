package com.example.comparison.repository;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.model.Record;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class InMemoryResultRepository implements ResultRepository {

    private final Map<String, ComparisonResult> mismatches = new HashMap<>();

    public void saveComparisonResult(Record actualRecord, ComparisonResult comparisonResult) {
        mismatches.put(actualRecord.getSymbol(), comparisonResult);
    }

    public void saveMatch(Record actualRecord, Record expectedRecord) {
        mismatches.put(actualRecord.getSymbol(), ComparisonResult.builder().actual(actualRecord).build());
    }

    public void saveUnexpectedRecord(Record actualRecord) {
        mismatches.put(actualRecord.getSymbol(), ComparisonResult.builder().actual(actualRecord).reasons(setOf(MismatchType.UNEXPECTED)).build());
    }

    public void saveMissingRecord(Record expectedRecord) {
        mismatches.put(expectedRecord.getSymbol(), ComparisonResult.builder().expected(expectedRecord).reasons(setOf(MismatchType.MISSING)).build());
    }

    @Override
    public boolean doesMatchResultExist(String symbol) {
        return mismatches.containsKey(symbol);
    }

    @Override
    public Stream<ComparisonResult> getMisMatches() {
        return mismatches.values().stream()
                .filter(it -> it.getReasons().isEmpty());
    }

    private Set<MismatchType> setOf(MismatchType... values) {
        return Stream.of(values).collect(Collectors.toSet());
    }


}
