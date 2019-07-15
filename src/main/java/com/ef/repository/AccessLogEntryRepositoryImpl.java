package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.repository.mapper.AccessLogEntryMapper;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class AccessLogEntryRepositoryImpl implements AccessLogEntryRepository {

    private final AccessLogEntryMapper accessLogEntryMapper;

    @SuppressWarnings("unused")
    public AccessLogEntryRepositoryImpl(AccessLogEntryMapper accessLogEntryMapper) {
        this.accessLogEntryMapper = accessLogEntryMapper;
    }

    @Override
    public void deleteAll() {
        accessLogEntryMapper.deleteAll();
    }

    @Override
    public void save(Collection<AccessLogEntry> accessLogEntries) {
        accessLogEntryMapper.save(accessLogEntries);
    }
}
