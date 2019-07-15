package com.ef.repository.mapper;

import com.ef.domain.PeriodSummary;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PeriodSummaryMapper {

    @Delete("delete from period_summary")
    void deleteAll();

    @Insert({"<script>",
            "insert into period_summary (ip_address, start_period, end_period, cnt, comment) values ",
            "<foreach collection='periodSummaries' item='periodSummary' index='index' open='(' separator = '),(' close=')' >" +
                    "#{periodSummary.ipaddress}," +
                    "#{periodSummary.startPeriod}," +
                    "#{periodSummary.endPeriod}," +
                    "#{periodSummary.count}," +
                    "#{periodSummary.comment}" +
            "</foreach>",
            "</script>"})
    void save(@Param("periodSummaries") Collection<PeriodSummary> periodSummaries);

    @Results(id = "periodSummaries", value = {
            @Result(property = "ipaddress", column = "ip_address", javaType = String.class),
            @Result(property = "startPeriod", column = "start_period", javaType = LocalDateTime.class),
            @Result(property = "endPeriod", column = "end_period", javaType = LocalDateTime.class),
            @Result(property = "count", column = "cnt", javaType = Long.class),
            @Result(property = "comment", column = "comment", javaType = String.class)
    })
    @Select("select * from period_summary")
    List<PeriodSummary> findAll();
}
