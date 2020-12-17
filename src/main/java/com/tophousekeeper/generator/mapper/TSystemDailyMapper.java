package com.tophousekeeper.generator.mapper;

import com.tophousekeeper.generator.pojo.TSystemDaily;
import com.tophousekeeper.generator.pojo.TSystemDailyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TSystemDailyMapper {
    long countByExample(TSystemDailyExample example);

    int deleteByExample(TSystemDailyExample example);

    int insert(TSystemDaily record);

    int insertSelective(TSystemDaily record);

    List<TSystemDaily> selectByExample(TSystemDailyExample example);

    int updateByExampleSelective(@Param("record") TSystemDaily record, @Param("example") TSystemDailyExample example);

    int updateByExample(@Param("record") TSystemDaily record, @Param("example") TSystemDailyExample example);
}