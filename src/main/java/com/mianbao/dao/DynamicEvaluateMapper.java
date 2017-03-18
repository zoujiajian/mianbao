package com.mianbao.dao;

import com.mianbao.domain.DynamicEvaluate;
import com.mianbao.domain.DynamicEvaluateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicEvaluateMapper {
    long countByExample(DynamicEvaluateExample example);

    int deleteByExample(DynamicEvaluateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DynamicEvaluate record);

    int insertSelective(DynamicEvaluate record);

    List<DynamicEvaluate> selectByExample(DynamicEvaluateExample example);

    DynamicEvaluate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DynamicEvaluate record, @Param("example") DynamicEvaluateExample example);

    int updateByExample(@Param("record") DynamicEvaluate record, @Param("example") DynamicEvaluateExample example);

    int updateByPrimaryKeySelective(DynamicEvaluate record);

    int updateByPrimaryKey(DynamicEvaluate record);
}