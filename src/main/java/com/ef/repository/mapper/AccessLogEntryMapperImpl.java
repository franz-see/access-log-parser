package com.ef.repository.mapper;

import com.ef.domain.AccessLogEntry;
import com.ef.domain.PeriodSummary;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Singleton
public class AccessLogEntryMapperImpl implements AccessLogEntryMapper {

    private final SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings("unused")
    public AccessLogEntryMapperImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    private AccessLogEntryMapper getAccessLogEntryMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AccessLogEntryMapper.class);
    }

    @Override
    public void deleteAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getAccessLogEntryMapper(sqlSession).deleteAll();
            sqlSession.commit();
        }
    }

    @Override
    public void save(Collection<AccessLogEntry> accessLogEntries) {
        if (accessLogEntries.isEmpty()) {
            return;
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getAccessLogEntryMapper(sqlSession).save(accessLogEntries);
            sqlSession.commit();
        }
    }

    @Override
    public List<PeriodSummary> findHourlyCount(LocalDateTime startDateTime, int threshold) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getAccessLogEntryMapper(sqlSession).findHourlyCount(startDateTime, threshold);
        }
    }

    @Override
    public List<PeriodSummary> findDailyCount(LocalDateTime startDateTime, int threshold) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getAccessLogEntryMapper(sqlSession).findDailyCount(startDateTime, threshold);
        }
    }
}
