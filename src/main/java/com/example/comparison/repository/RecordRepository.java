package com.example.comparison.repository;

import com.example.comparison.model.Record;

import java.util.Optional;

public interface RecordRepository {

    void save (Record record);

    Optional<Record> find(String symbol);
}
