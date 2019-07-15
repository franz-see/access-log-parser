package com.ef.repository.mapper;

import com.ef.domain.PeriodSummary;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class PeriodSummaryMapperImpl implements PeriodSummaryMapper {

    private final SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings("unused")
    public PeriodSummaryMapperImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    private PeriodSummaryMapper getPeriodSummaryMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(PeriodSummaryMapper.class);
    }

    @Override
    public void deleteAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getPeriodSummaryMapper(sqlSession).deleteAll();
            sqlSession.commit();
        }
    }

    @Override
    public void save(Collection<PeriodSummary> periodSummaries) {
        if (periodSummaries.isEmpty()) {
            return;
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getPeriodSummaryMapper(sqlSession).save(periodSummaries);
            sqlSession.commit();
        }
    }
}
