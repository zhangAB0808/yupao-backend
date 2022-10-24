package com.yupi.yupao.mapper;

import com.yupi.yupao.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity com.yupi.usercenter.model.domain.User
 */
public interface UserMapper extends BaseMapper<User> {

//    @Select("select u.* from team t  join userTeam ut on ut.teamId=#{teamId}  join" +
//            "user u on u.id=ut.userId")
//    List<User> searchTeamMembers(@Param("teamId") Long teamId);
}




