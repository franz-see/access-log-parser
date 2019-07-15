package com.ef.repository.mapper;

import com.ef.domain.AccessLogEntry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface AccessLogEntryMapper {

    @Insert("insert into access_log_entry(" +
            "timestamp, ip_address, http_call, http_status_code, user_agent) " +
            "values(" +
            "#{timestamp}, #{ipaddress}, #{httpCall}, #{httpStatusCode}, #{userAgent})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long save(AccessLogEntry accessLogEntry);

}
