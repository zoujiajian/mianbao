package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.ScenicSpot;
import com.mianbao.domain.ScenicSpotExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface ScenicSpotMapper {
    long countByExample(ScenicSpotExample example);

    int deleteByExample(ScenicSpotExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScenicSpot record);

    int insertSelective(ScenicSpot record);

    List<ScenicSpot> selectByExample(ScenicSpotExample example);

    List<ScenicSpot> getAllScenicSpot();

    ScenicSpot selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScenicSpot record, @Param("example") ScenicSpotExample example);

    int updateByExample(@Param("record") ScenicSpot record, @Param("example") ScenicSpotExample example);

    int updateByPrimaryKeySelective(ScenicSpot record);

    int updateByPrimaryKey(ScenicSpot record);

    List<ScenicSpot> selectTopScenicSpot(@Param("limit") Integer limit,@Param("offset") Integer offset);

    List<ScenicSpot> selectTopScenicSpotWithCondition(@Param("condition") String condition,@Param("limit") Integer limit,@Param("offset") Integer offset);

    long countAll();
}