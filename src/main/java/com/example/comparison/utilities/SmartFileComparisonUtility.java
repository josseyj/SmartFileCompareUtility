package com.example.comparison.utilities;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.model.Record;
import com.example.comparison.repository.RecordRepository;
import com.example.comparison.repository.ComparisonResultRepository;
import com.example.comparison.utilities.internal.ComparisonReportGenerator;
import com.example.comparison.utilities.internal.RecordComparator;
import com.example.comparison.utilities.internal.RecordParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The smart file comparison utility.
 */
public class SmartFileComparisonUtility {

    private final RecordParser recordParser = new RecordParser();

    private final RecordComparator recordComparator = new RecordComparator();

    private final RecordRepository recordRepository;

    private final ComparisonResultRepository comparisonResultRepository;

    private final ComparisonReportGenerator comparisonReportGenerator;

    public SmartFileComparisonUtility(RecordRepository recordRepository, ComparisonResultRepository comparisonResultRepository) {
        this.recordRepository = recordRepository;
        this.comparisonResultRepository = comparisonResultRepository;
        this.comparisonReportGenerator = new ComparisonReportGenerator(comparisonResultRepository);
    }

    /**
     * Compares and generates report.
     *
     * @param expectedFilePath the expected records file path
     * @param actualFilePath the actual records file path
     * @param outputFilePath the output file path
     */
    public void compareAndGenerateReport(final String expectedFilePath, final String actualFilePath, final String outputFilePath) {
        parseRecords(expectedFilePath)
                .forEach(recordRepository::save);

        parseRecords(actualFilePath)
                .map(this::compareWithExpected)
                .forEach(comparisonResultRepository::saveComparisonResult);

        ComparisonReportGenerator comparisonReportGenerator = new ComparisonReportGenerator(comparisonResultRepository);
        comparisonReportGenerator.generateReport(outputFilePath);
    }

    private ComparisonResult compareWithExpected(Record record) {
        Optional<Record> expectedRecord = recordRepository.find(record.getSymbol());

        return expectedRecord
                .map(it -> recordComparator.compare(expectedRecord.get(), record))
                .orElse(ComparisonResult.builder()
                        .symbol(record.getSymbol())
                        .actual(record)
                        .mismatches(setOf(MismatchType.UNEXPECTED))
                        .build());
    }

    private Stream<Record> parseRecords(String expectedFilePath) {
        try {
            return Files.readAllLines(Paths.get(expectedFilePath)).stream()
                    .filter(line -> ! line.isEmpty())
                    .map(recordParser::parse);
        } catch (IOException ex) {
            throw new ComparisonException("Error while parsing the records.", ex);
        }
    }

    private static <T> Set<T> setOf(T... values) {
        return Stream.of(values).collect(Collectors.toSet());
    }

}
