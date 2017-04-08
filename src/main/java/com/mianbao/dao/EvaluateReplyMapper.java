package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.EvaluateReply;
import com.mianbao.domain.EvaluateReplyExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluateReplyMapper {
    long countByExample(EvaluateReplyExample example);

    int deleteByExample(EvaluateReplyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EvaluateReply record);

    int insertSelective(EvaluateReply record);

    List<EvaluateReply> selectByExample(EvaluateReplyExample example);

    EvaluateReply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EvaluateReply record, @Param("example") EvaluateReplyExample example);

    int updateByExample(@Param("record") EvaluateReply record, @Param("example") EvaluateReplyExample example);

    int updateByPrimaryKeySelective(EvaluateReply record);

    int updateByPrimaryKey(EvaluateReply record);
}