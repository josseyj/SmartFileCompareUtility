package com.example.comparison.repository;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.model.Record;

import java.util.Optional;
import java.util.stream.Stream;

public interface ResultRepository {

    void saveComparisonResult(Record actualRecord, ComparisonResult comparisonResult);

    void saveMatch(Record actualRecord, Record expectedRecord);

    void saveUnexpectedRecord(Record actualRecord);

    void saveMissingRecord(Record expectedRecord);

    boolean doesMatchResultExist(String symbol);

    Stream<ComparisonResult> getMisMatches();

}
