package com.example.comparison.repository.impl.inmemory;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.model.Record;
import com.example.comparison.repository.ComparisonResultRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class InMemoryComparisonResultRepository implements ComparisonResultRepository {

    private final InMemoryRecordRepository recordRepository;

    private final Map<String, ComparisonResult> mismatches = new HashMap<>();

    public void saveComparisonResult(ComparisonResult comparisonResult) {
        mismatches.put(comparisonResult.getSymbol(), comparisonResult);
    }

    @Override
    public Stream<ComparisonResult> findAllMismatches() {
        return Stream.concat(getMisMatches(),
                getMisses());
    }

    private Stream<ComparisonResult> getMisMatches() {
        return mismatches.values().stream()
                .filter(it -> ! it.getMismatches().isEmpty());
    }

    /**
     * The in memory record repository implementation is used to get the missing records.
     *
     * @return comparison results for missing records
     */
    private Stream<ComparisonResult> getMisses() {
        return recordRepository
                .findAll(it -> !mismatches.containsKey(it.getSymbol()))
                .map(it -> createMissingResult(it));
    }

    private static ComparisonResult createMissingResult(Record expectedResult) {
        return ComparisonResult.builder()
                .symbol(expectedResult.getSymbol())
                .expected(expectedResult)
                .mismatches(setOf(MismatchType.MISSING))
                .build();
    }

    private static <T> Set<T> setOf(T... values) {
        return Stream.of(values).collect(Collectors.toSet());
    }


}
