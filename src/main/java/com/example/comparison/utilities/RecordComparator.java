package com.example.comparison.utilities;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.model.Record;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RecordComparator {

    private static final PriceComparator PRICE_COMPARATOR = new PriceComparator();

    public ComparisonResult compare(Record expected, Record actual) {
        Set<MismatchType> mismatches = new HashSet<>();
        if (! expected.getQuantity().equals(actual.getQuantity())) {
//            mismatches.add(String.format("Expected Quantity: %d, Actual Quantity: %d",
//                    expected.getQuantity(), actual.getQuantity()));
            mismatches.add(MismatchType.QUANTITY);
        }
        if (! expected.getComments().equals(actual.getComments())) {
//            mismatches.add(String.format("Expected Comment: %s, Actual Comment: %s",
//                    expected.getComments(), actual.getComments()));
            mismatches.add(MismatchType.COMMENTS);
        }
        if (PRICE_COMPARATOR.compare(expected.getPrice(), actual.getPrice()) != 0) {
//            mismatches.add(String.format("Expected Price: %s, Actual Price: %s",
//                    expected.getPrice(), actual.getPrice()));
            mismatches.add(MismatchType.PRICE);
        }
        return ComparisonResult.builder()
                .actual(actual)
                .expected(expected)
                .reasons(mismatches)
                .build();
        }

    private static class PriceComparator implements Comparator<BigDecimal> {

        private BigDecimal threshold = new BigDecimal("0.005");

        @Override
        public int compare(BigDecimal o1, BigDecimal o2) {
            BigDecimal difference = o1.subtract(o2);
            if (difference.abs().compareTo(threshold) >= 0) {
                return difference.signum();
            } else {
                return 0;
            }
        }
    }

}
