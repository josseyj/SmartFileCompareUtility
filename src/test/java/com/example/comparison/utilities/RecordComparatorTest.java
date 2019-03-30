package com.example.comparison.utilities;

import com.example.comparison.model.ComparisonResult;
import com.example.comparison.model.MismatchType;
import com.example.comparison.model.Record;
import com.example.comparison.utilities.internal.RecordComparator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

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
                {new BigDecimal("10.004"), new BigDecimal("10"), false },
                {new BigDecimal("10.005"), new BigDecimal("10"), true },
                {new BigDecimal("10.0049"),new BigDecimal("10"),  false },
                {new BigDecimal("10.010"), new BigDecimal("10"), true },
        };
    }

    @Test(dataProvider = "testData")
    public void testComparePriceDifference(BigDecimal price1, BigDecimal price2, boolean expectedMismatch) {
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
        ComparisonResult result = recordComparator.compare(record1, record2);

        //then
        Assert.assertEquals(result.getMismatches().isEmpty(), !expectedMismatch, "Comparison result not as expected.");
        if (expectedMismatch) {
            Assert.assertTrue(result.getMismatches().contains(MismatchType.PRICE), "Only price mismatch expected.");
        }
    }

    @Test
    public void testCompareDifferences() {
        //given
        Record record1 = Record.builder()
                .symbol("A")
                .price(new BigDecimal("100"))
                .quantity(10)
                .comments("Test A")
                .build();

        Record record2 = Record.builder()
                .symbol("A")
                .price(new BigDecimal("200"))
                .quantity(15)
                .comments("Test B")
                .build();

        //when
        ComparisonResult result = recordComparator.compare(record1, record2);

        //then
        Assert.assertFalse(result.getMismatches().isEmpty(), "Expected mismatches.");
        Assert.assertEquals(result.getMismatches().size(), 3, "There should be 3 mismatches");
        Assert.assertTrue(result.getMismatches().contains(MismatchType.PRICE), "There should be a price mismatch");
        Assert.assertTrue(result.getMismatches().contains(MismatchType.QUANTITY), "There should be a quantity mismatch");
        Assert.assertTrue(result.getMismatches().contains(MismatchType.COMMENTS), "There should be a comments mismatch");
    }

}