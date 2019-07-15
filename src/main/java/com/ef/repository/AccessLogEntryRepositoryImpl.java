package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.domain.PeriodSummary;
import com.ef.repository.mapper.AccessLogEntryMapper;

import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    @Override
    public List<PeriodSummary> findHourlyCount(LocalDateTime startDateTime, int threshold) {
        return accessLogEntryMapper.findHourlyCount(startDateTime, threshold);
    }

    @Override
    public List<PeriodSummary> findDailyCount(LocalDateTime startDateTime, int threshold) {
        return accessLogEntryMapper.findDailyCount(startDateTime, threshold);
    }
}
