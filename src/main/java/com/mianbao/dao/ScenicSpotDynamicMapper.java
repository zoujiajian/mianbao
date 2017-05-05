package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.ScenicSpotDynamic;
import com.mianbao.domain.ScenicSpotDynamicExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenicSpotDynamicMapper {
    long countByExample(ScenicSpotDynamicExample example);

    int deleteByExample(ScenicSpotDynamicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ScenicSpotDynamic record);

    int insertSelective(ScenicSpotDynamic record);

    List<ScenicSpotDynamic> selectByExample(ScenicSpotDynamicExample example);

    ScenicSpotDynamic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ScenicSpotDynamic record, @Param("example") ScenicSpotDynamicExample example);

    int updateByExample(@Param("record") ScenicSpotDynamic record, @Param("example") ScenicSpotDynamicExample example);

    int updateByPrimaryKeySelective(ScenicSpotDynamic record);

    int updateByPrimaryKey(ScenicSpotDynamic record);

    int insertScenicDynamicList(List<ScenicSpotDynamic> scenicSpotDynamicList);

    List<ScenicSpotDynamic> selectWithLimit(@Param("scenicId") int scenicId, @Param("limit") int limit, @Param("offset") int offset);
}