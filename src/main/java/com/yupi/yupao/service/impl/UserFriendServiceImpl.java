package com.yupi.yupao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserFriend;
import com.yupi.yupao.model.vo.UserVo;
import com.yupi.yupao.service.UserFriendService;
import com.yupi.yupao.mapper.UserFriendMapper;
import com.yupi.yupao.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend>
        implements UserFriendService {

    @Resource
    private UserService userService;

    /**
     * 根据登录用户查出其好友信息
     *
     * @param loginUser
     * @return
     */
    @Override
    public List<UserVo> searchFriends(User loginUser) {
        //是否登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long id = loginUser.getId();
        // 查出好友
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id).or().eq("friendId", id);
        List<UserFriend> list = this.list(queryWrapper);

        List<UserVo> result = list.stream().map(userFriend -> {
            Long friendId = userFriend.getFriendId();
            User friend;
            if (!friendId.equals(id)) {
                friend = userService.getById(friendId);
            } else {
                Long userId = userFriend.getUserId();
                friend = userService.getById(userId);
            }
            UserVo userFriendVo = new UserVo();
            BeanUtils.copyProperties(friend, userFriendVo);
            return userFriendVo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 判断其是否为用户好友
     * @param user
     * @param request
     * @return
     */
    @Override
    public boolean isFriend(User user, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return isFriend(user, loginUser);
    }

    @Override
    public boolean isFriend(User user, User loginUser) {
        Long loginUserId = loginUser.getId();
        Long userId = user.getId();
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();

        //select * from userFriend where (userId=? and friendId=?) or (friendId=? and userId=?)
        queryWrapper.and(qw -> {
            qw.eq("userId", userId).eq("friendId", loginUserId);
        })
                .or(qw -> {
                    qw.eq("userId", loginUserId).eq("friendId", userId);
                });
        UserFriend one = this.getOne(queryWrapper);
        return one!=null;
    }


}




