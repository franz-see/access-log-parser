package com.ef.repository;

import com.ef.domain.AccessLogEntry;

public interface AccessLogEntryRepository {

    AccessLogEntry save(AccessLogEntry accessLogEntry);
}
