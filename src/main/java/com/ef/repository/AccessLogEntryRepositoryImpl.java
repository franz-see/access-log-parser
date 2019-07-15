package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.repository.mapper.AccessLogEntryMapper;

import javax.inject.Singleton;

@Singleton
public class AccessLogEntryRepositoryImpl implements AccessLogEntryRepository {

    private final AccessLogEntryMapper accessLogEntryMapper;

    public AccessLogEntryRepositoryImpl(AccessLogEntryMapper accessLogEntryMapper) {
        this.accessLogEntryMapper = accessLogEntryMapper;
    }

    @Override
    public AccessLogEntry save(AccessLogEntry accessLogEntry) {
        Long id = accessLogEntryMapper.save(accessLogEntry);
        accessLogEntry.setId(id);
        return accessLogEntry;
    }
}
