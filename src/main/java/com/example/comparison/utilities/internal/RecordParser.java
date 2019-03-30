package com.example.comparison.utilities.internal;

import com.example.comparison.model.Record;
import com.example.comparison.utilities.RecordParsingException;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordParser {

//    format: <4 char Ticker> <6 char Quantity> <Price: NNNNN.NNNN> <Note till EOL>
    private final Pattern RECORD_PATTERN = Pattern.compile("^(?<ticker>.{4})" +
        " (?<quantity>[-+]?[0-9]+)" +
        " (?<price>.+?)" +
        "( (?<comments>.*)?)$");

    public Record parse(String recordLine) {
        Matcher matcher = RECORD_PATTERN.matcher(recordLine);
        if (!matcher.matches()) {
            throw new RecordParsingException(String.format("Record line did not match the expecetd format: [%s]", recordLine));
        }
        return Record.builder()
                .symbol(matcher.group("ticker").trim())
                .quantity(Integer.parseInt(matcher.group("quantity")))
                .price(new BigDecimal(matcher.group("price")))
                .comments(matcher.group("comments"))
                .build();
    }
}
