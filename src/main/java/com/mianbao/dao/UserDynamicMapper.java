package com.mianbao.dao;

import com.mianbao.domain.UserDynamic;
import com.mianbao.domain.UserDynamicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDynamicMapper {
    long countByExample(UserDynamicExample example);

    int deleteByExample(UserDynamicExample example);

    int deleteByPrimaryKey(Integer dynamicId);

    int insert(UserDynamic record);

    int insertSelective(UserDynamic record);

    List<UserDynamic> selectByExample(UserDynamicExample example);

    UserDynamic selectByPrimaryKey(Integer dynamicId);

    int updateByExampleSelective(@Param("record") UserDynamic record, @Param("example") UserDynamicExample example);

    int updateByExample(@Param("record") UserDynamic record, @Param("example") UserDynamicExample example);

    int updateByPrimaryKeySelective(UserDynamic record);

    int updateByPrimaryKey(UserDynamic record);
}