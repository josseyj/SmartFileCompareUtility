package com.example.comparison.repository.impl.inmemory;

import com.example.comparison.model.Record;
import com.example.comparison.repository.RecordRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class InMemoryRecordRepository implements RecordRepository {

    private Map<String, Record> records = new HashMap<>();

    @Override
    public void save(Record record) {
        records.put(record.getSymbol(), record);
    }

    @Override
    public Optional<Record> find(String symbol) {
        return Optional.ofNullable(records.get(symbol));
    }

    public Stream<Record> findAll(Predicate<Record> condition) {
        return records.values().stream()
                .filter(condition);
    }

}
