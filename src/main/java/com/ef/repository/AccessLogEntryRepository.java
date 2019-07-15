package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.domain.PeriodSummary;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AccessLogEntryRepository {

    void deleteAll();

    void save(Collection<AccessLogEntry> accessLogEntries);

    List<PeriodSummary> findHourlyCount(LocalDateTime startDateTime, int threshold);

    List<PeriodSummary> findDailyCount(LocalDateTime startDateTime, int threshold);
}
