package com.ef.repository;

import com.ef.domain.PeriodSummary;
import com.ef.repository.mapper.PeriodSummaryMapper;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class PeriodSummaryRepositoryImpl implements PeriodSummaryRepository {

    private final PeriodSummaryMapper periodSummaryMapper;

    @SuppressWarnings("unused")
    public PeriodSummaryRepositoryImpl(PeriodSummaryMapper periodSummaryMapper) {
        this.periodSummaryMapper = periodSummaryMapper;
    }

    @Override
    public void deleteAll() {
        periodSummaryMapper.deleteAll();
    }

    @Override
    public void save(Collection<PeriodSummary> periodSummaries) {
        periodSummaryMapper.save(periodSummaries);
    }
    
}
