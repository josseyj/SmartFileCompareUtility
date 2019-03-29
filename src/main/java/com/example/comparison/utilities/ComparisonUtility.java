package com.example.comparison.utilities;

import com.example.comparison.model.Record;
import com.example.comparison.model.Result;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class ComparisonUtility {

    private final RecordParser recordParser = new RecordParser();

    private final RecordComparator recordComparator = new RecordComparator();

    public Result compare(final String expectedFilePath, final String actualFilePath) {
        Result.ResultBuilder resultBuilder = Result.builder();
        Map<String, Record> expectedRecords = parseRecords(expectedFilePath);
        Map<String, Record> actualRecords = parseRecords(actualFilePath);
        for (Map.Entry<String, Record> actualRecord : actualRecords.entrySet()) {
            Record expectedRecord = expectedRecords.get(actualRecord.getKey());
            if (expectedRecord != null) {
                expectedRecords.remove(actualRecord.getValue());
                Optional<Result.Mismatch> mismatch = recordComparator.compare(actualRecord.getValue(), expectedRecord);
                if (mismatch.isPresent()) {
                    resultBuilder.mismatch(mismatch.get());
                }
            } else {
               resultBuilder.unexpectedRecord(actualRecord.getValue());
            }
        }
        resultBuilder.missingRecords(expectedRecords.values());
        return resultBuilder.build();
    }


    private Map<String, Record> parseRecords(String expectedFilePath) {
        try {
            return Files.readAllLines(Paths.get(expectedFilePath)).stream()
                    .map(recordParser::parse)
                    .collect(toMap(Record::getSymbol, identity()));
        } catch (IOException ex) {
            throw new ComparisonException("Error while parsing the records.", ex);
        }
    }

}
