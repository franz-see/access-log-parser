package com.ef.repository;

import com.ef.domain.PeriodSummary;

import java.util.Collection;
import java.util.List;

public interface PeriodSummaryRepository {

    void deleteAll();

    void save(Collection<PeriodSummary> periodSummaries);

    List<PeriodSummary> findAll();
}
