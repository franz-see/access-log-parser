package com.ef.repository.mapper;

import com.ef.domain.AccessLogEntry;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.inject.Singleton;

@Singleton
public class AccessLogEntryMapperImpl implements AccessLogEntryMapper {

    private final SqlSessionFactory sqlSessionFactory;

    public AccessLogEntryMapperImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    private AccessLogEntryMapper getAccessLogEntryMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AccessLogEntryMapper.class);
    }

    @Override
    public Long save(AccessLogEntry accessLogEntry) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Long id = getAccessLogEntryMapper(sqlSession).save(accessLogEntry);
            sqlSession.commit();
            return id;
        }
    }
}
