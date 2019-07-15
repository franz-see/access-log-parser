package com.ef.repository;

import com.ef.domain.AccessLogEntry;

import java.util.Collection;

public interface AccessLogEntryRepository {

    void deleteAll();

    void save(Collection<AccessLogEntry> accessLogEntries);
}
