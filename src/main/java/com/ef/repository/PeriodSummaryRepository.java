package com.ef.repository;

import com.ef.domain.PeriodSummary;

import java.util.Collection;

public interface PeriodSummaryRepository {

    void deleteAll();

    void save(Collection<PeriodSummary> periodSummaries);

}
