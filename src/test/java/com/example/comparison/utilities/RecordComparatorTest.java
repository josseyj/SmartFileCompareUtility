package com.example.comparison.utilities;

import com.example.comparison.model.Record;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

public class RecordComparatorTest {

    private final RecordComparator recordComparator = new RecordComparator();

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {new BigDecimal("10"), new BigDecimal("10"), false },
                {new BigDecimal("10"), new BigDecimal("10.004"), false },
                {new BigDecimal("10"), new BigDecimal("10.005"), true },
                {new BigDecimal("10"), new BigDecimal("10.0049"), false },
                {new BigDecimal("10"), new BigDecimal("10.010"), true },
        };
    }

    @Test(dataProvider = "testData")
    public void testCompareNonSignificantPriceDifference(BigDecimal price1, BigDecimal price2, boolean expectedMismatch) {
        //given
        Record record1 = Record.builder()
                .symbol("A")
                .price(price1)
                .quantity(5)
                .comments("Test")
                .build();

        Record record2 = Record.builder()
                .symbol("A")
                .price(price2)
                .quantity(5)
                .comments("Test")
                .build();

        //when
        Optional<Result.Mismatch> result = recordComparator.compare(record1, record2);

        //then
        Assert.assertEquals(result.isPresent(), expectedMismatch);
    }
}