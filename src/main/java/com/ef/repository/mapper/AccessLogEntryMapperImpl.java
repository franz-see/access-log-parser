package com.ef.repository.mapper;

import com.ef.domain.AccessLogEntry;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.inject.Singleton;
import java.util.Collection;

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
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getAccessLogEntryMapper(sqlSession).save(accessLogEntries);
            sqlSession.commit();
        }
    }
}
