package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.UserLike;
import com.mianbao.domain.UserLikeExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikeMapper {
    long countByExample(UserLikeExample example);

    int deleteByExample(UserLikeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserLike record);

    int insertSelective(UserLike record);

    List<UserLike> selectByExample(UserLikeExample example);

    UserLike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserLike record, @Param("example") UserLikeExample example);

    int updateByExample(@Param("record") UserLike record, @Param("example") UserLikeExample example);

    int updateByPrimaryKeySelective(UserLike record);

    int updateByPrimaryKey(UserLike record);
}