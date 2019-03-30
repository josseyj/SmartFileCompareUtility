package com.example.comparison.utilities.internal;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.repository.ComparisonResultRepository;
import com.example.comparison.utilities.ReportGenerationException;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ComparisonReportGenerator {

    private final ComparisonResultRepository comparisonResultRepository;

    public void generateReport(String reportFile) {
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFile), StandardCharsets.UTF_8)) {
            comparisonResultRepository.findAllMismatches()
                    .map(ComparisonReportGenerator::reportLine)
                    .filter(reportLine -> ! reportLine.isEmpty())
                    .map(ComparisonReportGenerator::addNewLine)
                    .forEach(reportLine -> write(writer, reportLine));
        } catch (IOException ex) {
            throw new ReportGenerationException("Error while generating report.", ex);
        }
    }

    private static String reportLine(ComparisonResult comparisonResult) {
        Set<MismatchType> reasons = comparisonResult.getMismatches();
        if (reasons.isEmpty()) {
            return "";
        }

        if (reasons.contains(MismatchType.MISSING)) {
            return String.format("missing line for %s", comparisonResult.getSymbol());

        } else if (reasons.contains(MismatchType.UNEXPECTED)) {
            return String.format("unexpected line for %s", comparisonResult.getSymbol());

        } else {
            return String.format("mismatch for %s (%s)", comparisonResult.getSymbol(), reasonsText(comparisonResult));
        }
    }

    private static String reasonsText(ComparisonResult comparisonResult) {
        Set<MismatchType> reasons = comparisonResult.getMismatches();
        return reasons.stream()
                .map(mismatchType -> reasonsText(comparisonResult, mismatchType))
                .filter(text -> ! text.isEmpty())
                .collect(Collectors.joining(", "));
    }

    private static String reasonsText(ComparisonResult comparisonResult, MismatchType mismatchType) {
        switch (mismatchType) {
            case PRICE:
                return String.format("Expected Price: %010.04f, Actual Price: %010.04f",
                        comparisonResult.getExpected().getPrice(), comparisonResult.getActual().getPrice());
            case COMMENTS:
                return String.format("Expected Comments: %s, Actual Comments: %s",
                        comparisonResult.getExpected().getComments(), comparisonResult.getActual().getComments());
            case QUANTITY:
                return String.format("Expected Quantity: %d, Actual Quantity: %d",
                        comparisonResult.getExpected().getQuantity(), comparisonResult.getActual().getQuantity());
            default:
                return "";
        }
    }

    private static void write(BufferedWriter writer, String reportLine) {
        try {
            writer.write(reportLine);
        } catch (IOException ex) {
            throw new ReportGenerationException("Error while writing report.", ex);
        }
    }

    private static String addNewLine(String input) {
        return input + System.lineSeparator();
    }

}
