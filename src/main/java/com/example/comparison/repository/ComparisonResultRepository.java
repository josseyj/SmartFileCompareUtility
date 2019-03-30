package com.example.comparison.repository;

import com.example.comparison.model.ComparisonResult;

import java.util.stream.Stream;

/**
 * The repository to save the comparison result.
 */
public interface ComparisonResultRepository {

    /**
     * Saves comparison result.
     *
     * @param comparisonResult the result
     */
    void saveComparisonResult(ComparisonResult comparisonResult);

    /**
     * Find all mismatches including unexpected records and missing records.
     *
     * @return a stream of results
     */
    Stream<ComparisonResult> findAllMismatches();

}
