package com.mianbao.dao;

import com.mianbao.domain.ScenicSpotDynamic;
import com.mianbao.domain.ScenicSpotDynamicExample;
import java.util.List;
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
}