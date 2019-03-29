package com.example.comparison.repository;

import com.example.comparison.model.Record;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface RecordRepository {

    void save (Record record);

    Optional<Record> find(String symbol);

    Stream<Record> findAll(Predicate<Record> condition);
}
