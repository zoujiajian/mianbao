package com.mianbao.dao;


import java.util.List;

import com.mianbao.domain.UserLike;
import com.mianbao.domain.UserLikeExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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

    List<UserLike> selectScenicSpotWithLimit(@Param("userId") int userId, @Param("limit") int limit, @Param("offset") int offset);

    List<UserLike> selectScenicSpotWithLimitAndCondi(@Param("scenicIds") List<Integer> scenicIds, @Param("userId") int userId, @Param("limit") int limit, @Param("offset") int offset);
}