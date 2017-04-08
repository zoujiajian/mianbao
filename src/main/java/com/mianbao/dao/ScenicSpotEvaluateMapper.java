package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.ScenicSpotEvaluate;
import com.mianbao.domain.ScenicSpotEvaluateExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotEvaluateMapper {
    long countByExample(ScenicSpotEvaluateExample example);

    int deleteByExample(ScenicSpotEvaluateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScenicSpotEvaluate record);

    int insertSelective(ScenicSpotEvaluate record);

    List<ScenicSpotEvaluate> selectByExample(ScenicSpotEvaluateExample example);

    ScenicSpotEvaluate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScenicSpotEvaluate record, @Param("example") ScenicSpotEvaluateExample example);

    int updateByExample(@Param("record") ScenicSpotEvaluate record, @Param("example") ScenicSpotEvaluateExample example);

    int updateByPrimaryKeySelective(ScenicSpotEvaluate record);

    int updateByPrimaryKey(ScenicSpotEvaluate record);
}