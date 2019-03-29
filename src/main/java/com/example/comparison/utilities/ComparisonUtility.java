package com.example.comparison.utilities;

import com.example.comparison.model.Record;
import com.example.comparison.repository.RecordRepository;
import com.example.comparison.repository.ResultRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class ComparisonUtility {

    private final RecordParser recordParser = new RecordParser();

    private final RecordComparator recordComparator = new RecordComparator();

    private final RecordRepository recordRepository;

    private final ResultRepository resultRepository;

    public Result compare(final String expectedFilePath, final String actualFilePath) {
        parseRecords(expectedFilePath)
                .forEach(recordRepository::save);

        parseRecords(actualFilePath)
                .forEach(this::compareAndSaveMatchResult);

        findAndSaveMissingRecords();

        return resultRepository.getResult();
    }

    private void findAndSaveMissingRecords() {
        recordRepository
                .findAll(it -> ! resultRepository.doesMatchResultExist(it.getSymbol()))
                .forEach(it -> resultRepository.resultExists(it));
    }

    private void compareAndSaveMatchResult(Record record) {
        Optional<Record> expectedRecord = recordRepository.find(record.getSymbol());

        Optional<Result.Mismatch> mismatch = expectedRecord
                .flatMap(it -> recordComparator.compare(record, it));

        resultRepository.saveMatchResult(record, expectedRecord, mismatch);
    }


    private Stream<Record> parseRecords(String expectedFilePath) {
        try {
            return Files.readAllLines(Paths.get(expectedFilePath)).stream()
                    .map(recordParser::parse);
        } catch (IOException ex) {
            throw new ComparisonException("Error while parsing the records.", ex);
        }
    }

}
