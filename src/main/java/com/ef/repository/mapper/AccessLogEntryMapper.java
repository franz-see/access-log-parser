package com.ef.repository.mapper;

import com.ef.domain.AccessLogEntry;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface AccessLogEntryMapper {

    @Delete("delete from access_log_entry")
    void deleteAll();

    @Insert({"<script>",
            "insert into  access_log_entry (timestamp, ip_address, http_call, http_status_code, user_agent) values ",
            "<foreach collection='accessLogEntries' item='accessLogEntry' index='index' open='(' separator = '),(' close=')' >" +
                    "#{accessLogEntry.timestamp}," +
                    "#{accessLogEntry.ipaddress}," +
                    "#{accessLogEntry.httpCall}," +
                    "#{accessLogEntry.httpStatusCode}," +
                    "#{accessLogEntry.userAgent}" +
            "</foreach>",
            "</script>"})
    void save(@Param("accessLogEntries") Collection<AccessLogEntry> accessLogEntries);

}
