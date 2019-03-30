package com.example.comparison.utilities;

import com.example.comparison.model.Record;
import com.example.comparison.utilities.internal.RecordParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class RecordParserTest {

    @Test
    public void testParseBuyRecord() {
        //given
        String recordLine = "AAPL 001000 00190.5000 Buying Apple.";
        RecordParser recordParser = new RecordParser();

        //when
        Record record = recordParser.parse(recordLine);

        //then
        Assert.assertEquals(record.getSymbol(), "AAPL");
        Assert.assertEquals(record.getQuantity().intValue(), 1000);
        Assert.assertEquals(record.getPrice().setScale(4), new BigDecimal(00190.5000).setScale(4));
        Assert.assertEquals(record.getComments(), "Buying Apple.");
    }

    @Test
    public void testParseSellRecord() {
        //given
        String recordLine = "MSFT -01000 00101.2000 Selling Microsoft.";
        RecordParser recordParser = new RecordParser();

        //when
        Record record = recordParser.parse(recordLine);

        //then
        Assert.assertEquals(record.getSymbol(), "MSFT");
        Assert.assertEquals(record.getQuantity().intValue(), -1000);
        Assert.assertEquals(record.getPrice().setScale(4), new BigDecimal("00101.2000").setScale(4));
        Assert.assertEquals(record.getComments(), "Selling Microsoft.");
    }

}