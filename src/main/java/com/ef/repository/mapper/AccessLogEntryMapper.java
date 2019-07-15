package com.ef.repository.mapper;

import com.ef.domain.AccessLogEntry;
import com.ef.domain.PeriodSummary;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    @Results(id = "periodSummaries", value = {
            @Result(property = "ipaddress", column = "ip_address", javaType = String.class),
            @Result(property = "startPeriod", column = "start_period", javaType = LocalDateTime.class),
            @Result(property = "endPeriod", column = "end_period", javaType = LocalDateTime.class),
            @Result(property = "count", column = "cnt", javaType = Long.class),
            @Result(property = "comment", column = "comment", javaType = String.class)
    })
    @Select("select tmp.ip_address\n" +
            "     , tmp.period as start_period\n" +
            "     , DATE_ADD(tmp.period, interval 1 hour) as end_period\n" +
            "     , tmp.cnt\n" +
            "     , concat(tmp.ip_address, \" did \", tmp.cnt, \" call(s) from \", tmp.period, \" to \", DATE_ADD(tmp.period, interval 1 hour)) as comment\n" +
            "  from (\n" +
            "        select ip_address\n" +
            "             , DATE_FORMAT(timestamp, concat(\"%Y-%m-%d %H:\", LPAD(#{startDateTime.minute}, 2, '0'), \":\", LPAD(#{startDateTime.second}, 2, '0'))) as period\n" +
            "             , count(1) as cnt\n" +
            "          from access_log_entry\n" +
            "         where timestamp >= #{startDateTime}\n" +
            "        group by ip_address, period\n" +
            "        having count(1) >= #{threshold}\n" +
            "        order by count(1) desc\n" +
            "       ) tmp")
    List<PeriodSummary> findHourlyCount(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("threshold") int threshold);

    @ResultMap("periodSummaries")
    @Select("select tmp.ip_address\n" +
            "     , tmp.period as start_period\n" +
            "     , DATE_ADD(tmp.period, interval 1 day) as end_period\n" +
            "     , tmp.cnt\n" +
            "     , concat(tmp.ip_address, \" did \", tmp.cnt, \" call(s) from \", tmp.period, \" to \", DATE_ADD(tmp.period, interval 1 hour)) as comment\n" +
            "  from (\n" +
            "        select ip_address\n" +
            "             , DATE_FORMAT(timestamp, concat(\"%Y-%m-%d \", LPAD(#{startDateTime.hour}, 2, '0'), \":\", LPAD(#{startDateTime.minute}, 2, '0'), \":\", LPAD(#{startDateTime.second}, 2, '0'))) as period\n" +
            "             , count(1) as cnt\n" +
            "          from access_log_entry\n" +
            "         where timestamp >= #{startDateTime}\n" +
            "        group by ip_address, period\n" +
            "        having count(1) >= #{threshold}\n" +
            "        order by count(1) desc\n" +
            "       ) tmp")
    List<PeriodSummary> findDailyCount(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("threshold") int threshold);
}
