package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.ScenicSpot;
import com.mianbao.domain.ScenicSpotExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotMapper {
    long countByExample(ScenicSpotExample example);

    int deleteByExample(ScenicSpotExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScenicSpot record);

    int insertSelective(ScenicSpot record);

    List<ScenicSpot> selectByExample(ScenicSpotExample example);

    ScenicSpot selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScenicSpot record, @Param("example") ScenicSpotExample example);

    int updateByExample(@Param("record") ScenicSpot record, @Param("example") ScenicSpotExample example);

    int updateByPrimaryKeySelective(ScenicSpot record);

    int updateByPrimaryKey(ScenicSpot record);
}