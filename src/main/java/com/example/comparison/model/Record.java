package com.example.comparison.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class Record {

    private final String symbol;

    private final String comments;

    private final BigDecimal price;

    private final Integer quantity;

}
