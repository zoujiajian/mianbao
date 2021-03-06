package com.mianbao.dao;

import com.mianbao.domain.ScenicSpotEvaluate;
import com.mianbao.domain.ScenicSpotEvaluateExample;
import java.util.List;
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

    List<ScenicSpotEvaluate> selectWithLimit(@Param("scenicId") int scenicId, @Param("limit") int limit, @Param("offset") int offset);

    Double selectAvgScore(int scenicId);

}