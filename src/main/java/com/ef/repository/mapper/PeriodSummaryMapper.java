package com.ef.repository.mapper;

import com.ef.domain.PeriodSummary;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

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
}
