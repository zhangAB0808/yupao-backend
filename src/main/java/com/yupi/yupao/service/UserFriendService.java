package com.yupi.yupao.service;

import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserFriend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 */
public interface UserFriendService extends IService<UserFriend> {

    /**
     * 查出用户所有好友信息
     * @param loginUser
     * @return
     */
    List<UserVo> searchFriends(User loginUser);

    /**
     * 判断其是否为用户好友
     * @param user
     * @param request
     * @return
     */
    boolean isFriend(User user, HttpServletRequest request);
    boolean isFriend(User user, User loginUser);

}
